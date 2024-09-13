package io.playce.migrator.domain.migration.controller;

import com.fasterxml.jackson.databind.JsonNode;
import io.playce.migrator.analysis.process.service.AnalysisHistoryService;
import io.playce.migrator.constant.ErrorCode;
import io.playce.migrator.constant.JobStatus;
import io.playce.migrator.constant.TaskType;
import io.playce.migrator.domain.application.service.ApplicationService;
import io.playce.migrator.domain.project.service.ProjectService;
import io.playce.migrator.dto.application.ApplicationResponse;
import io.playce.migrator.dto.migration.MigrationCondition;
import io.playce.migrator.dto.migration.MigrationRuleResponse;
import io.playce.migrator.dto.migrationhistory.AppliedMigrationRuleResponse;
import io.playce.migrator.dto.migrationhistory.ChangedFileContent;
import io.playce.migrator.dto.migrationhistory.ChangedFiles;
import io.playce.migrator.dto.migrationhistory.MigrationHistoryResponse;
import io.playce.migrator.exception.PlayceMigratorException;
import io.playce.migrator.migration.process.component.impl.MigrationStatusComponent;
import io.playce.migrator.migration.process.service.MigrationHistoryService;
import io.playce.migrator.migration.process.service.MigrationRuleService;
import io.playce.migrator.util.FileUtil;
import io.playce.migrator.util.JacksonObjectMapperUtil;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriUtils;

import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

@RequestMapping("/api/projects/{projectId}/applications/{applicationId}")
@RestController
@RequiredArgsConstructor
public class MigrationController {

    private final ProjectService projectService;
    private final ApplicationService applicationService;
    private final MigrationHistoryService migrationHistoryService;
    private final MigrationRuleService migrationRuleService;
    private final AnalysisHistoryService analysisHistoryService;
    private final MigrationStatusComponent migrationStatusComponent;

    private void validateProjectAndApp(Long projectId, Long applicationId) {
        projectService.getProject(projectId);
        applicationService.getApplicationDetail(projectId, applicationId);
    }

    @PostMapping("/migration")
    @ResponseStatus(HttpStatus.OK)
    public void requestMigration(@PathVariable Long projectId, @PathVariable Long applicationId, @RequestBody String migrationRequest) {
        analysisHistoryService.checkAnalysisSuccessExist(applicationId);
        applicationService.checkProgressingJob(applicationId, TaskType.Migration.name());
        validateProjectAndApp(projectId, applicationId);
        JsonNode jsonNode = JacksonObjectMapperUtil.readTree(migrationRequest);
        List<MigrationCondition> conditionList = parsingToConditionList(jsonNode);
        Long migrationHistoryId = migrationHistoryService.registMigrationHistory(applicationId, conditionList);
        migrationStatusComponent.sendStep0Status(projectId, applicationId, migrationHistoryId, JobStatus.REQ);
    }

    @GetMapping("/migration-histories")
    @ResponseStatus(HttpStatus.OK)
    public List<MigrationHistoryResponse> getMigrationHistories(@PathVariable Long projectId, @PathVariable Long
            applicationId) {
        validateProjectAndApp(projectId, applicationId);
        return migrationHistoryService.getMigrationHistories(applicationId);
    }

    @PatchMapping("/migration-histories/{migrationHistoryId}")
    @ResponseStatus(HttpStatus.OK)
    public void cancelMigration(@PathVariable Long projectId,
                                @PathVariable Long applicationId,
                                @PathVariable Long migrationHistoryId) {
        validateProjectAndApp(projectId, applicationId);
        migrationHistoryService.cancelMigration(migrationHistoryId);
    }

    @GetMapping("/migration-rules")
    @ResponseStatus(HttpStatus.OK)
    public List<MigrationRuleResponse> getMigrationRules() {
        return migrationRuleService.getMigrationRules();
    }

    @GetMapping("/migration-result-download")
    public ResponseEntity<Resource> downloadMigrationResultFile(@PathVariable Long projectId, @PathVariable Long
            applicationId, Long migrationHistoryId) {
        validateProjectAndApp(projectId, applicationId);
        MigrationHistoryResponse migrationHistoryResponse = migrationHistoryService.getMigrationHistoryResponse(migrationHistoryId);

        String filePath = migrationHistoryResponse.getMigrationResultFilePath();
        Resource resource = FileUtil.getFile(filePath);
        String encodeFileName = UriUtils.encode(FileUtil.getName(filePath), StandardCharsets.UTF_8);
        String contentDisposition = "attachment;filename=" + encodeFileName + ";filename*=UTF-8''" + encodeFileName;
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition).body(resource);
    }

    @GetMapping("/applied-migration-rules")
    @ResponseStatus(HttpStatus.OK)
    public List<AppliedMigrationRuleResponse> getAppliedMigrationRules(@PathVariable Long projectId,
                                                                       @PathVariable Long applicationId,
                                                                       @RequestParam Long migrationHistoryId) {
        validateProjectAndApp(projectId, applicationId);
        return migrationHistoryService.getAppliedMigrationRules(migrationHistoryId);
    }

    @GetMapping("/migration-histories/{migrationHistoryId}/changed-files")
    @ResponseStatus(HttpStatus.OK)
    public List<ChangedFiles> getChangedFiles(@PathVariable Long projectId,
                                              @PathVariable Long applicationId,
                                              @PathVariable Long migrationHistoryId) {
        validateProjectAndApp(projectId, applicationId);
        return migrationHistoryService.getChangedFiles(migrationHistoryId);
    }

    @GetMapping("/migration-histories/{migrationHistoryId}/changed-files-content/{changedFileId}")
    @ResponseStatus(HttpStatus.OK)
    public ChangedFileContent getChangedFileContent(@PathVariable Long projectId,
                                                    @PathVariable Long applicationId,
                                                    @PathVariable Long migrationHistoryId,
                                                    @PathVariable Long changedFileId) throws Exception {
        projectService.getProject(projectId);
        ApplicationResponse application = applicationService.getApplicationDetail(projectId, applicationId);
        return migrationHistoryService.getChangedFileContent(application, migrationHistoryId, changedFileId);
    }

    private List<MigrationCondition> parsingToConditionList(JsonNode jsonNode) {
        List<MigrationCondition> migrationConditions = new ArrayList<>();
        try {
            for (JsonNode node : jsonNode) {
                long migrationRuleId = node.get("migrationRuleId").asLong();
                String migrationCondition = node.get("migrationCondition").toString();

                MigrationCondition mc = new MigrationCondition();
                mc.setMigrationRuleId(migrationRuleId);
                mc.setMigrationCondition(migrationCondition);
                migrationConditions.add(mc);
            }
        } catch (Exception e) {
            throw new PlayceMigratorException(ErrorCode.PM107H);
        }
        return migrationConditions;
    }
}
