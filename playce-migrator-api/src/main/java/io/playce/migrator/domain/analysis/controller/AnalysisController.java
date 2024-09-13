package io.playce.migrator.domain.analysis.controller;

import io.playce.migrator.analysis.process.component.impl.AnalysisStatusComponent;
import io.playce.migrator.analysis.process.service.AnalysisHistoryService;
import io.playce.migrator.analysis.process.service.AnalysisReportService;
import io.playce.migrator.analysis.process.service.AnalysisRuleService;
import io.playce.migrator.analysis.process.service.ApplicationOverviewService;
import io.playce.migrator.constant.JobStatus;
import io.playce.migrator.constant.TaskType;
import io.playce.migrator.domain.application.service.ApplicationService;
import io.playce.migrator.domain.project.service.ProjectService;
import io.playce.migrator.dto.analysis.AnalysisRuleResponse;
import io.playce.migrator.dto.analysishistory.AnalysisHistoryResponse;
import io.playce.migrator.dto.analysishistory.AppliedAnalysisRuleResponse;
import io.playce.migrator.dto.analysisreport.*;
import io.playce.migrator.dto.application.ApplicationResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/projects/{projectId}/applications/{applicationId}")
@RequiredArgsConstructor
public class AnalysisController {

    private final ProjectService projectService;
    private final ApplicationService applicationService;
    private final ApplicationOverviewService applicationOverviewService;
    private final AnalysisHistoryService analysisHistoryService;

    private final AnalysisReportService analysisReportService;
    private final AnalysisRuleService analysisRuleService;
    private final AnalysisStatusComponent analysisStatusComponent;

    private ApplicationResponse validateProjectAndApp(Long projectId, Long applicationId) {
        projectService.getProject(projectId);
        return applicationService.getApplicationDetail(projectId, applicationId);
    }

    @PostMapping("/analysis")
    public void requestAnalysis(@PathVariable Long projectId, @PathVariable Long applicationId) {
        applicationService.checkProgressingJob(applicationId, TaskType.Analysis.name());
        validateProjectAndApp(projectId, applicationId);
        String libraryNames = applicationOverviewService.getLibraryNames(applicationId);
        Long analysisHistoryId = analysisHistoryService.registHistory(applicationId, libraryNames);
        analysisStatusComponent.sendStep0Status(projectId, applicationId, analysisHistoryId, JobStatus.REQ);
    }


    @GetMapping("/analysis-histories")
    @ResponseStatus(HttpStatus.OK)
    public List<AnalysisHistoryResponse> getAnalysisHistories(@PathVariable Long projectId, @PathVariable Long applicationId) {
        validateProjectAndApp(projectId, applicationId);
        return analysisHistoryService.getAnalysisHistories(applicationId);
    }

    @PatchMapping("/analysis-histories/{analysisHistoryId}")
    @ResponseStatus(HttpStatus.OK)
    public void cancelAnalysis(@PathVariable Long projectId,
                               @PathVariable Long applicationId,
                               @PathVariable Long analysisHistoryId) {
        validateProjectAndApp(projectId, applicationId);
        analysisHistoryService.cancelAnalysis(analysisHistoryId);
    }


    @GetMapping("/dependencies")
    @ResponseStatus(HttpStatus.OK)
    public List<AnalyzedLibraries> getAnalyzedDependencies(@PathVariable Long projectId,
                                                           @PathVariable Long applicationId) {
        validateProjectAndApp(projectId, applicationId);
        return analysisReportService.getAnalyzedDependencies(applicationId);
    }

    @GetMapping("/analysis-histories/{analysisHistoryId}/report-groups")
    @ResponseStatus(HttpStatus.OK)
    public List<AnalysisReportGroupResponse> getAnalysisReportGroups(@PathVariable Long projectId,
                                                                     @PathVariable Long applicationId,
                                                                     @PathVariable Long analysisHistoryId) {
        validateProjectAndApp(projectId, applicationId);
        return analysisReportService.getAnalysisReportGroups();
    }

