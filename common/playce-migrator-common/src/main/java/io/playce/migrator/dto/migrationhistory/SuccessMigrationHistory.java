package io.playce.migrator.dto.migrationhistory;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class SuccessMigrationHistory {
    private Long applicationId;
    private Long migrationHistoryId;
    private String jobStatus;
    private String errorMessage;
    private Instant startDatetime;
    private Instant endDatetime;
}
