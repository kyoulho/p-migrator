package io.playce.migrator.dto.analysisreport;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class IssueDetail {
    private Long analysisLineNumber;
    private String overrideComment;
    private String detectedString;
}
