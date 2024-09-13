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
 * Dong-Heon Han    Aug 29, 2022		First Draft.
 */

package io.playce.migrator.migration.process.service;

import io.playce.migrator.config.PlayceMigratorConfig;
import io.playce.migrator.constant.*;
import io.playce.migrator.dao.entity.ChangedFile;
import io.playce.migrator.dao.mybatis.MybatisMigrationRuleLink;
import io.playce.migrator.dao.repository.ChangedFileRepository;
import io.playce.migrator.dto.migration.MigrationTaskItem;
import io.playce.migrator.dto.progress.JobProgress;
import io.playce.migrator.exception.PlayceMigratorException;
import io.playce.migrator.migration.generator.GenerateRequest;
import io.playce.migrator.migration.process.async.MigrationRuleComponent;
import io.playce.migrator.migration.process.component.impl.MigrationStatusComponent;
import io.playce.migrator.rule.loader.RuleSessionFactory;
import io.playce.migrator.rule.loader.service.impl.DatabaseRuleLoader;
import io.playce.migrator.util.FileUtil;
import io.playce.migrator.util.PathUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.collections4.CollectionUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.BlockingQueue;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.playce.migrator.util.WebSocketSender.sendMessage;

@Service
@RequiredArgsConstructor
@Slf4j
public class MigrationProcessService {
    private final PlayceMigratorConfig playceMigratorConfig;
    private final MigrationHistoryService migrationHistoryService;
    private final BlockingQueue<MigrationTaskItem> migrationQueue;
    private final MigrationRuleLinkService migrationRuleLinkService;
    private final DatabaseRuleLoader databaseRuleLoader;
    private final MigrationRuleComponent migrationRuleComponent;
    private final MigrationStatusComponent migrationStatusComponent;
    private final ChangedFileRepository changedFileRepository;
    private final ResultGeneratorService resultGeneratorService;


    @Async("migrationExecutor")
    public void processTask() {
        try {
            MigrationTaskItem item = migrationQueue.take();
            log.debug("analysisQueue out -> applicationId: {}, migrationHistoryId: {}", item.getApplicationId(), item.getMigrationHistoryId());
            process(item);
        } catch (InterruptedException e) {
            throw new PlayceMigratorException(ErrorCode.PM701T, e.getMessage(), e);
        }
    }

