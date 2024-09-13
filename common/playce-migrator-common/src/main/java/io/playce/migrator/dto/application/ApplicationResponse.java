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
 * Author            Date                Description
 * ---------------  ----------------    ------------
 * Jaeeon Bae       8월 23, 2022            First Draft.
 */
package io.playce.migrator.dto.application;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.playce.migrator.dto.analysishistory.LastAnalysisHistory;
import io.playce.migrator.dto.analysishistory.SuccessAnalysisHistory;
import io.playce.migrator.dto.migrationhistory.LastMigrationHistory;
import io.playce.migrator.dto.migrationhistory.SuccessMigrationHistory;
import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
@JsonIgnoreProperties(value = {"handler"})
public class ApplicationResponse {

    // application
    private Long projectId;
    private Long applicationId;
    private boolean scmYn;
    private Boolean projectTargetUseYn;
    private String originPath;
    private String originFileName;
    private String scmUrl;
    private String branchName;
    private String originFileSize;
    private String applicationType;
    private String description;

    // credential
    private Long credentialId;

    // source
    private String middlewareType;
    private String javaVersion;

    // target
    private Long targetId;

    // analysis
    private LastAnalysisHistory lastAnalysis;
    private SuccessAnalysisHistory successAnalysisHistory;

    // migration
    private LastMigrationHistory lastMigration;
    private SuccessMigrationHistory successMigrationHistory;

    // common
    private String registLoginId;
    private Instant registDatetime;
    private String modifyLoginId;
    private Instant modifyDatetime;
}