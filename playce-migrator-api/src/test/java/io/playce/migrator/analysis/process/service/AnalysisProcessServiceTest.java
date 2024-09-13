package io.playce.migrator.analysis.process.service;

import io.playce.migrator.constant.ErrorCode;
import io.playce.migrator.dao.entity.AnalysisHistoryDetail;
import io.playce.migrator.dao.entity.ApplicationOverview;
import io.playce.migrator.dao.repository.AnalysisHistoryDetailRepository;
import io.playce.migrator.dao.repository.ApplicationOverviewRepository;
import io.playce.migrator.dto.analysis.AnalysisTaskItem;
import io.playce.migrator.exception.PlayceMigratorException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
 * Dong-Heon Han    Aug 18, 2022		First Draft.
 */

@SpringBootTest
@Slf4j
class AnalysisProcessServiceTest {
    @Autowired
    private AnalysisProcessService analysisProcessService;
    @Autowired
    private AnalysisHistoryDetailRepository analysisHistoryDetailRepository;
    @Autowired
    private ApplicationOverviewRepository applicationOverviewRepository;

    private void detailLogging(List<AnalysisHistoryDetail> details) {
        details.forEach(d -> log.debug("{}", d));
    }

    private void initData(AnalysisTaskItem item) {
        // prepare N 설정.
        Optional<ApplicationOverview> byId = applicationOverviewRepository.findById(item.getApplicationId());
        byId.ifPresent(applicationOverview -> {
            applicationOverview.setPrepareProcessYn(false);
            applicationOverviewRepository.save(applicationOverview);
        });
    }

    @Test
    void testErrorCodePM201() {
        AnalysisTaskItem app = new AnalysisTaskItem();
        app.setApplicationId(0L);
        app.setAnalysisHistoryId(0L);
        PlayceMigratorException e = assertThrows(PlayceMigratorException.class, () -> analysisProcessService.process(app));
        assertEquals(e.getErrorCode(), ErrorCode.PM201R);
    }
    @Test
    void testProcess() {
        AnalysisTaskItem item = getAnalysisTaskItem("sample.zip");
        initData(item);

        analysisProcessService.process(item);

        List<AnalysisHistoryDetail> details = analysisHistoryDetailRepository.findAll();
        detailLogging(details);

        details.forEach(d -> {
            if(d.getAnalysisRuleId() == 1L) {
                log.debug("{}", d);
            }
        });
    }

    private static AnalysisTaskItem getAnalysisTaskItem(String originFileName) {
        AnalysisTaskItem item = new AnalysisTaskItem();
        item.setOriginPath("test-files/zip");
        item.setOriginFileName(originFileName);
        item.setApplicationType("ZIP");
        item.setApplicationId(1L);
        item.setAnalysisHistoryId(1L);
        item.setSelectedLibraryNames("[]");
        item.setTargetId(1L);
        return item;
    }

    @Test
    void javaDependencyCheck() {
        AnalysisTaskItem item = getAnalysisTaskItem("dependency-check.zip");
        initData(item);

        analysisProcessService.process(item);

        List<AnalysisHistoryDetail> details = analysisHistoryDetailRepository.findAll();
//        assertEquals(52, details.size());
        detailLogging(details);
    }

    @Test
    void msalos_master_20211123() {
        AnalysisTaskItem item = getAnalysisTaskItem("msalos-master.20211123.zip");
        initData(item);

        analysisProcessService.process(item);

        List<AnalysisHistoryDetail> details = analysisHistoryDetailRepository.findAll();
//        assertEquals(52, details.size());
        detailLogging(details);
    }
}