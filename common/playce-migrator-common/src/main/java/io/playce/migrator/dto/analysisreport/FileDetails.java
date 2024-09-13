package io.playce.migrator.dto.analysisreport;


import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class FileDetails {
    private String sha1Hash;
    private String foundAtPath;
    private String description;
}
