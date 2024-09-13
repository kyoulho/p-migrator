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
 * Dong-Heon Han    Sep 21, 2022		First Draft.
 */

package io.playce.migrator.analysis.process.component.impl;

import io.playce.migrator.analysis.process.component.IAnalysisProvider;
import io.playce.migrator.analysis.process.service.AnalysisPostService;
import io.playce.migrator.analysis.process.service.ApplicationOverviewService;
import io.playce.migrator.constant.ErrorCode;
import io.playce.migrator.constant.FileType;
import io.playce.migrator.constant.JobStatus;
import io.playce.migrator.constant.JobStep;
import io.playce.migrator.dto.analysis.AnalysisRuleRequest;
import io.playce.migrator.dto.analysis.AnalysisRuleResult;
import io.playce.migrator.dto.analysis.AnalysisTaskItem;
import io.playce.migrator.dto.progress.JobProgress;
import io.playce.migrator.exception.PlayceMigratorException;
import io.playce.migrator.rule.loader.RuleSession;
import io.playce.migrator.rule.loader.RuleSessionFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Component;

import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

import static io.playce.migrator.util.WebSocketSender.sendMessage;

@Component
@RequiredArgsConstructor
@Slf4j
public class JarDependencyRuleCheckProvider implements IAnalysisProvider {
    private final ApplicationOverviewService applicationOverviewService;
    private final AnalysisPostService analysisPostService;
    private final AnalysisStatusComponent analysisStatusComponent;

    @Override
    public void check(AnalysisTaskItem item, FileType type, Path appPath, RuleSessionFactory factory, JobProgress progress) {
        try {
            List<String> libraries = applicationOverviewService.getLibraries(item.getApplicationId());
            List<AnalysisRuleResult> analysisRuleResults = analysisProcess(appPath, libraries, item, type, factory, progress);
            analysisPostService.saveAnalysisHistoryDetails(item, analysisRuleResults);
        } catch (RuntimeException e) {
            throw new PlayceMigratorException(ErrorCode.PM701T, e);
        }
    }

    private List<AnalysisRuleResult> analysisProcess(Path filePath, List<String> libraries, AnalysisTaskItem item, FileType type, RuleSessionFactory factory, JobProgress progress) {
        RuleSession ruleSession = factory.newSession();

        List<AnalysisRuleResult> analysisRuleResults = new ArrayList<>();
        for(String library: libraries) {
            AnalysisRuleRequest request = new AnalysisRuleRequest();
            request.setContent(library);
            ruleSession.execute(request);

            AnalysisRuleResult result = request.getResult();
            if (ObjectUtils.isNotEmpty(result)) {
                result.setAnalysisFilePath(filePath.getParent().toString());
                result.setAnalysisFileName(library);

                analysisRuleResults.add(result);
                log.debug("result - file: {}", library);
            }
        }
        sendMessage(analysisStatusComponent, progress, JobStep.STEP4, JobStatus.PROC, "process dependencies");
        return analysisRuleResults;
    }
}