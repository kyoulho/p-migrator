package io.playce.migrator.migration.generator.docker;

import io.playce.migrator.constant.ErrorCode;
import io.playce.migrator.constant.MigratorPath;
import io.playce.migrator.exception.PlayceMigratorException;
import io.playce.migrator.migration.generator.GenerateRequest;
import io.playce.migrator.migration.generator.GeneratorName;
import io.playce.migrator.migration.generator.IResultGenerator;
import io.playce.migrator.util.FileUtil;
import io.playce.migrator.util.ObjectChecker;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Path;

@Component("DOCKERFILE_GEN")
@Slf4j
public class DockerfileResultGenerator implements IResultGenerator {
    @Override
    public GeneratorName getName() {
        return GeneratorName.DOCKERFILE_GEN;
    }

    @Override
    public Path generate(GenerateRequest request) {
        log.debug("start dockerfile generator. {}", request.getMigrationHistoryId());
        Path beforeResultPath = request.getBeforeResultPath();
        ObjectChecker.requireNonNull(beforeResultPath, new PlayceMigratorException(ErrorCode.PM201R, "before result path is null"));

        Path dockerfilePath = Path.of(request.getAppPath().toString(), MigratorPath.history.path(), String.valueOf(request.getMigrationHistoryId()), "Dockerfile");
        createFile(dockerfilePath, request);
        log.debug("end dockerfile generator.");
        return dockerfilePath;
    }

    private void createFile(Path dockerfilePath, GenerateRequest request) {
        String baseImageName = request.getBaseImageName();
        ObjectChecker.requireNonNull(baseImageName, new PlayceMigratorException(ErrorCode.PM107H, "Base image name is missing."));

        String dockerFileConfig = request.getDockerFileConfig();
        dockerFileConfig = dockerFileConfig == null ? StringUtils.EMPTY: dockerFileConfig;

        FileUtil.removeFile(dockerfilePath);
        try(FileWriter out = new FileWriter(dockerfilePath.toFile())) {
            String sb =
                    "FROM" + StringUtils.SPACE + baseImageName + StringUtils.LF +
                            dockerFileConfig + StringUtils.LF +
                    "COPY" + StringUtils.SPACE + "./" + request.getBeforeResultPath().getFileName().toString() + StringUtils.SPACE + "/usr/local/tomcat/webapps/ROOT.war" + StringUtils.LF +
                    "EXPOSE" + StringUtils.SPACE + "8080" + StringUtils.LF +
                    "CMD" + StringUtils.SPACE + "[\"catalina.sh\", \"run\"]" + StringUtils.LF;
            out.write(sb);
        } catch (IOException e) {
            throw new PlayceMigratorException(ErrorCode.PM201R, "before result path is null");
        }
    }

    @Override
    public boolean isSupported(GenerateRequest request) {
        return request.isContainerizationYn();
    }
}
