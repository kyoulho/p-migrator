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
 * Dong-Heon Han    Oct 12, 2022		First Draft.
 */

package io.playce.migrator.analysis.process.component.impl;

import io.playce.migrator.component.AbstractWebsocketObserver;
import io.playce.migrator.constant.JobStatus;
import io.playce.migrator.constant.JobStep;
import io.playce.migrator.dto.progress.JobProgress;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Component;

@Component("/user/topic/analysis-status")
@RequiredArgsConstructor
public class AnalysisStatusComponent extends AbstractWebsocketObserver {
    private final SimpMessagingTemplate messagingTemplate;

    @Override
    public void sendMessage(Object message) {
        super.notification(message, messagingTemplate);
    }

    public void sendStep0Status(Long projectId, Long applicationId, Long analysisHistoryId, JobStatus status) {
        JobProgress progress = JobProgress.builder().projectId(projectId).applicationId(applicationId).historyId(analysisHistoryId)
                .currentStep(JobStep.STEP0.getDisplay()).status(status).current(0).total(0).build();
        sendMessage(progress);
    }
}