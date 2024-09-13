package io.playce.migrator.domain.settings;

import io.playce.migrator.analysis.process.service.AnalysisRuleService;
import io.playce.migrator.constant.ErrorCode;
import io.playce.migrator.dto.analysis.AnalysisRuleCreateRequest;
import io.playce.migrator.dto.analysis.AnalysisRuleResponse;
import io.playce.migrator.dto.analysis.AnalysisRuleSimulationRequest;
import io.playce.migrator.dto.analysisreport.IssueDetail;
import io.playce.migrator.dto.migration.MigrationRuleResponse;
import io.playce.migrator.exception.PlayceMigratorException;
import io.playce.migrator.migration.process.service.MigrationRuleService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RequestMapping("/api/settings")
@RestController
@RequiredArgsConstructor
public class SettingsController {
    private final AnalysisRuleService analysisRuleService;
    private final MigrationRuleService migrationRuleService;

    @GetMapping("/analysis-rules")
    @ResponseStatus(HttpStatus.OK)
    public List<AnalysisRuleResponse> getAnalysisRules() {
        return analysisRuleService.getAnalysisRules();
    }

    @GetMapping("/analysis-rules/{analysisRuleId}")
    @ResponseStatus(HttpStatus.OK)
    public AnalysisRuleResponse getAnalysisRule(@PathVariable Long analysisRuleId) {
        return analysisRuleService.getAnalysisRule(analysisRuleId);
    }

    @PostMapping("/analysis-rules")
    @ResponseStatus(HttpStatus.OK)
    public AnalysisRuleResponse createAnalysisRules(@Validated @RequestBody AnalysisRuleCreateRequest analysisRuleCreateRequest, BindingResult bindingResult) {
        validateRequest(bindingResult);
        Long analysisRuleId = analysisRuleService.createAnalysisRule(analysisRuleCreateRequest);
        return analysisRuleService.getAnalysisRule(analysisRuleId);
    }

    @PostMapping("/analysis-rules/simulation")
    @ResponseStatus(HttpStatus.OK)
    public List<IssueDetail> simulateAnalysisRules(@RequestBody AnalysisRuleSimulationRequest request) {
        List<IssueDetail> issueDetails = analysisRuleService.simulateAnalysisRule(request);
        log.debug("rule simulate result: {}", issueDetails);
        if (issueDetails.isEmpty()) {
            throw new PlayceMigratorException(ErrorCode.PM702T);
        }
        return issueDetails;
    }

    @DeleteMapping("/analysis-rules/{analysisRuleId}")
    public AnalysisRuleResponse removeAnalysisRule(@PathVariable Long analysisRuleId) {
        AnalysisRuleResponse analysisRule = analysisRuleService.getAnalysisRule(analysisRuleId);
        analysisRuleService.removeAnalysisRule(analysisRuleId);
        return analysisRule;
    }

    @GetMapping("/migration-rules")
    @ResponseStatus(HttpStatus.OK)
    public List<MigrationRuleResponse> getMigrationRules() {
        return migrationRuleService.getMigrationRules();
    }

    @GetMapping("/migration-rules/{migrationRuleId}")
    @ResponseStatus(HttpStatus.OK)
    public MigrationRuleResponse getMigrationRule(@PathVariable Long migrationRuleId) {
        return migrationRuleService.getMigrationRule(migrationRuleId);
    }

    private void validateRequest(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new PlayceMigratorException(ErrorCode.PM107H, "An error occurred while binding data.");
        }
    }
}
