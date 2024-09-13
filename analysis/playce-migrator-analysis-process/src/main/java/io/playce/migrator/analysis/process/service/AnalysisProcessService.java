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
 * Dong-Heon Han    Aug 17, 2022		First Draft.
 */

package io.playce.migrator.analysis.process.service;

import io.playce.migrator.analysis.process.component.IAnalysisProvider;
import io.playce.migrator.analysis.process.component.impl.AnalysisStatusComponent;
import io.playce.migrator.config.PlayceMigratorConfig;
import io.playce.migrator.constant.ErrorCode;
import io.playce.migrator.constant.FileType;
import io.playce.migrator.constant.JobStatus;
import io.playce.migrator.constant.JobStep;
import io.playce.migrator.dao.entity.ApplicationOverview;
import io.playce.migrator.dto.analysis.AnalysisTaskItem;
import io.playce.migrator.dto.progress.JobProgress;
import io.playce.migrator.exception.PlayceMigratorException;
import io.playce.migrator.rule.loader.RuleSessionFactory;
import io.playce.migrator.rule.loader.service.impl.DatabaseRuleLoader;
import io.playce.migrator.util.PathUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.time.Instant;
import java.util.Map;
import java.util.concurrent.BlockingQueue;

import static io.playce.migrator.util.WebSocketSender.sendMessage;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnalysisProcessService {
    private final PlayceMigratorConfig playceMigratorConfig;
    private final AnalysisHistoryService analysisHistoryService;
    private final ApplicationOverviewService applicationOverviewService;
    private final DatabaseRuleLoader databaseRuleLoader;
    private final AnalysisPostService analysisPostService;
    private final Map<String, IAnalysisProvider> analysisProviderMap;
    private final BlockingQueue<AnalysisTaskItem> analysisQueue;
    private final AnalysisStatusComponent analysisStatusComponent;
    private final ApplicationSummaryService applicationSummaryService;

    @Async("analysisExecutor")
    public void processTask() {
        try {
            AnalysisTaskItem item = analysisQueue.take();
            log.debug("analysisQueue out -> applicationId: {}, analysisHistoryId: {}", item.getApplicationId(), item.getAnalysisHistoryId());
            process(item);
        } catch (InterruptedException e) {
            throw new PlayceMigratorException(ErrorCode.PM701T, e.getMessage(), e);
        }
    }

    public void process(AnalysisTaskItem item) {
        Path appPath = playceMigratorConfig.getAppPath(item.getOriginFileName(), item.getApplicationId());

        JobProgress progress = JobProgress.builder().projectId(item.getProjectId()).applicationId(item.getApplicationId()).historyId(item.getAnalysisHistoryId()).originFileName(item.getOriginFileName()).build();
        try {
            //1 - update start
            log.debug("start analysis - analysisHistoryId: {}, {}", item.getAnalysisHistoryId(), item);

            //update status
            analysisHistoryService.updateJobToProcess(item, Instant.now());

            if(isCanceled(item, progress, JobStep.STEP1)) return;

            Long applicationId = item.getApplicationId();
            ApplicationOverview applicationOverview = applicationOverviewService.getApplicationOverview(applicationId);
            Long targetFileCount = applicationOverview.getTargetFileCount();
            boolean prepareProcessYn = applicationOverview.getPrepareProcessYn();
            if(!prepareProcessYn || targetFileCount == null) {
                applicationOverviewService.prepare(item);

                long totalCount = PathUtils.getFileCount(appPath);
                applicationOverview.setTargetFileCount(totalCount);
            }
            progress.setTotal(applicationOverview.getTargetFileCount() + 15);
            sendMessage(analysisStatusComponent, progress, JobStep.STEP1, JobStatus.PROC, "prepared.");

            //2 - summary.
            if (!prepareProcessYn) {
                sendMessage(analysisStatusComponent, progress, JobStep.STEP2, JobStatus.PROC, "create summary.");
                applicationSummaryService.createSummaries(item, appPath, progress);
                applicationOverview.setPrepareProcessYn(true);
                applicationOverviewService.update(applicationOverview);
            }

            //3 - get rule factory
            //load rule file-type 별 ruleSessionFactory
            Map<FileType, RuleSessionFactory> ruleSessionFactoryMap = databaseRuleLoader.getSessionFactoryMapByTargetId(item.getTargetId());

            //4 - history 삭제
            sendMessage(analysisStatusComponent, progress, JobStep.STEP3, JobStatus.PROC, "remove history.");
            analysisPostService.removeAnalysisHistoryDetails(item);

            //5 - rule check
            sendMessage(analysisStatusComponent, progress, JobStep.STEP4, JobStatus.PROC, "run rule.");
            for (FileType type : ruleSessionFactoryMap.keySet()) {
                //check cancel
                if(isCanceled(item, progress, JobStep.STEP4)) return;

                IAnalysisProvider analysisProvider = analysisProviderMap.get(type.getProvider());
                RuleSessionFactory factory = ruleSessionFactoryMap.get(type);
                if (factory == null) continue;

                try {
                    analysisProvider.check(item, type, appPath, factory, progress);
                } catch (PlayceMigratorException e) {
                    log.error(e.getMessage(), e);
                    throw new Exception(e);
                }
            }
            analysisHistoryService.updateJobToComplete(item, Instant.now());
            sendMessage(analysisStatusComponent, progress, JobStep.STEP5, JobStatus.CMPL, "done.");
            log.debug("done analysis - analysisHistoryId: {}", item.getAnalysisHistoryId());
        } catch (Exception e) {
            String message = e.toString();
            analysisHistoryService.updateJobToFail(item, message, Instant.now());
            sendMessage(analysisStatusComponent, progress, JobStep.STEP5, JobStatus.FAIL, "failed.", message);
            log.error("failed analysis - analysisHistoryId: {}", item.getAnalysisHistoryId(), e);
        }
    }

    private boolean isCanceled(AnalysisTaskItem item, JobProgress progress, JobStep jobStep) {
        JobStatus jobStatus = analysisHistoryService.getJobStatus(item);
        if (jobStatus == JobStatus.CNCL) {
            log.info("This job has been cancelled. {}", item);
            analysisHistoryService.updateJobToCancel(item, Instant.now());
            sendMessage(analysisStatusComponent, progress, jobStep, JobStatus.CNCL, "done");
            return true;
        }
        return false;
    }
}