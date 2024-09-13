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
 * Dong-Heon Han    Aug 12, 2022		First Draft.
 */

package io.playce.migrator.prepare.zip.component;

import io.playce.migrator.constant.ErrorCode;
import io.playce.migrator.exception.PlayceMigratorException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.zip.ZipEntry;
import java.util.zip.ZipInputStream;

@Component
@Slf4j
public class UnzipComponent {
    public void decompress(Path sourceFile, Path targetDir) {
        try (ZipInputStream zis = new ZipInputStream(Files.newInputStream(sourceFile))) {
            ZipEntry zipEntry = zis.getNextEntry();
            while (zipEntry != null) {
                unzipFile(targetDir, zis, zipEntry);
                zipEntry = zis.getNextEntry();
            }
            zis.closeEntry();
        } catch (IOException | RuntimeException e) {
            if(e instanceof PlayceMigratorException) throw (PlayceMigratorException) e;
            throw new PlayceMigratorException(ErrorCode.PM308F, "Unzip failed.. " + e.getMessage(), e);
        }
    }

    private void unzipFile(Path targetDir, ZipInputStream zis, ZipEntry zipEntry) throws IOException {
        boolean isDirectory = zipEntry.getName().endsWith(File.separator);
        Path newPath = zipSlipProtect(zipEntry, targetDir);

        if (isDirectory) {
            Files.createDirectories(newPath);
        } else {
            if (newPath.getParent() != null && Files.notExists(newPath.getParent())) {
                Files.createDirectories(newPath.getParent());
            }
            Files.copy(zis, newPath, StandardCopyOption.REPLACE_EXISTING);
        }
    }

    private Path zipSlipProtect(ZipEntry zipEntry, Path targetDir) {
        Path targetDirResolved = targetDir.resolve(zipEntry.getName());
        Path normalizePath = targetDirResolved.normalize();
        if (!normalizePath.startsWith(targetDir)) {
            throw new PlayceMigratorException(ErrorCode.PM308F, "Bad zip entry: " + zipEntry.getName());
        }
        return normalizePath;
    }
}