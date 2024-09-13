package io.playce.migrator.dto.analysis;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;


@Setter
@Getter
@ToString
public class AnalysisRuleRequest {
    private Integer startLineNumber;
    private String content;
    private Object objectContent;
    private AnalysisRuleResult result;
}
