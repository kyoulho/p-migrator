package io.playce.migrator.dto.analysisreport;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class DeletedApiResponse {
    private String className;
    private String jdkInternalApi;
    private String replacement;
}