    public void process(MigrationTaskItem item) {
        log.debug("start migration - migrationHistoryId: {}, {}", item.getMigrationHistoryId(), item);
        Path workDir = playceMigratorConfig.getWorkDirWithApp(item.getApplicationId());
        String appName = item.getOriginFileName();
        Path appPath = Path.of(workDir.toString(), appName);
        Path migrationPath = Path.of(workDir.toString(), appName, MigratorPath.migartion.path());

        JobProgress progress = JobProgress.builder().projectId(item.getProjectId()).applicationId(item.getApplicationId()).historyId(item.getMigrationHistoryId()).originFileName(item.getOriginFileName()).build();
        if (isCanceled(item, progress, JobStep.STEP1)) return;

        try {
            long totalCount = FileType.values().length + 10;
            progress.setTotal(totalCount);

            //delete migration path
            FileUtil.removeFile(migrationPath);
            sendMessage(migrationStatusComponent, progress, JobStep.STEP1, JobStatus.PROC, "remove migration file.");

            //update status
            migrationHistoryService.updateJobToProcess(item, Instant.now());

            //source path
            Path sourcePath = Path.of(appPath.toString(), MigratorPath.unzip.path());
            if (!sourcePath.toFile().exists()) {
                String message = "The source file to be migrated does not exist. path: " + sourcePath;
                migrationHistoryService.updateJobToFail(item, message, Instant.now());
                throw new PlayceMigratorException(ErrorCode.PM310F, message);
            }

            sendMessage(migrationStatusComponent, progress, JobStep.STEP2, JobStatus.PROC, "count file list.");
            List<String> allFileList = getAllFileList(sourcePath);

            //load migrationRuleLink
            sendMessage(migrationStatusComponent, progress, JobStep.STEP3, JobStatus.PROC, "load rule condition.");
            List<MybatisMigrationRuleLink> ruleLinks = migrationRuleLinkService.getRuleLinkMapByFileType(item.getMigrationHistoryId());
            if (ruleLinks.isEmpty()) return;
            if (isCanceled(item, progress, JobStep.STEP4)) return;

            List<Long> migrationRuleIds = ruleLinks.stream().map(MybatisMigrationRuleLink::getMigrationRuleId).distinct().collect(Collectors.toList());

            sendMessage(migrationStatusComponent, progress, JobStep.STEP3, JobStatus.PROC, "load session factories.");
            //load migration rule
            Map<Long, RuleSessionFactory> sessionFactoryMap = databaseRuleLoader.getSessionFactoryMapByMigrationIds(migrationRuleIds);

            sendMessage(migrationStatusComponent, progress, JobStep.STEP4, JobStatus.PROC, "run rule.");
            //전환
            for (Long migrationRuleId : migrationRuleIds) {
                List<MybatisMigrationRuleLink> linksByRuleId = filterLinkByRuleId(migrationRuleId, ruleLinks);
                if (linksByRuleId.isEmpty()) continue;

                if (isCanceled(item, progress, JobStep.STEP4)) return;
                RuleSessionFactory factory = sessionFactoryMap.get(migrationRuleId);

                if (isCanceled(item, progress, JobStep.STEP4)) return;
                migrationProcess(allFileList, appPath, sourcePath, migrationPath, linksByRuleId.get(0).getFileType(), factory, linksByRuleId, item);
            }

            if (isCanceled(item, progress, JobStep.STEP4)) return;
            sendMessage(migrationStatusComponent, progress, JobStep.STEP4, JobStatus.PROC, "copy etc files.");
            copyOtherFiles(allFileList, appPath, migrationPath);

            //result generate
            if (isCanceled(item, progress, JobStep.STEP4)) return;
            sendMessage(migrationStatusComponent, progress, JobStep.STEP4, JobStatus.PROC, "copy zip file.");
            createResult(appPath, item, migrationPath);

            migrationHistoryService.updateJobToComplete(item, Instant.now());
            sendMessage(migrationStatusComponent, progress, JobStep.STEP5, JobStatus.CMPL, "done.");
            log.debug("done migration - migrationHistoryId: {}", item.getMigrationHistoryId());
        } catch (Exception e) {
            String message = e.toString();
            migrationHistoryService.updateJobToFail(item, message, Instant.now());
            sendMessage(migrationStatusComponent, progress, JobStep.STEP5, JobStatus.CMPL, "failed.", message);
            log.error("failed migration - migrationHistoryId: {}", item.getMigrationHistoryId(), e);
        }
    }

    private List<MybatisMigrationRuleLink> filterLinkByRuleId(Long migrationRuleId, List<MybatisMigrationRuleLink> links) {
        List<MybatisMigrationRuleLink> result = new ArrayList<>();
        for (MybatisMigrationRuleLink link : links) {
            if (Objects.equals(link.getMigrationRuleId(), migrationRuleId)) {
                result.add(link);
            }
        }
        return result;
    }

    private boolean isCanceled(MigrationTaskItem item, JobProgress progress, JobStep jobStep) {
        JobStatus jobStatus = migrationHistoryService.getJobStatus(item);
        if (jobStatus == JobStatus.CNCL) {
            log.info("This job has been cancelled. {}", item);
            migrationHistoryService.updateJobToCancel(item, Instant.now());
            sendMessage(migrationStatusComponent, progress, jobStep, JobStatus.CNCL, "done");
            return true;
        }
        return false;
    }

