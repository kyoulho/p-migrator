package io.playce.migrator.migration.process.service;

import io.playce.migrator.config.PlayceMigratorConfig;
import io.playce.migrator.constant.ErrorCode;
import io.playce.migrator.constant.JobStatus;
import io.playce.migrator.constant.MigratorPath;
import io.playce.migrator.dao.entity.ChangedFile;
import io.playce.migrator.dao.entity.MigrationHistory;
import io.playce.migrator.dao.mapper.MigrationHistoryMapper;
import io.playce.migrator.dao.repository.ChangedFileRepository;
import io.playce.migrator.dao.repository.MigrationHistoryRepository;
import io.playce.migrator.dto.application.ApplicationResponse;
import io.playce.migrator.dto.migration.MigrationCondition;
import io.playce.migrator.dto.migration.MigrationTaskItem;
import io.playce.migrator.dto.migrationhistory.*;
import io.playce.migrator.exception.PlayceMigratorException;
import io.playce.migrator.migration.generator.GeneratorName;
import io.playce.migrator.util.LoginUserUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.io.File;
import java.io.FileReader;
import java.io.IOException;
import java.nio.file.Path;
import java.time.Instant;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class MigrationHistoryService {

    private final MigrationHistoryRepository migrationHistoryRepository;
    private final MigrationHistoryMapper migrationHistoryMapper;

    private final MigrationRuleLinkService migrationRuleLinkService;
    private final LoginUserUtil loginUserUtil;
    private final ModelMapper modelMapper;
    private final ChangedFileRepository changedFileRepository;
    private final PlayceMigratorConfig playceMigratorConfig;

    public Long registMigrationHistory(Long applicationId, List<MigrationCondition> conditionList) {
        MigrationHistory migrationHistory = new MigrationHistory();
        migrationHistory.setJobStatus(JobStatus.REQ.name());
        migrationHistory.setApplicationId(applicationId);
        migrationHistory.setRegistLoginId(loginUserUtil.getLoginId());
        migrationHistory.setRegistDatetime(Instant.now());
        Long migrationHistoryId = migrationHistoryRepository.save(migrationHistory).getMigrationHistoryId();

        for (MigrationCondition mc : conditionList) {
            migrationRuleLinkService.registMigrationRuleLink(migrationHistoryId, mc.getMigrationRuleId(), mc.getMigrationCondition());
        }
        return migrationHistoryId;
    }

    public MigrationHistoryResponse getMigrationHistoryResponse(Long migrationHistoryId) {
        MigrationHistory migrationHistory = getMigrationHistory(migrationHistoryId);
        // todo 리스폰스 DTO 필드명 변경
        MigrationHistoryResponse response = modelMapper.map(migrationHistory, MigrationHistoryResponse.class);
        response.setMigrationResultFilePath(migrationHistory.getCompressResultPath());
        return response;
    }

    public LastMigrationHistory getLastHistory(Long applicationId) {
        MigrationHistory migrationHistory = migrationHistoryRepository.findFirstByApplicationIdOrderByEndDatetimeDesc(applicationId)
                .orElse(null);
        if (migrationHistory == null) return null;

        return modelMapper.map(migrationHistory, LastMigrationHistory.class);
    }

    public SuccessMigrationHistory getSuccessHistory(Long applicationId) {
        MigrationHistory migrationHistory = migrationHistoryRepository.findFirstByApplicationIdAndJobStatusOrderByEndDatetimeDesc(applicationId, JobStatus.CMPL.name())
                .orElse(null);
        if (migrationHistory == null) return null;
        return modelMapper.map(migrationHistory, SuccessMigrationHistory.class);
    }

    public List<MigrationHistoryResponse> getMigrationHistories(Long applicationId) {
        // todo query 변경
        return migrationHistoryMapper.selectMigrationHistories(applicationId);
    }

    private MigrationHistory getMigrationHistory(Long migrationHistoryId) {
        return migrationHistoryRepository.findById(migrationHistoryId).orElseThrow(() ->
                new PlayceMigratorException(ErrorCode.PM201R, "Migration history does not exist. id: " + migrationHistoryId));
    }

    public JobStatus getJobStatus(MigrationTaskItem item) {
        MigrationHistory migrationHistory = getMigrationHistory(item.getMigrationHistoryId());
        return JobStatus.valueOf(migrationHistory.getJobStatus());
    }

    public void updateJobToFail(MigrationTaskItem item, String errorMessage, Instant now) {
        MigrationHistory migrationHistory = getMigrationHistory(item.getMigrationHistoryId());
        migrationHistory.setJobStatus(JobStatus.FAIL.name());
        migrationHistory.setErrorMessage(errorMessage);
        migrationHistory.setEndDatetime(now);
    }

    public void handlingInProgressItemFailures() {
        migrationHistoryMapper.updateToStatusByFromStatus(JobStatus.PROC.name(), JobStatus.FAIL.name());
    }

    public List<MigrationTaskItem> getRequestedMigrationHistory() {
        return migrationHistoryMapper.selectMigrationRequestItems();
    }

    public void setReqStatusToPend(Long migrationHistoryId) {
        setToStatusByFromStatusAndId(JobStatus.REQ, JobStatus.PEND, migrationHistoryId);
    }

    private void setToStatusByFromStatusAndId(JobStatus from, JobStatus to, Long id) {
        MigrationHistory history = migrationHistoryRepository.findByMigrationHistoryIdAndJobStatus(id, from.name()).orElseThrow(() ->
                new PlayceMigratorException(ErrorCode.PM201R, "migration history not found, id: " + id));
        history.setJobStatus(to.name());
    }

    public void setMigrationResultPath(Long migrationHistoryId, Map<String,Path> resultMap) {
        MigrationHistory history = getMigrationHistory(migrationHistoryId);
        history.setCompressResultPath(getResultPath(GeneratorName.ZIP_GEN,resultMap));
        history.setBuildResultPath(getResultPath(GeneratorName.BUILD_GEN, resultMap));
        history.setDockerfileResultPath(getResultPath(GeneratorName.DOCKERFILE_GEN, resultMap));
        history.setDockerImageResultPath(getResultPath(GeneratorName.DOCKER_IMAGE_GEN, resultMap));
    }

    private String getResultPath(GeneratorName gen, Map<String, Path> resultMap) {
        if(resultMap.containsKey(gen.name())) {
            return resultMap.get(gen.name()).toString();
        }
        return null;
    }

    public List<AppliedMigrationRuleResponse> getAppliedMigrationRules(Long migrationHistoryId) {
        getMigrationHistory(migrationHistoryId);
        return migrationHistoryMapper.selectAppliedMigrationRuleList(migrationHistoryId);
    }

    public void updateJobToComplete(MigrationTaskItem item, Instant now) {
        MigrationHistory migrationHistory = getMigrationHistory(item.getMigrationHistoryId());
        migrationHistory.setJobStatus(JobStatus.CMPL.name());
        migrationHistory.setErrorMessage(null);
        migrationHistory.setEndDatetime(now);
    }

    public void updateJobToProcess(MigrationTaskItem item, Instant now) {
        MigrationHistory migrationHistory = getMigrationHistory(item.getMigrationHistoryId());
        migrationHistory.setJobStatus(JobStatus.PROC.name());
        migrationHistory.setStartDatetime(now);
    }

    public void cancelMigration(Long migrationHistoryId) {
        MigrationHistory migrationHistory = getMigrationHistory(migrationHistoryId);
        if (JobStatus.CMPL.name().equals(migrationHistory.getJobStatus()) ||
                JobStatus.FAIL.name().equals(migrationHistory.getJobStatus()) ||
                JobStatus.CNCL.name().equals(migrationHistory.getJobStatus())) {
            throw new PlayceMigratorException(ErrorCode.PM202R, "migration history status '" + migrationHistory.getJobStatus() + "' can not be cancel.");
        }
        migrationHistory.setJobStatus(JobStatus.CNCL.name());
        migrationHistory.setEndDatetime(Instant.now());
    }

    public void updateJobToCancel(MigrationTaskItem item, Instant now) {
        MigrationHistory migrationHistory = getMigrationHistory(item.getMigrationHistoryId());
        migrationHistory.setEndDatetime(now);
    }

    public List<ChangedFiles> getChangedFiles(Long migrationHistoryId) {
        getMigrationHistory(migrationHistoryId);
        return migrationHistoryMapper.selectChangedFiles(migrationHistoryId)
                .stream().map(this::generateFileName).collect(Collectors.toList());
    }

    public ChangedFileContent getChangedFileContent(ApplicationResponse application, Long migrationHistoryId, Long changedFileId) throws Exception {
        getMigrationHistory(migrationHistoryId);
        ChangedFile changedFile = getChangedFile(changedFileId);

        ChangedFileContent changedFileContent = new ChangedFileContent();
        Path workDir = playceMigratorConfig.getWorkDirWithApp(application.getApplicationId());
        Path migrationPath = Path.of(workDir.toString(), application.getOriginFileName(), MigratorPath.migartion.path());
        Path resultPath = Path.of(migrationPath.toString(), MigratorPath.unzip.path(), changedFile.getFilePathInResult());

        File originFile = FileUtils.getFile(changedFile.getOriginFileAbsolutePath());
        File resultFile = FileUtils.getFile(resultPath.toString());
        try {
            if (originFile.exists()) {
                changedFileContent.setOriginal(IOUtils.toString(new FileReader(originFile.getAbsolutePath())));
            }
            if (resultFile.exists()) {
                changedFileContent.setChanged(IOUtils.toString(new FileReader(resultFile.getAbsolutePath())));
            }
        } catch (IOException e) {
            log.debug("Unhandled exception occurred while read files. " +
                    "migration history id : {}, changed file id : {}", migrationHistoryId, changedFileId);
            throw e;
        }
        return changedFileContent;
    }

    private ChangedFiles generateFileName(ChangedFiles file) {
        file.setMigrationFileName(FilenameUtils.getName(file.getMigrationFileName()));
        return file;
    }

    private ChangedFile getChangedFile(Long changedFileId) {
        return changedFileRepository.findById(changedFileId).orElseThrow(() ->
                new PlayceMigratorException(ErrorCode.PM201R, "Changed File does not exist. id: " + changedFileId));
    }
}
