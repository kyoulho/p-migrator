/*
 * Copyright 2022 The playce-migrator-mvp Project.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Revision History
 * Author			Date				Description
 * ---------------	----------------	------------
 * Dong-Heon Han    Aug 11, 2022		First Draft.
 */

package io.playce.migrator.util;

import io.playce.migrator.constant.ErrorCode;
import io.playce.migrator.constant.FileType;
import io.playce.migrator.constant.LibType;
import io.playce.migrator.constant.MigratorPath;
import io.playce.migrator.exception.PlayceMigratorException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Objects;
import java.util.stream.Stream;

@Slf4j
public abstract class PathUtils {
    public static String getExtension(Path filePath) {
        String fileName = filePath.getFileName().toString();
        int index = fileName.lastIndexOf(".");
        if (index == -1) {
            return null;
        }

        return fileName.substring(index + 1);
    }

    public static String getFileName(Path filePath) {
        return filePath.getFileName().toString();
    }

    public static void removeTargetDir(Path newTargetDir) {
        File targetDir = newTargetDir.toFile();
        if (targetDir.exists()) {
            log.debug("target directory is exists: {}", targetDir.getAbsolutePath());
            try {
                FileUtils.forceDelete(targetDir);
            } catch (IOException e) {
                log.error("Failed to delete target directory: {}", targetDir.getAbsolutePath());
                throw new PlayceMigratorException(ErrorCode.PM307F, "Failed to delete target directory", e);
            }
        }
    }

    public static String getExtensionUpperCase(Path sourceFile) {
        return Objects.requireNonNull(getExtension(sourceFile)).toUpperCase();
    }

    public static Path getSubLibrariesPath(Path appPath, String libraryName) {
        String app = appPath.toString();
        //jar
        if (libraryName.toLowerCase().endsWith(LibType.JAR.getLower())) {
            return Path.of(app, MigratorPath.subjars.path(), libraryName);
        }
        //war
        return Path.of(app, MigratorPath.subwars.path(), libraryName);
    }

    public static Path getTargetFilePath(Path appPath, Path migrationPath, Path sourceFilePath) {
        String sourceFile = sourceFilePath.toString();
        String app = appPath.toString();
        String migration = migrationPath.toString();

        return Path.of(sourceFile.replace(app, migration));
    }

    public static Stream<Path> getWorkStream(FileType type, Path work) throws IOException {
        if(type == FileType.CLASS) {
            Path path = Path.of(work.toString(), MigratorPath.decompile.path());
            if(!path.toFile().exists()) {
                return Stream.of();
            }
            return Files.walk(path);
        }
        return Files.walk(Path.of(work.toString(), MigratorPath.unzip.path()));
    }

    public static Long getFileCount(Path appPath) throws IOException {
        try(Stream<Path> stream = Files.walk(appPath)) {
            return stream.filter(Files::isRegularFile).filter(f -> FileType.check(f.getFileName())).count();
        }
    }
}