    private void createResult(Path appPath, MigrationTaskItem item, Path migrationPath) {
        Path migrationFiles = Path.of(migrationPath.toString(), MigratorPath.unzip.name());
        Long migrationHistoryId = item.getMigrationHistoryId();

        GenerateRequest generateRequest = getGenerateRequest(appPath, item, migrationFiles, migrationHistoryId);
        Map<String, Path> resultMap = resultGeneratorService.generator(generateRequest);

        log.debug("result path map: {}", resultMap);
        migrationHistoryService.setMigrationResultPath(migrationHistoryId, resultMap);
    }

    private static GenerateRequest getGenerateRequest(Path appPath, MigrationTaskItem item, Path migrationFiles, Long migrationHistoryId) {
        GenerateRequest generateRequest = new GenerateRequest();
        generateRequest.setAppPath(appPath);
        generateRequest.setProjectName(item.getProjectName());
        generateRequest.setMigrationHistoryId(migrationHistoryId);
        generateRequest.setBaseImageName(item.getBaseImageName());
        generateRequest.setDockerFileConfig(item.getDockerfileConfig());
        generateRequest.setContainerizationYn(item.isContainerizationYn());
        generateRequest.setApplicationType(ApplicationType.valueOf(item.getApplicationType()));
        generateRequest.setMigrationFiles(migrationFiles);
        generateRequest.setTagName(item.getTagName());
        return generateRequest;
    }

    private void copyOtherFiles(List<String> allFileList, Path appPath, Path migrationPath) {
        for (String fileStr : allFileList) {
            Path sourceFilePath = Path.of(fileStr);
            Path targetFilePath = PathUtils.getTargetFilePath(appPath, migrationPath, sourceFilePath);
            log.debug("copy file: {}", fileStr);
            try {
                FileUtil.createDirectoryIfnotExist(targetFilePath.getParent());
                Files.copy(sourceFilePath, targetFilePath, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException e) {
                throw new PlayceMigratorException(ErrorCode.PM304F, e);
            }
        }
    }

    private List<String> getAllFileList(Path sourcePath) {
        try (Stream<Path> stream = Files.walk(sourcePath)) {
            return stream.filter(Files::isRegularFile).map(Path::toString).collect(Collectors.toList());
        } catch (IOException e) {
            throw new PlayceMigratorException(ErrorCode.PM371F, e);
        }
    }

    private void migrationProcess(List<String> allFileList, Path appPath, Path sourcePath, Path migrationPath, String type,
                                  RuleSessionFactory factory, List<MybatisMigrationRuleLink> links, MigrationTaskItem item) {
        // modify Collections.synchronizeList to CopyOnWriteArrayList
        // https://stackoverflow.com/questions/25113987/why-is-there-a-concurrentmodificationexception-even-when-list-is-synchronized
        List<ChangedFile> changedFileList = new CopyOnWriteArrayList<>();

        try (Stream<Path> workStream = Files.walk(sourcePath)) {
            List<Path> files = workStream.filter(Files::isRegularFile)
                    .filter(f -> f.getFileName().toString().endsWith(type.toLowerCase())).collect(Collectors.toList());
            if (files.isEmpty()) return;

            List<CompletableFuture<Void>> futures = files.stream()
                    .map(f -> {
                        Path targetFilePath = PathUtils.getTargetFilePath(appPath, migrationPath, f);
                        FileUtil.createDirectoryIfnotExist(targetFilePath.getParent());
                        allFileList.remove(f.toString());

                        return migrationRuleComponent.migration(f, targetFilePath, factory, links, item, changedFileList);
                    }).collect(Collectors.toList());
            futures.forEach(CompletableFuture::join);

            if (CollectionUtils.isNotEmpty(changedFileList)) {
                // 변경 된 파일이 존재하는 경우 change file insert
                changedFileRepository.saveAll(changedFileList);
            }
        } catch (IOException e) {
            throw new PlayceMigratorException(ErrorCode.PM271R, e);
        }
    }
}