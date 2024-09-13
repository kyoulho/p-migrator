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
import io.playce.migrator.analysis.process.async.DependenciesInfoAnalyzer;
import io.playce.migrator.analysis.process.component.DeletedApiProvider;
import io.playce.migrator.analysis.process.component.DeprecatedApiProvider;
import io.playce.migrator.constant.ErrorCode;
import io.playce.migrator.dao.entity.ApplicationOverview;
import io.playce.migrator.dao.repository.ApplicationOverviewRepository;
import io.playce.migrator.dao.repository.DependenciesInfoRepository;
import io.playce.migrator.dto.analysis.AnalysisTaskItem;
import io.playce.migrator.exception.PlayceMigratorException;
import io.playce.migrator.prepare.service.ApplicationPrepareService;
import io.playce.migrator.util.JacksonObjectMapperUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.nio.file.Path;
import java.util.List;

@Service
@RequiredArgsConstructor
public class ApplicationOverviewService {
    private final ApplicationOverviewRepository applicationOverviewRepository;
    private final DependenciesInfoRepository dependenciesInfoRepository;
    private final ApplicationPrepareService applicationPrepareService;
    private final DependenciesInfoAnalyzer dependenciesInfoAnalyzer;
    private final ObjectMapper objectMapper;
    private final DeprecatedApiProvider deprecatedApiProvider;
    private final DeletedApiProvider deletedApiProvider;

    @Transactional
    public void prepare(AnalysisTaskItem item) {
        Long applicationId = item.getApplicationId();
        boolean prepareApplication = applicationPrepareService.prepareApplication(item);

        if (!prepareApplication) {
            throw new PlayceMigratorException(ErrorCode.PM271R, "Check the application data. applicationId: " + applicationId);
        }

        // dependencies 정보 생성
        updateDependenciesInfo(applicationId, item.getOriginFileName());
        // deprecated 정보 생성
        Path targetPath = applicationPrepareService.getTargetPath(applicationId, item.getOriginFileName());
        deprecatedApiProvider.deprscan(item.getJavaVersion(), new File(targetPath.toString()), applicationId);
        // deleted 정보 생성
        deletedApiProvider.deps(new File(targetPath.toString()), applicationId);
    }

    private void updateDependenciesInfo(Long applicationId, String originFileName) {
        dependenciesInfoRepository.deleteByApplicationId(applicationId);
        getLibraries(applicationId)
                .forEach(library -> dependenciesInfoAnalyzer.analyzeDependencies(applicationId, originFileName, library));
    }

    public String getLibraryNames(Long applicationId) {
        ApplicationOverview applicationOverview = getApplicationOverview(applicationId);
        return applicationOverview.getLibraryNames();
    }

    public ApplicationOverview getApplicationOverview(Long applicationId) {
        return applicationOverviewRepository.findById(applicationId).orElseThrow(() ->
                new PlayceMigratorException(ErrorCode.PM201R, "Application overview information does not exist. applicationId: " + applicationId)
        );
    }

    public List<String> getLibraries(Long applicationId) {
        String libraryNames = getLibraryNames(applicationId);
        return JacksonObjectMapperUtil.getObject(objectMapper, libraryNames);
    }

    @Transactional
    public void update(ApplicationOverview applicationOverview) {
        applicationOverviewRepository.save(applicationOverview);
    }
}