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

import io.playce.migrator.analysis.process.component.ConversionComponent;
import io.playce.migrator.analysis.process.component.impl.AnalysisStatusComponent;
import io.playce.migrator.config.PlayceMigratorConfig;
import io.playce.migrator.constant.*;
import io.playce.migrator.dao.entity.ApplicationSummary;
import io.playce.migrator.dao.repository.ApplicationSummaryRepository;
import io.playce.migrator.dto.analysis.AnalysisTaskItem;
import io.playce.migrator.dto.progress.JobProgress;
import io.playce.migrator.exception.PlayceMigratorException;
import io.playce.migrator.prepare.AbstractPrepareService;
import io.playce.migrator.util.PathUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.playce.migrator.util.WebSocketSender.sendMessage;


@Service
@RequiredArgsConstructor
@Slf4j
public class ApplicationSummaryService {
    private final PlayceMigratorConfig playceMigratorConfig;
    private final ApplicationSummaryRepository applicationSummaryRepository;
    private final ApplicationOverviewService applicationOverviewService;
    private final ConversionComponent conversionComponent;
    private final AnalysisStatusComponent analysisStatusComponent;

    @Transactional
    public void removeSummaries(AnalysisTaskItem item) {
        Long applicationId = item.getApplicationId();
        applicationSummaryRepository.deleteByApplicationId(applicationId);
    }

    @Transactional
    public void createSummaries(AnalysisTaskItem item, Path appPath, JobProgress progress) {
        Long applicationId = item.getApplicationId();

        removeSummaries(item);
        createFileCountSummaries(applicationId, appPath);
        sendMessage(analysisStatusComponent, progress, JobStep.STEP1, JobStatus.PROC, "summary... count by file type.");

        Path targetDir = AbstractPrepareService.getApplicationDir(playceMigratorConfig.getWorkDir(), item.getApplicationId(), item.getOriginFileName());
        Path newTargetDir = AbstractPrepareService.getTargetUnzipDir(targetDir);
        if(item.getApplicationType().equals(ApplicationType.ZIP.name())) {
            Map<String, Integer> charsetMap = conversionComponent.conversionUtf8(newTargetDir);
            createCharsetSummaries(applicationId, charsetMap);
            sendMessage(analysisStatusComponent, progress, JobStep.STEP1, JobStatus.PROC, "summary... number of files per charset.");
        }
    }

    private void createFileCountSummaries(Long applicationId, Path appPath) {
        List<String> libraries = applicationOverviewService.getLibraries(applicationId);

        createFileCountSummary(appPath, applicationId, appPath.getFileName().toString());
        for(String name: libraries) {
            Path subLibraryPath = PathUtils.getSubLibrariesPath(appPath, name);
            createFileCountSummary(subLibraryPath, applicationId, "library " + name);
        }
    }

    private void createCharsetSummaries(Long applicationId, Map<String, Integer> charsetMap) {
        charsetMap.forEach((charset, count) -> {
            putSummary(applicationId, charset, count);
        });
    }

    private void createFileCountSummary(Path appPath, Long applicationId, String name) {
        Path unzipAppPath = Path.of(appPath.toString(), MigratorPath.unzip.path());
        fileCountSummary(applicationId, unzipAppPath, name);
        Path decompileAppPath = Path.of(appPath.toString(), MigratorPath.decompile.path());
        fileCountSummary(applicationId, decompileAppPath, name + " decompiled");
    }

    private void fileCountSummary(Long applicationId, Path path, String name) {
        if(!path.toFile().exists()) {
            log.debug("The destination path does not exist. path: {}", path);
            return;
        }
        Map<String, Integer> result = new HashMap<>();
        try(Stream<Path> stream = Files.walk(path)) {
            List<Path> files = stream.filter(Files::isRegularFile).collect(Collectors.toList());
            files.forEach(f -> {
                String extension = FilenameUtils.getExtension(f.toString());
                if(result.containsKey(extension)) {
                    result.put(extension, result.get(extension) + 1);
                } else {
                    result.put(extension, 1);
                }
            });
        } catch (IOException e) {
            throw new PlayceMigratorException(ErrorCode.PM271R, e);
        }

        for(String extension: result.keySet()) {
            putSummary(applicationId, name + StringUtils.SPACE + extension, result.get(extension));
        }
    }

    private void putSummary(Long applicationId, String summaryGubun, Integer fileCount) {
        ApplicationSummary summary = new ApplicationSummary();
        summary.setApplicationId(applicationId);
        summary.setSummaryGubun(summaryGubun);
        summary.setFileCount(fileCount);

        applicationSummaryRepository.save(summary);
    }
}