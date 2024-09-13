package io.playce.migrator.dto.analysishistory;

import lombok.Getter;
import lombok.Setter;

import java.time.Instant;

@Getter
@Setter
public class SuccessAnalysisHistory {
    private Long applicationId;
    private Long analysisHistoryId;
    private String jobStatus;
    private String errorMessage;
    private Instant startDatetime;
    private Instant endDatetime;
}
