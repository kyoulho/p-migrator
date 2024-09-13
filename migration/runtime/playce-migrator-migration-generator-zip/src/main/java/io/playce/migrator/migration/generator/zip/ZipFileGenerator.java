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
 * Dong-Heon Han    Aug 30, 2022		First Draft.
 */

package io.playce.migrator.migration.generator.zip;

import io.playce.migrator.constant.ErrorCode;
import io.playce.migrator.constant.MigratorPath;
import io.playce.migrator.exception.PlayceMigratorException;
import io.playce.migrator.migration.generator.GenerateRequest;
import io.playce.migrator.migration.generator.GeneratorName;
import io.playce.migrator.migration.generator.IResultGenerator;
import io.playce.migrator.util.FileUtil;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.nio.file.FileVisitResult;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.SimpleFileVisitor;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

@Component("ZIP_GEN")
public class ZipFileGenerator implements IResultGenerator {
    @Override
    public boolean isSupported(GenerateRequest request) {
        return true;
    }

    @Override
    public GeneratorName getName() {
        return GeneratorName.ZIP_GEN;
    }

    @Override
    public Path generate(GenerateRequest request) {
        String date = DateFormatUtils.format(System.currentTimeMillis(), "yyyyMMddHHmm");
        String fileName = getFileName(request.getAppPath());
        String formattedFileName = String.format("PlayceMigrator_%s_%s_%s.%s", request.getProjectName(), fileName, date, request.getApplicationType().getMigrationResultExtension());
        Path zipPath = Path.of(request.getAppPath().toString(), MigratorPath.history.path(), String.valueOf(request.getMigrationHistoryId()), formattedFileName);
        FileUtil.removeFile(zipPath);

        FileUtil.createDirectoryIfnotExist(zipPath.getParent());
        try (ZipOutputStream out = new ZipOutputStream(new FileOutputStream(zipPath.toFile()))) {
            Files.walkFileTree(request.getMigrationFiles(), new SimpleFileVisitor<>() {
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    if (attrs.isSymbolicLink()) {
                        return FileVisitResult.CONTINUE;
                    }

                    try (FileInputStream fis = new FileInputStream(file.toFile())) {
                        Path targetFile = request.getMigrationFiles().relativize(file);
                        out.putNextEntry(new ZipEntry(targetFile.toString()));

                        byte[] buffer = new byte[1024];
                        int len;
                        while ((len = fis.read(buffer)) > 0) {
                            out.write(buffer, 0, len);
                        }
                        out.closeEntry();
                    }
                    return FileVisitResult.CONTINUE;
                }
            });
        } catch (IOException e) {
            throw new PlayceMigratorException(ErrorCode.PM373F, e);
        }
        return zipPath;
    }

    private static String getFileName(Path appPath) {
        String name = appPath.getFileName().toString();
        int index = name.lastIndexOf(".");
        if (index != -1) {
            name = name.substring(0, index);
        }
        return name;
    }
}