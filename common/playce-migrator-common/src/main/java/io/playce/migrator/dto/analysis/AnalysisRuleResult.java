package io.playce.migrator.dto.analysis;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter @Getter @ToString
public class AnalysisRuleResult {
    private Long analysisRuleId;
    private Integer lineNumber;
    private String analysisFilePath;
    private String analysisOriginFilePath;
    private String analysisFileName;
    private String detectedString;
    private String overrideComment;
}
