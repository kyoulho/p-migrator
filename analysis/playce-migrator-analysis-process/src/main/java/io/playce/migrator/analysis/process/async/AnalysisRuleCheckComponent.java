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
 * Dong-Heon Han    Aug 18, 2022		First Draft.
 */

package io.playce.migrator.analysis.process.async;

import io.playce.migrator.constant.ErrorCode;
import io.playce.migrator.dto.analysis.AnalysisRuleRequest;
import io.playce.migrator.dto.analysis.AnalysisRuleResult;
import io.playce.migrator.dto.analysis.AnalysisRuleResults;
import io.playce.migrator.exception.PlayceMigratorException;
import io.playce.migrator.rule.loader.RuleSession;
import io.playce.migrator.rule.loader.RuleSessionFactory;
import io.playce.migrator.util.StringNullChecker;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.BufferedReader;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.MalformedInputException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
@Slf4j
public class AnalysisRuleCheckComponent {
    private final List<String> splitList = List.of("/unzip/", "/decompile/", "/migration/", "/sub-jars/");

    @Async("workerExecutor")
    public CompletableFuture<AnalysisRuleResults> apply(Path filePath, RuleSessionFactory factory, Charset charset) { //throws TaskRejectedException {
        AnalysisRuleResults analysisRuleResults = new AnalysisRuleResults(filePath);
        RuleSession ruleSession = factory.newSession();

        int lineNumber = 0;
        log.debug("analysis - file: {}", filePath);
        try(BufferedReader br = new BufferedReader(Files.newBufferedReader(filePath, charset))) {
            String line;
            long start = System.currentTimeMillis();
            while ((line = br.readLine()) != null) {
                lineNumber++;

                if (StringNullChecker.isEmpty(line)) continue;

                AnalysisRuleRequest request = new AnalysisRuleRequest();
                request.setStartLineNumber(lineNumber);
                request.setContent(line);
                ruleSession.execute(request);

                AnalysisRuleResult result = request.getResult();
                String originFilePath = filePath.toString();
                if (ObjectUtils.isNotEmpty(result)) {
                    result.setLineNumber(lineNumber);
                    result.setAnalysisOriginFilePath(originFilePath);
                    result.setAnalysisFilePath(getFilePath(filePath.getParent().toString()));
                    result.setAnalysisFileName(filePath.getFileName().toString());

                    analysisRuleResults.addResult(result);
                    log.debug("result - detect: {}, comment: {}, file: {}:{}", result.getDetectedString(), result.getOverrideComment(), filePath, lineNumber);
                }
            }
            log.debug("analysis - file: {}, totalLine:{}, duration: {}", filePath, lineNumber, System.currentTimeMillis() - start);
        } catch (MalformedInputException e) {
            return apply(filePath, factory, StandardCharsets.ISO_8859_1);
        } catch (IOException e) {
            log.error("error - lineNumber: {}", lineNumber);
            throw new PlayceMigratorException(ErrorCode.PM371F, e);
        }

        return CompletableFuture.completedFuture(analysisRuleResults);
    }

    private String getFilePath(String fullPath) {
        for(String split: splitList) {
            int index = fullPath.indexOf(split);
            if(index != -1) {
                fullPath = fullPath.substring(index + split.length());
            }
        }
        return fullPath;
    }
}