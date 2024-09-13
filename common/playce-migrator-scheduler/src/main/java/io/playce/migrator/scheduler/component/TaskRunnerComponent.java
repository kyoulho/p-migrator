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
 * Dong-Heon Han    Aug 26, 2022		First Draft.
 */

package io.playce.migrator.scheduler.component;

import io.playce.migrator.constant.TaskType;
import io.playce.migrator.scheduler.task.ITask;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.Map;


@Component
@RequiredArgsConstructor
@Slf4j
public class TaskRunnerComponent {
    private final Map<String, ITask> taskMap;

    public void runTaskService(TaskType taskType) {
        String serviceName = taskType.getServiceName();
        ITask task = taskMap.get(serviceName);
        if(task == null) {
            log.error("service not found. name: {}", serviceName);
            return;
        }

        task.execute();
    }
}