    @GetMapping("/analysis-histories/{analysisHistoryId}/report-groups/{analysisReportGroupId}/analysisRules")
    @ResponseStatus(HttpStatus.OK)
    public List<AnalysisReportRuleResponse> getAnalysisReportRules(@PathVariable Long projectId,
                                                                   @PathVariable Long applicationId,
                                                                   @PathVariable Long analysisHistoryId,
                                                                   @PathVariable Long analysisReportGroupId) {
        validateProjectAndApp(projectId, applicationId);
        return analysisReportService.getAnalysisReportRules(analysisHistoryId, analysisReportGroupId);
    }

    @GetMapping("/analysis-histories/{analysisHistoryId}/report-groups/{analysisReportGroupId}/analysisRules/{analysisRuleId}")
    @ResponseStatus(HttpStatus.OK)
    public List<AnalysisReportRuleResult> getAnalysisReportRuleResults(@PathVariable Long projectId,
                                                                       @PathVariable Long applicationId,
                                                                       @PathVariable Long analysisHistoryId,
                                                                       @PathVariable Long analysisReportGroupId,
                                                                       @PathVariable Long analysisRuleId) {
        validateProjectAndApp(projectId, applicationId);
        return analysisReportService.getAnalysisReportRuleResults(analysisHistoryId, analysisRuleId);
    }

    @GetMapping("/analysis-rules")
    @ResponseStatus(HttpStatus.OK)
    public List<AnalysisRuleResponse> getAnalysisRules(@PathVariable Long projectId, @PathVariable Long applicationId) {
        ApplicationResponse response = validateProjectAndApp(projectId, applicationId);
        return analysisRuleService.getRulesByTargetId(response.getTargetId());
    }

    @GetMapping("/applied-analysis-rules")
    @ResponseStatus(HttpStatus.OK)
    public List<AppliedAnalysisRuleResponse> getAppliedAnalysisRules(@PathVariable Long projectId,
                                                                     @PathVariable Long applicationId,
                                                                     @RequestParam Long analysisHistoryId) {
        validateProjectAndApp(projectId, applicationId);
        return analysisHistoryService.getAppliedAnalysisRules(analysisHistoryId);
    }

    @GetMapping("/deprecated-apis")
    @ResponseStatus(HttpStatus.OK)
    public List<DeprecatedApiResponse> getDeprecatedApis(@PathVariable Long projectId,
                                                         @PathVariable Long applicationId) {
        validateProjectAndApp(projectId, applicationId);
        return analysisReportService.getDeprecatedApis(applicationId);
    }

    @GetMapping("/deleted-apis")
    @ResponseStatus(HttpStatus.OK)
    public List<DeletedApiResponse> getDeletedApis(@PathVariable Long projectId,
                                                   @PathVariable Long applicationId) {
        validateProjectAndApp(projectId, applicationId);
        return analysisReportService.getDeletedApis(applicationId);
    }

    @GetMapping("/analysis-histories/{analysisHistoryId}/summary/file-count-by-extension")
    @ResponseStatus(HttpStatus.OK)
    public List<SummaryExtension> getSummaryExtension(@PathVariable Long analysisHistoryId,
                                                      @PathVariable Long applicationId,
                                                      @PathVariable Long projectId) {
        validateProjectAndApp(projectId, applicationId);
        return analysisReportService.getSummaryExtensions(applicationId);
    }

    @GetMapping("/analysis-histories/{analysisHistoryId}/summary/file-count-by-technology")
    @ResponseStatus(HttpStatus.OK)
    public List<SummaryTechnology> getSummaryTechnology(@PathVariable Long analysisHistoryId,
                                                      @PathVariable Long applicationId,
                                                      @PathVariable Long projectId) {
        validateProjectAndApp(projectId, applicationId);
        return analysisReportService.getSummaryTechnologies(analysisHistoryId);
    }
}
