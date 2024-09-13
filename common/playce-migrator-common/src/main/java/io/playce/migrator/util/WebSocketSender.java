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
 * Dong-Heon Han    Oct 13, 2022		First Draft.
 */

package io.playce.migrator.util;

import io.playce.migrator.component.IWebsocketObserver;
import io.playce.migrator.constant.JobStatus;
import io.playce.migrator.constant.JobStep;
import io.playce.migrator.dto.progress.JobProgress;

public class WebSocketSender {
    public static void sendMessage(IWebsocketObserver observer, JobProgress progress, JobStep jobStep, JobStatus status, String message, String error) {
        progress.decrease(jobStep, message, status, error);
        observer.sendMessage(progress);
    }

    public static void sendMessage(IWebsocketObserver observer, JobProgress progress, JobStep jobStep, JobStatus status, String message) {
        progress.decrease(jobStep, message, status, null);
        observer.sendMessage(progress);
    }
}