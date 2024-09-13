package io.playce.migrator.migration.generator;

import io.playce.migrator.constant.ApplicationType;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.nio.file.Path;

@Getter
@Setter
@ToString
public class GenerateRequest {
//  /work-dir/어플 아이디/어플 파일명의 디렉토리
    private Path appPath;
    private Path beforeResultPath;
    private String projectName;
    private Long migrationHistoryId;
    private ApplicationType applicationType;
    private boolean containerizationYn;
//  /appPath/migration/unzip 디렉토리
    private Path migrationFiles;

    private String baseImageName;
    private String dockerFileConfig;
    private String tagName;
}
