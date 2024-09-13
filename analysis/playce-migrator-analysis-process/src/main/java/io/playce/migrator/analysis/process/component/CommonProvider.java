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
 * Dong-Heon Han    Sep 21, 2022		First Draft.
 */

package io.playce.migrator.analysis.process.component;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.stereotype.Component;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.attribute.PosixFilePermission;
import java.util.HashSet;
import java.util.Set;

@Component
@RequiredArgsConstructor
@Slf4j
public class CommonProvider implements InitializingBean {
    private static File jdeprscanFile;
    private static File jdepsFile;

    public static File getJdeprscanFile() {
        return jdeprscanFile;
    }

    public static File getJdepsFile() {
        return jdepsFile;
    }

    @Override
    public void afterPropertiesSet() throws Exception {
        String tmpDir = System.getProperty("java.io.tmpdir");
        jdeprscanFile = new File(tmpDir, "scripts/jdeprscan.sh");
        jdepsFile = new File(tmpDir, "scripts/jdeps.sh");

        try {
            FileUtils.copyURLToFile(CommonProvider.class.getClassLoader().getResource("scripts/jdeprscan.sh"), jdeprscanFile);
            FileUtils.copyURLToFile(CommonProvider.class.getClassLoader().getResource("scripts/jdeps.sh"), jdepsFile);

            Set<PosixFilePermission> perms = new HashSet<>();
            perms.add(PosixFilePermission.OWNER_READ);
            perms.add(PosixFilePermission.OWNER_WRITE);
            perms.add(PosixFilePermission.OWNER_EXECUTE);
            perms.add(PosixFilePermission.GROUP_READ);
            perms.add(PosixFilePermission.GROUP_EXECUTE);
            perms.add(PosixFilePermission.OTHERS_READ);
            perms.add(PosixFilePermission.OTHERS_EXECUTE);

            Files.setPosixFilePermissions(jdeprscanFile.toPath(), perms);
            Files.setPosixFilePermissions(jdepsFile.toPath(), perms);

            log.debug("jdeprscan scripts file copied to " + jdeprscanFile.getParentFile().getAbsolutePath());
            log.debug("jdeps scripts file copied to " + jdepsFile.getParentFile().getAbsolutePath());
        } catch (IOException e) {
            log.error("Unhandled exception occurred while copy application assessment scripts.", e);
        }
    }

    public String parseJavaVersion(String targetJavaVersion) {
        Integer version;
        try {
            version = Integer.parseInt(targetJavaVersion.substring(3));
        } catch (NumberFormatException e) {
            version = Integer.parseInt(targetJavaVersion.substring(3, 4));
        }

        return String.valueOf(version);
    }
}