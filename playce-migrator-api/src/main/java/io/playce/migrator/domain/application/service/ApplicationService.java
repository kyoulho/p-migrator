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
 * Jaeeon Bae       8월 18, 2022            First Draft.
 */
package io.playce.migrator.domain.application.service;

import io.playce.migrator.analysis.process.service.AnalysisHistoryService;
import io.playce.migrator.constant.ErrorCode;
import io.playce.migrator.constant.JobStatus;
import io.playce.migrator.constant.TaskType;
import io.playce.migrator.dao.entity.Application;
import io.playce.migrator.dao.mapper.ApplicationMapper;
import io.playce.migrator.dao.repository.ApplicationRepository;
import io.playce.migrator.dto.analysishistory.LastAnalysisHistory;
import io.playce.migrator.dto.application.ApplicationRequest;
import io.playce.migrator.dto.application.ApplicationResponse;
import io.playce.migrator.dto.migrationhistory.LastMigrationHistory;
import io.playce.migrator.exception.PlayceMigratorException;
import io.playce.migrator.migration.process.service.MigrationHistoryService;
import io.playce.migrator.util.LoginUserUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ApplicationService {
    private final ApplicationRepository applicationRepository;
    private final ApplicationMapper applicationMapper;
    private final ModelMapper modelMapper;
    private final LoginUserUtil loginUserUtil;
    private final AnalysisHistoryService analysisHistoryService;
    private final MigrationHistoryService migrationHistoryService;

    public List<ApplicationResponse> getApplicationList(Long projectId) {
        return applicationMapper.selectApplications(projectId)
                .stream().map(this::checkScmYn).collect(Collectors.toList());
    }

    public ApplicationResponse getApplicationDetail(Long projectId, Long applicationId) {
        ApplicationResponse response = applicationMapper.selectApplication(projectId, applicationId)
                .orElseThrow(() -> new PlayceMigratorException(ErrorCode.PM201R, "application ID : " + applicationId + " not exist."));
        return checkScmYn(response);
    }

    public Application getDefaultApplication(Long projectId, ApplicationRequest applicationRequest, Long applicationId) {
        Application application = modelMapper.map(applicationRequest, Application.class);
        if (applicationId != null) {
            application.setApplicationId(applicationId);
        }
        application.setProjectId(projectId);
        application.setRegistLoginId(loginUserUtil.getLoginId());
        application.setRegistDatetime(Instant.now());
        application.setModifyLoginId(loginUserUtil.getLoginId());
        application.setModifyDatetime(Instant.now());
        return application;
    }

    public void removeApplication(Long applicationId) {
        checkProgressingJob(applicationId, TaskType.Analysis.name());
        checkProgressingJob(applicationId, TaskType.Migration.name());
        applicationRepository.deleteById(getEntity(applicationId).getApplicationId());
    }

    public Application getEntity(Long applicationId) {
        return applicationRepository.findById(applicationId).orElseThrow(
                () -> new PlayceMigratorException(ErrorCode.PM201R, "application ID : " + applicationId + " Not Found."));
    }

    public void checkProgressingJob(Long applicationId, String type) {
        boolean isProgressing = false;
        String status = null;

        if (TaskType.Analysis.name().equals(type)) {
            LastAnalysisHistory lastAnalysisHistory = analysisHistoryService.getLastHistory(applicationId);
            if (lastAnalysisHistory != null) {
                status = lastAnalysisHistory.getJobStatus();

                isProgressing = JobStatus.REQ.name().equals(status)
                        || JobStatus.PEND.name().equals(status)
                        || JobStatus.PROC.name().equals(status);
            }
        }

        if (TaskType.Migration.name().equals(type)) {
            LastMigrationHistory lastMigrationHistory = migrationHistoryService.getLastHistory(applicationId);
            if (lastMigrationHistory != null) {
                status = lastMigrationHistory.getJobStatus();

                isProgressing = JobStatus.REQ.name().equals(status)
                        || JobStatus.PEND.name().equals(status)
                        || JobStatus.PROC.name().equals(status);
            }
        }

        if (isProgressing) {
            throw new PlayceMigratorException(ErrorCode.PM202R,
                    "This Job is already in progress. Please check the application id : " + applicationId + ", type : " + type + ", jobStatus : " + status);
        }
    }

    private ApplicationResponse checkScmYn(ApplicationResponse response) {
        if (response.isScmYn()) {
            response.setScmUrl(response.getOriginPath());
            response.setBranchName(response.getOriginFileName());
        }
        return response;
    }

    public Integer getAllApplicationCount() {
        return Math.toIntExact(applicationRepository.count());
    }
}