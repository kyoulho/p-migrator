/*
 * Copyright 2022 The playce-migrator-mvp Project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Revision History
 * Author			Date				Description
 * ---------------	----------------	------------
 * Dong-Heon Han    Aug 29, 2022		First Draft.
 */

package io.playce.migrator.migration.process.async;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.playce.migrator.constant.ErrorCode;
import io.playce.migrator.constant.MigratorPath;
import io.playce.migrator.dao.entity.ChangedFile;
import io.playce.migrator.dao.mybatis.MybatisMigrationRuleLink;
import io.playce.migrator.dao.repository.ChangedFileRepository;
import io.playce.migrator.dto.migration.MigrationRuleRequest;
import io.playce.migrator.dto.migration.MigrationRuleResult;
import io.playce.migrator.dto.migration.MigrationTaskItem;
import io.playce.migrator.exception.PlayceMigratorException;
import io.playce.migrator.rule.loader.RuleSession;
import io.playce.migrator.rule.loader.RuleSessionFactory;
import io.playce.migrator.util.JacksonObjectMapperUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.MalformedInputException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;

@Component
@RequiredArgsConstructor
@Slf4j
public class MigrationRuleComponent {

    private final ChangedFileRepository changedFileRepository;
    private final ObjectMapper objectMapper;

    @Async("workerExecutor")
    public CompletableFuture<Void> migration(Path source, Path target, RuleSessionFactory factory,
                                             List<MybatisMigrationRuleLink> links, MigrationTaskItem item, List<ChangedFile> changedFileList) {
        return migration(source, target, factory, links, StandardCharsets.UTF_8, item, changedFileList);
    }

    private CompletableFuture<Void> migration(Path source, Path target, RuleSessionFactory factory, List<MybatisMigrationRuleLink> links,
                                              Charset charset, MigrationTaskItem item, List<ChangedFile> changedFileList) {
        RuleSession ruleSession = factory.newSession();

        log.debug("migration file: {}, target : {}", source, target);

        try(BufferedReader br = new BufferedReader(Files.newBufferedReader(source, charset));
            FileWriter fw = new FileWriter(target.toFile())
        ) {
            String line;
            int lineNumber = 0;
            while((line = br.readLine()) != null) {
                lineNumber ++;

                MigrationRuleRequest request = null;
                MigrationRuleResult result = null;
                for (MybatisMigrationRuleLink link : links) {
                    request = new MigrationRuleRequest();

                    //input 값.
                    Map<String, String> input = JacksonObjectMapperUtil.getObject(objectMapper, link.getMigrationCondition());

                    request.setStartLineNumber(lineNumber);
                    request.setContent(result == null ? line : result.getContent());
                    request.setInput(input);
                    ruleSession.execute(request);
                    result = request.getResult();
                }
                assert request != null;

                fw.write(result.getContent());
                fw.write(StringUtils.LF);

                // 변경된 파일을 확인 후 add
                if (!request.getContent().equals(result.getContent())) {
                    if (isDuplicatedFiles(source, item, changedFileList)) continue;

                    ChangedFile cf = new ChangedFile();
                    cf.setMigrationHistoryId(item.getMigrationHistoryId());
                    cf.setOriginFileAbsolutePath(source.toString());
                    cf.setFilePathInResult(target.toString().substring(target.toString().indexOf(MigratorPath.unzip.path()) + 6));
                    changedFileList.add(cf);
                    log.trace("change from: {}", line);
                    log.trace("         to: {}", result.getContent());
                }
            }
            fw.flush();
        } catch (MalformedInputException e) {
            return migration(source, target, factory, links, StandardCharsets.ISO_8859_1, item, changedFileList);
        } catch (IOException e) {
            log.error(e.getMessage(), e);
            throw new PlayceMigratorException(ErrorCode.PM371F, e);
        }
        log.debug("done file: {}", source);
        return null;
    }

    private boolean isDuplicatedFiles(Path source, MigrationTaskItem item, List<ChangedFile> changedFileList) {
        if (changedFileList.stream().anyMatch(f -> f.getOriginFileAbsolutePath().equals(source.toString())))
            return true;

        // 중복 체크
        ChangedFile duplicateFile = changedFileRepository.findByMigrationHistoryIdAndOriginFileAbsolutePath(item.getMigrationHistoryId(), source.toString());
        return duplicateFile != null;
    }
}