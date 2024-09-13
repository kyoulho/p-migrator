package io.playce.migrator.dto.migration;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Setter @Getter @ToString
public class MigrationRuleResult {
    private Long migrationRuleId;
    private Integer lineNumber;
    private String content;
}
