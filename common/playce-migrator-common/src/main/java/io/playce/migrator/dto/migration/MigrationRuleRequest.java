package io.playce.migrator.dto.migration;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.Map;

@Setter @Getter @ToString
public class MigrationRuleRequest {
    private Integer startLineNumber;
    private String content;
    private Map<String, String> input;
    private MigrationRuleResult result;
}
