package io.playce.migrator.analysis.prepare;

import io.playce.migrator.config.PlayceMigratorConfig;
import io.playce.migrator.constant.ApplicationType;
import io.playce.migrator.dto.analysis.AnalysisTaskItem;
import io.playce.migrator.prepare.IPrepareService;
import io.playce.migrator.prepare.IScmPrepareService;
import io.playce.migrator.prepare.service.ApplicationPrepareService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
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

@SpringBootTest
@Slf4j
class ApplicationPrepareServiceTest {
    @Autowired
    private Map<String, IPrepareService> prepareServiceMap;
    @Autowired
    private Map<String, IScmPrepareService> scmPrepareServiceMap;
    @Autowired
    private PlayceMigratorConfig playceMigratorConfig;

    @Test
    public void source1OriginPathNull() {
        ApplicationPrepareService service = new ApplicationPrepareService(prepareServiceMap, scmPrepareServiceMap, playceMigratorConfig);

        AnalysisTaskItem app = new AnalysisTaskItem();
        app.setApplicationId(0L);
        app.setApplicationType("ZIP");
        boolean prepared = service.prepareApplication(app);
        assertFalse(prepared);
    }

    @Test
    public void source2OriginPathEmpty() {
        ApplicationPrepareService service = new ApplicationPrepareService(prepareServiceMap, scmPrepareServiceMap, playceMigratorConfig);

        AnalysisTaskItem app = new AnalysisTaskItem();
        app.setApplicationId(0L);
        app.setOriginPath(StringUtils.EMPTY);
        app.setApplicationType("ZIP");
        boolean prepared = service.prepareApplication(app);
        assertFalse(prepared);
    }

    @Test
    public void source3WrongZipName() {
        ApplicationPrepareService service = new ApplicationPrepareService(prepareServiceMap, scmPrepareServiceMap, playceMigratorConfig);

        AnalysisTaskItem app = new AnalysisTaskItem();
        app.setApplicationId(0L);
        app.setApplicationType("ZIP");
        app.setOriginPath("test-files/zip");
        app.setOriginFileName("sampl.zip");
        boolean prepared = service.prepareApplication(app);
        assertFalse(prepared);
    }

    @Test
    public void source4WrongZipName2() {
        ApplicationPrepareService service = new ApplicationPrepareService(prepareServiceMap, scmPrepareServiceMap, playceMigratorConfig);

        AnalysisTaskItem app = new AnalysisTaskItem();
        app.setApplicationId(0L);
        app.setApplicationType("ZIP");
        app.setOriginPath("test-files/zip");
        app.setOriginFileName("sampl.zi");
        boolean prepared = service.prepareApplication(app);
        assertFalse(prepared);
    }

    @Test
    public void binaryJar() {
        ApplicationPrepareService service = new ApplicationPrepareService(prepareServiceMap, scmPrepareServiceMap, playceMigratorConfig);

        AnalysisTaskItem app = new AnalysisTaskItem();
        app.setApplicationId(2L);
        app.setApplicationType("JAR");
        app.setOriginPath("test-files/jar");
        app.setOriginFileName("sample.jar");
        boolean prepared = service.prepareApplication(app);
        assertTrue(prepared);
    }

    @Test
    public void binaryJarInJar() {
        ApplicationPrepareService service = new ApplicationPrepareService(prepareServiceMap, scmPrepareServiceMap, playceMigratorConfig);

        AnalysisTaskItem app = new AnalysisTaskItem();
        app.setApplicationId(3L);
        app.setApplicationType("JAR");
        app.setOriginPath("test-files/jar");
        app.setOriginFileName("sample2.jar");
        boolean prepared = service.prepareApplication(app);
        assertTrue(prepared);
    }

    @Test
    public void binaryWar() {
        ApplicationPrepareService service = new ApplicationPrepareService(prepareServiceMap, scmPrepareServiceMap, playceMigratorConfig);

        AnalysisTaskItem app = new AnalysisTaskItem();
        app.setApplicationId(4L);
        app.setApplicationType("WAR");
        app.setOriginPath("test-files/war");
        app.setOriginFileName("Lab6A.war");
        boolean prepared = service.prepareApplication(app);
        assertTrue(prepared);
    }

    @Test
    public void binaryEar() {
        ApplicationPrepareService service = new ApplicationPrepareService(prepareServiceMap, scmPrepareServiceMap, playceMigratorConfig);

        AnalysisTaskItem app = new AnalysisTaskItem();
        app.setApplicationId(5L);
        app.setApplicationType("EAR");
        app.setOriginPath("test-files/ear");
        app.setOriginFileName("petstore.ear");
        boolean prepared = service.prepareApplication(app);
        assertTrue(prepared);
    }

    @Test
    public void source5Zip() {
        ApplicationPrepareService service = new ApplicationPrepareService(prepareServiceMap, scmPrepareServiceMap, playceMigratorConfig);

        AnalysisTaskItem app = new AnalysisTaskItem();
        app.setApplicationId(1L);
        app.setApplicationType("ZIP");
        app.setOriginPath("test-files/zip");
        app.setOriginFileName("sample.zip");
        boolean prepared = service.prepareApplication(app);
        assertTrue(prepared);
    }

    @Test
    public void source6Zip() {
        ApplicationPrepareService service = new ApplicationPrepareService(prepareServiceMap, scmPrepareServiceMap, playceMigratorConfig);

        AnalysisTaskItem app = new AnalysisTaskItem();
        app.setApplicationId(1L);
        app.setApplicationType("ZIP");
        app.setOriginPath("test-files/zip");
        app.setOriginFileName("msalos-master.20211123.zip");
        boolean prepared = service.prepareApplication(app);
        assertTrue(prepared);
    }

    public void scmGit() throws IOException {
        ApplicationPrepareService service = new ApplicationPrepareService(prepareServiceMap, scmPrepareServiceMap, playceMigratorConfig);

        AnalysisTaskItem app = new AnalysisTaskItem();
        app.setApplicationId(1L);
        app.setApplicationType(ApplicationType.SCM.name());
        app.setOriginPath("git@bitbucket.org:cloud-osci/playce-migrator-mvp.git");
        app.setOriginFileName("dev");
        app.setCredentialUsername("jhkyun@osci.kr");
        app.setCredentialPasswordYn(false);
        app.setCredentialKeyContent(Files.readString(Path.of("/Users/jinhyunkyun/.ssh/id_rsa")));

        boolean prepared = service.prepareApplication(app);
        assertTrue(prepared);
    }
}