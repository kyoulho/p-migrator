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
 * Dong-Heon Han    Aug 24, 2022		First Draft.
 */

package io.playce.migrator.analysis.process.service;

import io.playce.migrator.analysis.process.component.impl.AnalysisStatusComponent;
import io.playce.migrator.constant.JobStatus;
import io.playce.migrator.dto.analysis.AnalysisTaskItem;
import io.playce.migrator.scheduler.task.ITask;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;
import java.util.List;
import java.util.concurrent.BlockingQueue;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScheduleAnalysisService implements ITask {
    private final BlockingQueue<AnalysisTaskItem> analysisQueue;
    private final AnalysisHistoryService analysisHistoryService;
    private final AnalysisProcessService analysisProcessService;
    private final AnalysisStatusComponent analysisStatusComponent;

    @PostConstruct
    public void init() {
        analysisHistoryService.handlingInProgressItemFailures();
    }

    @Override
    public void execute() {
        List<AnalysisTaskItem> items = analysisHistoryService.getRequestedAnalysisHistory();
        items.forEach(i -> {
            log.debug("analysisQueue in <- applicationId: {}, analysisHistoryId: {}", i.getApplicationId(), i.getAnalysisHistoryId());
            try {
                analysisQueue.put(i);
                analysisStatusComponent.sendStep0Status(i.getProjectId(), i.getApplicationId(), i.getAnalysisHistoryId(), JobStatus.PEND);
                analysisHistoryService.setReqStatusToPend(i.getAnalysisHistoryId());
                analysisProcessService.processTask();
                log.debug("complete item: {}", i);
            } catch (InterruptedException e) {
                log.error("analysis failed: {}", i);
            }
        });
    }
}