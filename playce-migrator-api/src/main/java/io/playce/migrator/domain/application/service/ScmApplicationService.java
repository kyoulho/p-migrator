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

import io.playce.migrator.constant.ApplicationType;
import io.playce.migrator.constant.ErrorCode;
import io.playce.migrator.constant.ScmType;
import io.playce.migrator.dao.entity.Application;
import io.playce.migrator.dao.entity.ApplicationOverview;
import io.playce.migrator.dao.repository.ApplicationOverviewRepository;
import io.playce.migrator.dao.repository.ApplicationRepository;
import io.playce.migrator.domain.common.service.SubscriptionService;
import io.playce.migrator.domain.credential.service.CredentialService;
import io.playce.migrator.dto.application.ApplicationRequest;
import io.playce.migrator.dto.credential.CredentialResponse;
import io.playce.migrator.exception.PlayceMigratorException;
import io.playce.migrator.prepare.IScmCredentialService;
import io.playce.migrator.util.GeneralCipherUtil;
import io.playce.migrator.util.LoginUserUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.Map;

import static io.playce.migrator.util.ObjectChecker.requireTrue;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class ScmApplicationService {

    private final ApplicationService applicationService;
    private final ApplicationOverviewRepository applicationOverviewRepository;
    private final ApplicationRepository applicationRepository;
    private final ModelMapper modelMapper;
    private final LoginUserUtil loginUserUtil;
    private final CredentialService credentialService;
    private final Map<String, IScmCredentialService> credentialServiceMap;
    private final SubscriptionService subscriptionService;

    public Long createApplication(Long projectId, ApplicationRequest request) {
        requireTrue(subscriptionService.checkSubscription(),new PlayceMigratorException(ErrorCode.PM905SUB));
        // SCM 인증
        validateCredential(request);

        // App Record
        Application application = applicationService.getDefaultApplication(projectId, request, null);
        application.setApplicationName(request.getBranchName());
        application.setOriginPath(request.getScmUrl());
        application.setOriginFileName(request.getBranchName());
        application.setApplicationType(ApplicationType.SCM.name());
        Long applicationId = applicationRepository.save(application).getApplicationId();
        // 오버뷰 저장
        saveOverview(applicationId);
        return applicationId;
    }

    private void saveOverview(Long applicationId) {
        ApplicationOverview overview = new ApplicationOverview();
        overview.setApplicationId(applicationId);
        overview.setPrepareProcessYn(false);
        overview.setLibraryNames("[]");
        applicationOverviewRepository.save(overview);
    }

    public Long modifyApplication(Long applicationId, ApplicationRequest request) {
        // SCM 인증
        validateCredential(request);

        Application application = applicationService.getEntity(applicationId);
        modelMapper.map(request, application);
        application.setApplicationName(request.getBranchName());
        application.setOriginPath(request.getScmUrl());
        application.setOriginFileName(request.getBranchName());
        application.setModifyLoginId(loginUserUtil.getLoginId());
        application.setModifyDatetime(Instant.now());
        return applicationId;
    }

    private String validateCredential(ApplicationRequest request) {
        CredentialResponse credential = credentialService.getCredential(request.getCredentialId());
        String scmUrl = request.getScmUrl();
        ScmType scmType = ScmType.getScmType(scmUrl);
        IScmCredentialService scmCredentialService = credentialServiceMap.get(scmType.name());

        if (credential.getPasswordYn()) {
            requireTrue(scmUrl.startsWith("https://"), new PlayceMigratorException(ErrorCode.PM804S));
            return scmCredentialService.validateCredential(scmUrl, credential.getUsername(), GeneralCipherUtil.decrypt(credential.getPassword()));
        } else {
            requireTrue(scmUrl.startsWith("git@"), new PlayceMigratorException(ErrorCode.PM804S));
            return scmCredentialService.validateCredential(scmUrl, credential.getKeyContent());
        }
    }
}