package io.playce.migrator.dto.migrationhistory;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LastMigrationHistory {
    private String jobStatus;
    private String errorMessage;
}
