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
 * Jaeeon Bae       10월 05, 2022            First Draft.
 */
package io.playce.migrator.domain.application.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.playce.migrator.config.PlayceMigratorConfig;
import io.playce.migrator.constant.ApplicationType;
import io.playce.migrator.constant.ErrorCode;
import io.playce.migrator.dao.entity.Application;
import io.playce.migrator.dao.entity.ApplicationOverview;
import io.playce.migrator.dao.repository.ApplicationOverviewRepository;
import io.playce.migrator.dao.repository.ApplicationRepository;
import io.playce.migrator.domain.application.component.upload.FileValidatorManager;
import io.playce.migrator.domain.common.service.SubscriptionService;
import io.playce.migrator.dto.application.ApplicationRequest;
import io.playce.migrator.exception.PlayceMigratorException;
import io.playce.migrator.util.FileUtil;
import io.playce.migrator.util.LoginUserUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.nio.file.Path;
import java.time.Instant;
import java.util.List;

import static io.playce.migrator.constant.CommonConstants.FILE_SIZE;
import static io.playce.migrator.util.ObjectChecker.requireNonNull;
import static io.playce.migrator.util.ObjectChecker.requireTrue;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class UploadApplicationService {

    private final PlayceMigratorConfig playceMigratorConfig;
    private final ApplicationService applicationService;
    private final ApplicationRepository applicationRepository;
    private final ModelMapper modelMapper;
    private final LoginUserUtil loginUserUtil;
    private final ApplicationOverviewRepository applicationOverviewRepository;
    private final ObjectMapper objectMapper;
    private final FileValidatorManager fileValidatorManager;
    private final SubscriptionService subscriptionService;

    public Long createApplication(Long projectId, ApplicationRequest applicationRequest, MultipartFile applicationFile, Long applicationId) throws Exception {
        requireTrue(subscriptionService.checkSubscription(),new PlayceMigratorException(ErrorCode.PM905SUB));
        requireNonNull(applicationFile, new PlayceMigratorException(ErrorCode.PM107H, "MultipartFile does not exist"));
        validateFile(applicationFile);

        // temp 파일로 저장
        Path tempFile = FileUtil
                .saveFile(applicationFile, applicationFile.getOriginalFilename(), Path.of(FileUtils.getTempDirectoryPath() + playceMigratorConfig.getOriginDir()));
        if (!fileValidatorManager.validate(tempFile.toFile())) {
            throw new PlayceMigratorException(ErrorCode.PM377F);
        }

        Application application = applicationService.getDefaultApplication(projectId, applicationRequest, applicationId);
        application.setOriginPath(StringUtils.EMPTY);
        application.setOriginFileName(StringUtils.EMPTY);
        application.setApplicationType(StringUtils.EMPTY);
        application = applicationRepository.save(application);

        return saveAnalyzeFile(application, tempFile.toFile());
    }

    private Long saveAnalyzeFile(Application application, File tempFile) throws Exception {
        Application originApplication = applicationRepository.findById(application.getApplicationId())
                .orElseThrow(() -> new PlayceMigratorException(ErrorCode.PM201R, "application ID : " + application.getApplicationId() + " Not Found."));

        Path originPath = playceMigratorConfig.getOriginDirWithApp(originApplication.getApplicationId());
        File newFile = new File(originPath + File.separator + tempFile.getName());
        String analyzeFileName = tempFile.getName();
        String extension = FilenameUtils.getExtension(analyzeFileName);

        if (ApplicationType.isSupported(extension)) {
            FileUtils.copyFile(tempFile, newFile);
            originApplication.setApplicationName(FilenameUtils.getBaseName(analyzeFileName));
            originApplication.setOriginFileName(analyzeFileName);
            originApplication.setOriginPath(originPath.toString());
            originApplication.setOriginFileSize(tempFile.length());
            originApplication.setApplicationType(extension.toUpperCase());

            saveApplicationOverview(originApplication, newFile);
        }
        return originApplication.getApplicationId();
    }

    public Long modifyApplication(Long applicationId, ApplicationRequest applicationRequest, MultipartFile applicationFile) {
        Application application = applicationService.getEntity(applicationId);

        // origin file 정보
        String originPath = application.getOriginPath();
        String originFileName = application.getOriginFileName();
        Long originFileSize = application.getOriginFileSize();

        modelMapper.map(applicationRequest, application);
        application.setModifyLoginId(loginUserUtil.getLoginId());
        application.setModifyDatetime(Instant.now());

        // multipart file 이 없는 경우 기존 데이터를 재설정 해준다.
        if (applicationFile == null) {
            application.setOriginPath(originPath);
            application.setOriginFileName(originFileName);
            application.setOriginFileSize(originFileSize);
        }

        return applicationRepository.save(application).getApplicationId();
    }

    private void saveApplicationOverview(Application application, File applicationFile) {
        Long applicationId = application.getApplicationId();
        List<String> libraries = FileUtil.getLibraryNamesFromZipfile(applicationFile);
        ApplicationOverview overview = applicationOverviewRepository.findById(applicationId)
                .orElse(createNewApplicationOverview(applicationId));
        try {
            overview.setLibraryNames(objectMapper.writeValueAsString(libraries));
            overview.setPrepareProcessYn(false);
        } catch (JsonProcessingException e) {
            throw new PlayceMigratorException(ErrorCode.PM304F, e.getMessage(), e);
        }
        applicationOverviewRepository.save(overview);
    }

    private ApplicationOverview createNewApplicationOverview(Long applicationId) {
        ApplicationOverview overview = new ApplicationOverview();
        overview.setApplicationId(applicationId);
        return overview;
    }

    private void validateFile(MultipartFile analyzeFile) {
        String baseName = FilenameUtils.getBaseName(analyzeFile.getOriginalFilename());
        if (baseName.length() > 100) {
            throw new PlayceMigratorException(ErrorCode.PM378F);
        }

        if (analyzeFile.getSize() > FILE_SIZE) {
            throw new PlayceMigratorException(ErrorCode.PM375F);
        }
    }
}