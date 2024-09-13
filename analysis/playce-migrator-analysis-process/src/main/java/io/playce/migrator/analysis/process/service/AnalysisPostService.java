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
 * Dong-Heon Han    Aug 19, 2022		First Draft.
 */

package io.playce.migrator.analysis.process.service;

import io.playce.migrator.dao.entity.AnalysisHistoryDetail;
import io.playce.migrator.dao.repository.AnalysisHistoryDetailRepository;
import io.playce.migrator.dto.analysis.AnalysisRuleResult;
import io.playce.migrator.dto.analysis.AnalysisTaskItem;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class AnalysisPostService {
    private final AnalysisHistoryDetailRepository analysisHistoryDetailRepository;

    @Transactional
    public void saveAnalysisHistoryDetails(AnalysisTaskItem app, List<AnalysisRuleResult> analysisRuleResults) {
        List<AnalysisHistoryDetail> details = analysisRuleResults.stream().map(result -> {
            AnalysisHistoryDetail analysisHistoryDetail = makeAnalysisHistoryDetail(app, result);
            log.trace("save item: {}", analysisHistoryDetail);
            return analysisHistoryDetail;
        }).collect(Collectors.toList());
        analysisHistoryDetailRepository.saveAll(details);
    }

//    @Transactional
//    public void saveAnalysisHistoryDetail(AnalysisTaskItem app, AnalysisRuleResult analysisRuleResult) {
//        AnalysisHistoryDetail analysisHistoryDetail = makeAnalysisHistoryDetail(app, analysisRuleResult);
//        analysisHistoryDetailRepository.save(analysisHistoryDetail);
//    }

    private AnalysisHistoryDetail makeAnalysisHistoryDetail(AnalysisTaskItem app, AnalysisRuleResult result) {
        AnalysisHistoryDetail detail = new AnalysisHistoryDetail();
        detail.setAnalysisHistoryId(app.getAnalysisHistoryId());
        detail.setAnalysisLineNumber(result.getLineNumber());
        detail.setAnalysisOriginFilePath(result.getAnalysisOriginFilePath());
        detail.setAnalysisFilePath(result.getAnalysisFilePath());
        detail.setAnalysisFileName(result.getAnalysisFileName());
        detail.setDetectedString(result.getDetectedString());
        detail.setOverrideComment(result.getOverrideComment());
        detail.setAnalysisRuleId(result.getAnalysisRuleId());
        return detail;
    }

    @Transactional
    public void removeAnalysisHistoryDetails(AnalysisTaskItem app) {
        analysisHistoryDetailRepository.deleteByAnalysisHistoryId(app.getAnalysisHistoryId());
    }
}