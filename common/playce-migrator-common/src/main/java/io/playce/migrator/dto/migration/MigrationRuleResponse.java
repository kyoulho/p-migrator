package io.playce.migrator.dto.migration;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class MigrationRuleResponse {
    private Long migrationRuleId;
    private String migrationRuleName;
    private String fileType;
    private String migrationRuleContent;
    private String description;
    private String registLoginId;
    private Instant registDatetime;
    private String modifyLoginId;
    private Instant modifyDatetime;
}
