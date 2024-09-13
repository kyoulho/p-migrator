package io.playce.migrator.dto.analysisreport;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class AnalyzedLibraries {
    private String groupName;
    private List<AnalyzedLibrary> libraries;
}
