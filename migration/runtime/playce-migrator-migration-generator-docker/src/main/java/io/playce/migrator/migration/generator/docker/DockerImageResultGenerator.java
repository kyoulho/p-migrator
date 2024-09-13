package io.playce.migrator.migration.generator.docker;

import io.playce.migrator.constant.ErrorCode;
import io.playce.migrator.dto.common.CommandResult;
import io.playce.migrator.exception.PlayceMigratorException;
import io.playce.migrator.migration.generator.GenerateRequest;
import io.playce.migrator.migration.generator.GeneratorName;
import io.playce.migrator.migration.generator.IResultGenerator;
import io.playce.migrator.util.CommandUtil;
import io.playce.migrator.util.FileUtil;
import io.playce.migrator.util.ObjectChecker;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.nio.file.Path;

@Component("DOCKER_IMAGE_GEN")
@Slf4j
public class DockerImageResultGenerator implements IResultGenerator {
    @Override
    public GeneratorName getName() {
        return GeneratorName.DOCKER_IMAGE_GEN;
    }

    @Override
    public Path generate(GenerateRequest request) {
        Path beforeResultPath = request.getBeforeResultPath();
        ObjectChecker.requireNonNull(beforeResultPath, new PlayceMigratorException(ErrorCode.PM201R, "before result path is null"));

        String tagName = request.getTagName();
        ObjectChecker.requireNonNull(tagName, new PlayceMigratorException(ErrorCode.PM201R, "tag name is null"));

        Path workPath = beforeResultPath.getParent();
        String tarName = tagName + ".tar";

        Path resultPath = Path.of(workPath.toString(), tarName);
        FileUtil.removeFile(resultPath);
        CommandResult result = CommandUtil.run("docker build -t " + tagName + " .", workPath, new PlayceMigratorException(ErrorCode.PM705T, "Failed to build docker image."));
        if(result.getExitCode() == 0) {
            result = CommandUtil.run("docker save -o " + tarName + StringUtils.SPACE + tagName,  workPath, new PlayceMigratorException(ErrorCode.PM705T, "Image file extraction failed."));
        }

        if(result.getExitCode() == 0) {
            return resultPath;
        }
        return null;
    }

    @Override
    public boolean isSupported(GenerateRequest request) {
        Path beforeResultPath = request.getBeforeResultPath();
        return request.isContainerizationYn() && beforeResultPath != null && beforeResultPath.toFile().exists();
    }
}
