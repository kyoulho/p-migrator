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

package io.playce.migrator.prepare.zip.service;

import io.playce.migrator.constant.ApplicationType;
import io.playce.migrator.prepare.AbstractPrepareService;
import io.playce.migrator.prepare.zip.component.DecompileComponent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.nio.file.Path;

import static io.playce.migrator.constant.MigratorPath.decompile;
import static io.playce.migrator.constant.MigratorPath.unzip;

@Service("JAR")
@RequiredArgsConstructor
@Slf4j
public class BinaryJarService extends AbstractPrepareService {
    private final DecompileComponent decompileComponent;

    @Override
    public void execute(Path sourceFile, Path targetDir, String appType) {
        log.debug("start {}-service from-path: {}", appType, sourceFile);
        ApplicationType type = ApplicationType.valueOf(appType);

        Path unzipDir = Path.of(targetDir.toString(), unzip.path());
        Path decompressDir = Path.of(targetDir.toString(), decompile.path());
        decompileComponent.processSourceFile(sourceFile, unzipDir, decompressDir);
        decompileComponent.processSubJarFiles(targetDir, unzip.path(), decompile.path());
        log.debug("  end {}-service   to-path: {}", type, targetDir);
    }

}