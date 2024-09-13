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

package io.playce.migrator.config;


import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

import java.nio.file.Path;

@ConfigurationProperties(prefix = "playce-migrator")
@Component
@Getter @Setter @ToString
public class PlayceMigratorConfig {
    private String version;
    private String workDir;
    private String originDir;
    private String oauthDbName;

    public Path getWorkDirWithApp(Long applicationId) {
        return Path.of(workDir, String.valueOf(applicationId));
    }
    public Path getOriginDirWithApp(Long applicationId) {
        return Path.of(originDir, String.valueOf(applicationId));
    }

    public Path getAppPath(String originFileName, Long applicationId) {
        Path workDir = getWorkDirWithApp(applicationId);
        return Path.of(workDir.toString(), originFileName);
    }
}