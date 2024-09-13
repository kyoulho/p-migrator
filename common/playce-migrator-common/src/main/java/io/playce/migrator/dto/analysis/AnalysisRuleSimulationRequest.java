package io.playce.migrator.dto.analysis;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class AnalysisRuleSimulationRequest {
    private String ruleContent;
    private String targetString;
}
