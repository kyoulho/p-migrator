package io.playce.migrator.dto.analysisreport;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class AnalyzedLibrary {
    private String fileName;
    private FileDetails fileDetails;
}
