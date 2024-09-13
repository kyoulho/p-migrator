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
 * Dong-Heon Han    Aug 25, 2022		First Draft.
 */

package io.playce.migrator.common.job.service;

import io.playce.migrator.config.PlayceMigratorConfig;
import io.playce.migrator.dao.entity.Application;
import io.playce.migrator.dao.repository.ApplicationRepository;
import io.playce.migrator.exception.PlayceMigratorException;
import io.playce.migrator.scheduler.task.ITask;
import io.playce.migrator.util.FileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.IOException;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class ScheduleGarbegeRemoveService implements ITask {
    private final ApplicationRepository applicationRepository;
    private final PlayceMigratorConfig playceMigratorConfig;

    @Transactional
    @Override
    public void execute() {
        String originDir = playceMigratorConfig.getOriginDir();
        removeFiles(originDir);
        String workDir = playceMigratorConfig.getWorkDir();
        removeFiles(workDir);
    }

    private void removeFiles(String dir) {
        File file = new File(dir);
        if(!file.exists()) {
            try {
                FileUtils.forceMkdir(file);
                log.info("create directory. dir: {}", dir);
            } catch (IOException e) {
                log.error("create directory failed. dir: {}", dir, e);
                return;
            }
        }
        File[] subs = file.listFiles();

        assert subs != null;
        for(File sub: subs) {
            String filePath = sub.getAbsolutePath();
            if(sub.isFile()) {
                log.error("check the file - {}", filePath);
                continue;
            }

            Long applicationId = Long.parseLong(sub.getName());
            Optional<Application> app = applicationRepository.findById(applicationId);
            if(app.isEmpty()) {
                removeFile(sub, filePath);
            }
        }
    }

    private static void removeFile(File sub, String filePath) {
        try {
            boolean removed = FileUtil.removeFile(sub);
            if(removed) {
                log.info("The garbage file has been deleted. - {}", filePath);
            } else {
                log.error("The file was not deleted. - {}", filePath);
            }
        } catch (PlayceMigratorException e) {
            log.error("{} - {}", e.getErrorCode(), e.getMessage());
        }
    }
}