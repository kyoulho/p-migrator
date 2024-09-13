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

package io.playce.migrator.prepare;

import lombok.extern.slf4j.Slf4j;

import java.nio.file.Path;

import static io.playce.migrator.constant.MigratorPath.unzip;

@Slf4j
public abstract class AbstractPrepareService implements IPrepareService {
    public static Path getApplicationDir(String workDir, Long applicationId, String originFileName) {
        return Path.of(workDir, String.valueOf(applicationId), originFileName);
    }

    public static Path getTargetUnzipDir(Path targetDir) {
        return Path.of(targetDir.toString(), unzip.path());
    }
}