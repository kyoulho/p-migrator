package io.playce.migrator.migration.generator.maven;

import io.playce.migrator.constant.ApplicationType;
import io.playce.migrator.constant.ErrorCode;
import io.playce.migrator.exception.PlayceMigratorException;
import io.playce.migrator.migration.generator.GenerateRequest;
import io.playce.migrator.migration.generator.GeneratorName;
import io.playce.migrator.migration.generator.IResultGenerator;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.io.FileUtils;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component("BUILD_GEN")
@Slf4j
public class BuildResultGenerator implements IResultGenerator {
    @Override
    public boolean isSupported(GenerateRequest request) {
        return request.getApplicationType() == ApplicationType.ZIP || request.getApplicationType() == ApplicationType.SCM;
    }

    @Override
    public GeneratorName getName() {
        return GeneratorName.BUILD_GEN;
    }

    @Override
    public Path generate(GenerateRequest request) {
        Path pomPath = getPomPath(request.getMigrationFiles());
        mvnPackage(pomPath);
        Path destFile = copyWarFile(request);
        mvnClean(pomPath);
        return destFile;
    }

    private static Path getPomPath(Path migrationFiles) {
        try (Stream<Path> stream = Files.walk(migrationFiles)) {
            return stream
                    .filter(Files::isRegularFile)
                    .filter(path -> path.toString().contains("pom.xml"))
                    .min((p1, p2) -> {
                        long count1 = p1.toString().chars().filter(c -> c == File.separatorChar).count();
                        long count2 = p2.toString().chars().filter(c -> c == File.separatorChar).count();
                        return (int) (count1 - count2);
                    })
                    .orElseThrow(() -> new PlayceMigratorException(ErrorCode.PM704T));
        } catch (IOException e) {
            throw new PlayceMigratorException(ErrorCode.PM371F, e);
        }
    }

    private void mvnPackage(Path pomPath) {
        DefaultExecutor executor = new DefaultExecutor();
        executor.setWorkingDirectory(pomPath.getParent().toFile());

        CommandLine commandLine = CommandLine.parse(System.getProperty("user.dir") + "/maven/bin/mvn clean package");
        log.info("Execute Command [{}]", commandLine);
        try {
            executor.execute(commandLine);
        } catch (IOException e) {
            throw new PlayceMigratorException(ErrorCode.PM705T, "Failed packaging.", e);
        }
    }

    private Path copyWarFile(GenerateRequest request) {
        try (Stream<Path> stream = Files.walk(request.getMigrationFiles())) {
            List<Path> paths = stream.filter(Files::isRegularFile)
                    .filter(path -> path.toString().endsWith(".war"))
                    .collect(Collectors.toList());

            if (paths.size() < 1) {
                throw new PlayceMigratorException(ErrorCode.PM706T);
            } else if (paths.size() > 1) {
                throw new PlayceMigratorException(ErrorCode.PM707T);
            }

            Path binaryFilePath = paths.get(0);
            Path destFile = Path.of(request.getAppPath().toString(), "migration-histories", request.getMigrationHistoryId().toString(), binaryFilePath.getFileName().toString());
            FileUtils.copyFile(binaryFilePath.toFile(), destFile.toFile());

            return destFile;
        } catch (IOException e) {
            throw new PlayceMigratorException(ErrorCode.PM371F, e);
        }
    }

    private void mvnClean(Path pomPath) {
        DefaultExecutor executor = new DefaultExecutor();
        executor.setWorkingDirectory(pomPath.getParent().toFile());

        CommandLine commandLine = CommandLine.parse(System.getProperty("user.dir") + "/maven/bin/mvn clean");
        log.info("Execute Command [{}]", commandLine);
        try {
            executor.execute(commandLine);
        } catch (IOException e) {
            throw new PlayceMigratorException(ErrorCode.PM705T, "Maven clean failed.", e);
        }
    }
}
