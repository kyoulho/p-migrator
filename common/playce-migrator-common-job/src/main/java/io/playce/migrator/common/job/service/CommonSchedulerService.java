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
 * Dong-Heon Han    Aug 23, 2022		First Draft.
 */

package io.playce.migrator.common.job.service;

import io.playce.migrator.constant.TaskType;
import io.playce.migrator.scheduler.component.TaskRunnerComponent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CommonSchedulerService {
    private final TaskRunnerComponent taskRunnerComponent;

    @Scheduled(fixedDelayString = "#{commonSchedulerConfig.removeGarbageFilesFixed.fixedDelay * 1000}", initialDelayString = "#{commonSchedulerConfig.removeGarbageFilesFixed.initDelay * 1000}")
    public void runSchedule() {
        taskRunnerComponent.runTaskService(TaskType.Garbege);
    }
}