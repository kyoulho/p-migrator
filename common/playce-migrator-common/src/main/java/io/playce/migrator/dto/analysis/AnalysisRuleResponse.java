package io.playce.migrator.dto.analysis;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.Instant;

@Getter
@Setter
@ToString
public class AnalysisRuleResponse {
    private Long analysisRuleId;
    private String analysisRuleName;
    private String importance;
    private String analysisRuleContent;
    private String comment;
    private String todo;
    private String link;
    private String description;
    private Long analysisProcessingPolicyId;
    private String processingType;
    private String registLoginId;
    private Instant registDatetime;
    private String modifyLoginId;
    private Instant modifyDatetime;
}
