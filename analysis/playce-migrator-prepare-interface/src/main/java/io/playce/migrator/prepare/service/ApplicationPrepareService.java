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
 * Dong-Heon Han    Aug 11, 2022		First Draft.
 */

package io.playce.migrator.prepare.service;

import io.playce.migrator.config.PlayceMigratorConfig;
import io.playce.migrator.constant.ApplicationType;
import io.playce.migrator.constant.ScmType;
import io.playce.migrator.dto.analysis.AnalysisTaskItem;
import io.playce.migrator.exception.PlayceMigratorException;
import io.playce.migrator.prepare.AbstractPrepareService;
import io.playce.migrator.prepare.IPrepareService;
import io.playce.migrator.prepare.IScmPrepareService;
import io.playce.migrator.util.GeneralCipherUtil;
import io.playce.migrator.util.StringNullChecker;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.file.Path;
import java.util.Map;

@Service
@RequiredArgsConstructor
@Slf4j
public class ApplicationPrepareService {
    private final Map<String, IPrepareService> prepareServiceMap;
    private final Map<String, IScmPrepareService> scmPrepareServiceMap;
    private final PlayceMigratorConfig playceMigratorConfig;


    public boolean prepareApplication(AnalysisTaskItem app) {
        Long applicationId = app.getApplicationId();
        String originPath = app.getOriginPath();
        String originFileName = app.getOriginFileName();
        String appType = app.getApplicationType();
        Path targetDir = getTargetPath(applicationId, originFileName);

        log.debug("Run applicationId: {}, path: {}", applicationId, originPath);
        if (StringNullChecker.isEmpty(originPath)) {
            log.error("Fail applicationId: {} - The file path is empty.", applicationId);
            return false;
        }

        if (appType.equals(ApplicationType.SCM.name())) {
            downloadSourceFile(app, originPath, originFileName, targetDir);
            return true;
        }

        Path sourceFile = Path.of(originPath, originFileName);
        if (!prepareServiceMap.containsKey(appType)) {
            log.error("Fail applicationId: {} - Unsupported file type. path: {}", applicationId, sourceFile);
            return false;
        }

        if (!sourceFile.toFile().exists()) {
            log.error("Fail applicationId: {} - The file does not exist. path: {}", applicationId, sourceFile);
            return false;
        }

        IPrepareService prepareService = prepareServiceMap.get(appType);
        try {
            prepareService.execute(sourceFile, targetDir, appType);
        } catch (PlayceMigratorException e) {
            log.error("Fail applicationId: {} - {}", applicationId, e.getMessage());
            return false;
        }

        log.debug("Done applicationId: {}", applicationId);

        return true;
    }

    private void downloadSourceFile(AnalysisTaskItem app, String originPath, String originFileName, Path targetDir) {
        ScmType scmType = ScmType.getScmType(originPath);
        IScmPrepareService ScmPrepareService = scmPrepareServiceMap.get(scmType.name());
        Path targetUnzipDir = AbstractPrepareService.getTargetUnzipDir(targetDir);
        log.info("Start downloading source file path:{} branch:{}", originPath, originFileName);
        if (app.getCredentialPasswordYn()) {
            ScmPrepareService.cloneRepository(originPath, originFileName, app.getCredentialUsername(), GeneralCipherUtil.decrypt(app.getCredentialPassword()), targetUnzipDir);
        } else {
            ScmPrepareService.cloneRepository(originPath, originFileName, app.getCredentialKeyContent(), targetUnzipDir);
        }
        log.info("Completed downloading source file");
    }

    public Path getTargetPath(Long applicationId, String originFileName) {
        return Path.of(playceMigratorConfig.getWorkDir(), String.valueOf(applicationId), originFileName);
    }
}