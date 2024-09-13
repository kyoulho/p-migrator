package io.playce.migrator.dto.analysisreport;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AnalysisReportRuleResult {
    private String analysisFilePath;
    private String analysisFileName;
    private List<IssueDetail> issueDetails;
}
