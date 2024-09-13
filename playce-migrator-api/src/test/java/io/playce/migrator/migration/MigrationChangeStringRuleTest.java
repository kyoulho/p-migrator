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

package io.playce.migrator.migration;

import io.playce.migrator.dto.migration.MigrationRuleRequest;
import io.playce.migrator.dto.migration.MigrationRuleResult;
import io.playce.migrator.rule.loader.RuleSession;
import io.playce.migrator.rule.loader.RuleSessionFactory;
import io.playce.migrator.rule.loader.service.impl.FileRuleLoader;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;


@SpringBootTest
@Slf4j
public class MigrationChangeStringRuleTest {
    @Autowired
    private FileRuleLoader fileRuleLoader;

    @Test
    void stringChangeRuleTest() {
        Map<Long, RuleSessionFactory> migrationRuleMap = fileRuleLoader.getSessionFactoryMapByMigrationIds(List.of(1L));

        MigrationRuleRequest request = new MigrationRuleRequest();
        request.setContent("package io.playce.192.168.4.61.prerequisite.server.impl;");
        Map<String, String> input = new HashMap<>();
        input.put("source", "192.168.4.61");
        input.put("target", "192.168.4.77");
        request.setInput(input);
        request.setStartLineNumber(1);

        RuleSessionFactory factory = migrationRuleMap.get(1L);
        RuleSession ruleSession = factory.newSession();
        ruleSession.execute(request);

        MigrationRuleResult result = request.getResult();
        log.debug("{}", result);
        assertEquals(1, result.getLineNumber());
        assertTrue(result.getContent().contains("192.168.4.77"));
    }
}