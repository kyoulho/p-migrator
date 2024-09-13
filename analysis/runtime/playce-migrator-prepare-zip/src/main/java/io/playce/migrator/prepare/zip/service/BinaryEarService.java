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
 * Dong-Heon Han    Aug 14, 2022		First Draft.
 */

package io.playce.migrator.prepare.zip.service;

import io.playce.migrator.constant.ApplicationType;
import io.playce.migrator.prepare.AbstractPrepareService;
import io.playce.migrator.prepare.zip.component.UnzipComponent;
import io.playce.migrator.util.PathUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.playce.migrator.constant.MigratorPath.unzip;

@Service("EAR")
@RequiredArgsConstructor
@Slf4j
public class BinaryEarService extends AbstractPrepareService {
    private final UnzipComponent unzipComponent;
    private final BinaryWarService binaryWarService;

    @Override
    public void execute(Path sourceFile, Path targetDir, String appType) {
        log.debug("start {}-service from-path: {}", appType, sourceFile);
        ApplicationType type = ApplicationType.valueOf(appType);
        Path unzipDir = Path.of(targetDir.toString(), unzip.path());
        unzipComponent.decompress(sourceFile, unzipDir);

        processSubFiles(targetDir, "jar", "sub-jars");
        processSubFiles(targetDir, "war", "sub-wars");
        log.debug("  end {}-service   to-path: {}", type, targetDir);
    }

    private void processSubFiles(Path targetDir, String fileType, String deploy) {
        try(Stream<Path> stream = Files.walk(targetDir)) {
            List<Path> files = stream.filter(Files::isRegularFile).filter(f ->
                            f.getFileName().toString().endsWith(fileType)).collect(Collectors.toList());
            for(Path file: files) {
                Path newTargetDir = Path.of(targetDir.toString(), deploy, PathUtils.getFileName(file));
                binaryWarService.execute(file, newTargetDir, fileType.toUpperCase());
            }
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}