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
 * Dong-Heon Han    Aug 08, 2022		First Draft.
 */

package io.playce.migrator.rule.loader.service.impl;

import io.playce.migrator.constant.ErrorCode;
import io.playce.migrator.constant.FileType;
import io.playce.migrator.exception.PlayceMigratorException;
import io.playce.migrator.rule.loader.RuleSessionFactory;
import lombok.extern.slf4j.Slf4j;
import org.kie.api.io.Resource;
import org.kie.api.io.ResourceType;
import org.kie.internal.builder.KnowledgeBuilder;
import org.kie.internal.builder.KnowledgeBuilderFactory;
import org.kie.internal.io.ResourceFactory;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.io.StringReader;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

@Service("fileRuleLoader")
@Slf4j
public class FileRuleLoader extends AbstractRuleLoader {
    @Override
    public RuleSessionFactory createNewSessionFactory(Long targetId, FileType fileType) {
        Path dirPath = Path.of("rules", "analysis", String.valueOf(targetId), fileType.name());
        if (!dirPath.toFile().isDirectory()) {
            throw new PlayceMigratorException(ErrorCode.PM305F, "RuleGroup not found");
        }

        KnowledgeBuilder builder = KnowledgeBuilderFactory.newKnowledgeBuilder();
        loadFileContent(dirPath, builder);

        return createRuleSessionFactory(builder, "check target-id: " + targetId + ", file-type: " +  fileType.name());
    }

    private void
    loadFileContent(Path dir, KnowledgeBuilder builder) {
        for (File rule : Objects.requireNonNull(dir.toFile().listFiles())) {
            try {
                Path filePath = Path.of(rule.toURI());
                String content = Files.readString(filePath);

                String fileName = rule.getName();
                int index = fileName.indexOf(".");
                if (index == -1) {
                    log.error("wrong file name: {}", fileName);
                    continue;
                }

                log.debug("load file-rule: {}", rule);
                String strRuleId = fileName.substring(0, index);
                content = setRuleId(content, Long.parseLong(strRuleId));
                Resource resource = ResourceFactory.newReaderResource(new StringReader(content));
                builder.add(resource, ResourceType.DRL);
            } catch (IOException e) {
                throw new PlayceMigratorException(ErrorCode.PM306F, e);
            }
        }
    }

    @Override
    public Map<FileType, RuleSessionFactory> getSessionFactoryMapByTargetId(Long targetId) {
        //Not Impl 사용하지 않아서 구현하지 않음.
        return null;
    }

    @Override
    public Map<Long, RuleSessionFactory> getSessionFactoryMapByMigrationIds(List<Long> migrationRuleIds) {
        KnowledgeBuilder builder = KnowledgeBuilderFactory.newKnowledgeBuilder();

        Map<Long, RuleSessionFactory> result = new HashMap<>();
        for(Long migrationRuleId: migrationRuleIds) {
            loadRuleByMigrationId(builder, result, migrationRuleId);
        }
        return result;
    }

    private void loadRuleByMigrationId(KnowledgeBuilder builder, Map<Long, RuleSessionFactory> result, Long migrationRuleId) {
        Path dirPath = Path.of("rules", "migration", String.valueOf(migrationRuleId));
        log.debug("load path: {}", dirPath);
        for(File type: Objects.requireNonNull(dirPath.toFile().listFiles())) {
            loadRuleByType(builder, result, migrationRuleId, type);
        }
    }

    private void loadRuleByType(KnowledgeBuilder builder, Map<Long, RuleSessionFactory> result, Long migrationRuleId, File type) {
        loadFileContent(type.toPath(), builder);

        RuleSessionFactory ruleSessionFactory = createRuleSessionFactory(builder, "check migrationRuleId: " + migrationRuleId);
        result.put(migrationRuleId, ruleSessionFactory);
    }
}