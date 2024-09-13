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

import com.fasterxml.jackson.databind.ObjectMapper;
import io.playce.migrator.constant.ErrorCode;
import io.playce.migrator.constant.JobStatus;
import io.playce.migrator.dao.entity.AnalysisHistory;
import io.playce.migrator.dao.mapper.AnalysisHistoryMapper;
import io.playce.migrator.dao.repository.AnalysisHistoryRepository;
import io.playce.migrator.dto.analysis.AnalysisTaskItem;
import io.playce.migrator.dto.analysishistory.AnalysisHistoryResponse;
import io.playce.migrator.dto.analysishistory.AppliedAnalysisRuleResponse;
import io.playce.migrator.dto.analysishistory.LastAnalysisHistory;
import io.playce.migrator.dto.analysishistory.SuccessAnalysisHistory;
import io.playce.migrator.exception.PlayceMigratorException;
import io.playce.migrator.util.JacksonObjectMapperUtil;
import io.playce.migrator.util.LoginUserUtil;
import lombok.RequiredArgsConstructor;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class AnalysisHistoryService {
    private final ModelMapper modelMapper;
    private final ObjectMapper objectMapper;
    private final AnalysisHistoryRepository analysisHistoryRepository;
    private final AnalysisHistoryMapper analysisHistoryMapper;
    private final LoginUserUtil loginUserUtil;

    public Long registHistory(Long applicationId, String selectedLibraryNames) {
        AnalysisHistory analysisHistory = new AnalysisHistory();
        analysisHistory.setJobStatus(JobStatus.REQ.name());
        analysisHistory.setSelectedLibraryNames(selectedLibraryNames);
        analysisHistory.setApplicationId(applicationId);
        analysisHistory.setRegistLoginId(loginUserUtil.getLoginId());
        analysisHistory.setRegistDatetime(Instant.now());
        return analysisHistoryRepository.save(analysisHistory).getAnalysisHistoryId();
    }

    public LastAnalysisHistory getLastHistory(Long applicationId) {
        AnalysisHistory analysisHistory = analysisHistoryRepository.findFirstByApplicationIdOrderByEndDatetimeDesc(applicationId)
                .orElse(null);
        if (analysisHistory == null) return null;
        return modelMapper.map(analysisHistory, LastAnalysisHistory.class);
    }

    public SuccessAnalysisHistory getSuccessHistory(Long applicationId) {
        AnalysisHistory analysisHistory = analysisHistoryRepository.findFirstByApplicationIdAndJobStatusOrderByEndDatetimeDesc(applicationId, JobStatus.CMPL.name())
                .orElse(null);
        if (analysisHistory == null) return null;
        return modelMapper.map(analysisHistory, SuccessAnalysisHistory.class);
    }

    public List<AnalysisHistoryResponse> getAnalysisHistories(Long applicationId) {
       return analysisHistoryMapper.selectAnalysisHistories(applicationId);
    }

    public void updateJobToFail(AnalysisTaskItem item, String errorMessage, Instant now) {
        AnalysisHistory analysisHistory = getAnalysisHistory(item.getAnalysisHistoryId());
        analysisHistory.setJobStatus(JobStatus.FAIL.name());
        analysisHistory.setErrorMessage(errorMessage);
        analysisHistory.setEndDatetime(now);
    }

    public void updateJobToProcess(AnalysisTaskItem item, Instant now) {
        AnalysisHistory analysisHistory = getAnalysisHistory(item.getAnalysisHistoryId());
        analysisHistory.setJobStatus(JobStatus.PROC.name());
        analysisHistory.setStartDatetime(now);
    }


    public JobStatus getJobStatus(AnalysisTaskItem item) {
        AnalysisHistory analysisHistory = getAnalysisHistory(item.getAnalysisHistoryId());
        return JobStatus.valueOf(analysisHistory.getJobStatus());
    }

    public List<AnalysisTaskItem> getRequestedAnalysisHistory() {
        return analysisHistoryMapper.selectAnalysisRequestItems();
    }

    public void handlingInProgressItemFailures() {
        analysisHistoryMapper.updateToStatusByFromStatus(JobStatus.PROC.name(), JobStatus.FAIL.name());
    }

    public void setReqStatusToPend(Long analysisHistoryId) {
        setToStatusByFromStatusAndId(JobStatus.REQ, JobStatus.PEND, analysisHistoryId);
    }

    private void setToStatusByFromStatusAndId(JobStatus from, JobStatus to, Long id) {
        AnalysisHistory history = analysisHistoryRepository.findByAnalysisHistoryIdAndJobStatus(id, from.name()).orElseThrow(() ->
                new PlayceMigratorException(ErrorCode.PM201R, "analysis history not found, id: " + id));
        history.setJobStatus(to.name());
    }

    private AnalysisHistory getAnalysisHistory(Long analysisHistoryId) {
        return analysisHistoryRepository.findById(analysisHistoryId).orElseThrow(() ->
                new PlayceMigratorException(ErrorCode.PM201R, "Analysis history does not exist. id: " + analysisHistoryId));
    }


    public void updateStartDatetime(AnalysisTaskItem item, Instant now) {
        AnalysisHistory analysisHistory = getAnalysisHistory(item.getAnalysisHistoryId());
        analysisHistory.setStartDatetime(now);
    }

    public void updateJobToComplete(AnalysisTaskItem item, Instant now) {
        AnalysisHistory analysisHistory = getAnalysisHistory(item.getAnalysisHistoryId());
        analysisHistory.setJobStatus(JobStatus.CMPL.name());
        analysisHistory.setErrorMessage(null);
        analysisHistory.setEndDatetime(now);
    }

    public List<AppliedAnalysisRuleResponse> getAppliedAnalysisRules(Long analysisHistoryId) {
        getAnalysisHistory(analysisHistoryId);
        return analysisHistoryMapper.selectAppliedAnalysisRuleList(analysisHistoryId);
    }

    public List<String> getSelectedLibraries(Long analysisHistoryId) {
        AnalysisHistory analysisHistory = getAnalysisHistory(analysisHistoryId);
        String libraryNames = analysisHistory.getSelectedLibraryNames();
        return JacksonObjectMapperUtil.getObject(objectMapper, libraryNames);
    }

    public void checkAnalysisSuccessExist(Long applicationId) {
        SuccessAnalysisHistory successAnalysisHistory = getSuccessHistory(applicationId);
        if (successAnalysisHistory == null) {
            throw new PlayceMigratorException(ErrorCode.PM202R,
                    "Analysis Success job does not exist. Please check the application Id : " + applicationId);
        }
    }

    public void cancelAnalysis(Long analysisHistoryId) {
        AnalysisHistory analysisHistory = getAnalysisHistory(analysisHistoryId);
        if (JobStatus.CMPL.name().equals(analysisHistory.getJobStatus()) ||
                JobStatus.FAIL.name().equals(analysisHistory.getJobStatus()) ||
                JobStatus.CNCL.name().equals(analysisHistory.getJobStatus())) {
            throw new PlayceMigratorException(ErrorCode.PM202R, "analysis history status '" + analysisHistory.getJobStatus() + "' can not be cancel.");
        }
        analysisHistory.setJobStatus(JobStatus.CNCL.name());
        analysisHistory.setEndDatetime(Instant.now());
    }

    public void updateJobToCancel(AnalysisTaskItem item, Instant now) {
        AnalysisHistory analysisHistory = getAnalysisHistory(item.getAnalysisHistoryId());
        analysisHistory.setEndDatetime(now);
    }
}