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

package io.playce.migrator.dto.progress;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.playce.migrator.constant.JobStatus;
import io.playce.migrator.constant.JobStep;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter @Setter @Builder @ToString(includeFieldNames = false, exclude = "lock")
public class JobProgress {
    @JsonIgnore
    private final Object lock = new Object();
    private Long projectId;
    private Long applicationId;
    private Long historyId;
    private String originFileName;
    private String currentStep;
    private String detailMessage;
    @JsonIgnore
    private long now;
    private long current;
    private long total;
    private JobStatus status;
    private String errorMessage;

    public void setTotal(long total) {
        this.total = total;
        this.now = total;
    }
    public void decrease(JobStep jobStep, String message, JobStatus status, String error) {
        synchronized (lock) {
            setCurrentStep(jobStep.getDisplay());
            setDetailMessage(message);
            setStatus(status);
            setErrorMessage(error);
            if(this.status == JobStatus.CMPL || this.status == JobStatus.FAIL || this.status == JobStatus.CNCL) {
                now = 0L;
            } else {
                if(now < 1) {
                    now = 0L;
                } else {
                    now -= 1;
                }
            }
            current = total - now;
        }
    }
}