package io.playce.migrator.dto.analysishistory;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class LastAnalysisHistory {
    private String jobStatus;
    private String errorMessage;
}
