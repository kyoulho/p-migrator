package io.playce.migrator.migration.process.service;

import io.playce.migrator.dto.migration.MigrationTaskItem;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
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
 * Dong-Heon Han    Aug 29, 2022		First Draft.
 */

@SpringBootTest
@Slf4j
class MigrationProcessServiceTest {
    @Autowired
    private MigrationProcessService migrationProcessService;

    @Test
    void testProcess() {
        MigrationTaskItem item = new MigrationTaskItem();
        item.setOriginFileName("sample.zip");
        item.setApplicationType("ZIP");
        item.setApplicationId(1L);
        item.setMigrationHistoryId(1L);
        item.setTargetId(1L);
        try {
            migrationProcessService.process(item);
        } catch (RuntimeException e) {
            log.error(e.getMessage(), e);
        }
    }
}