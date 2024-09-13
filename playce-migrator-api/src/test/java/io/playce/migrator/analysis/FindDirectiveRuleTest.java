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
 * Dong-Heon Han    Aug 10, 2022		First Draft.
 */

package io.playce.migrator.analysis;

import io.playce.migrator.constant.FileType;
import io.playce.migrator.dto.analysis.AnalysisRuleRequest;
import io.playce.migrator.dto.analysis.AnalysisRuleResult;
import io.playce.migrator.rule.loader.RuleSession;
import io.playce.migrator.rule.loader.RuleSessionFactory;
import io.playce.migrator.rule.loader.service.impl.DatabaseRuleLoader;
import io.playce.migrator.rule.loader.service.impl.FileRuleLoader;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

/**
 * <pre>
 *
 * </pre>
 *
 * @author Dong-Heon Han
 * @version 3.0
 */
@SpringBootTest
@Slf4j
public class FindDirectiveRuleTest {
    @Autowired
    private FileRuleLoader fileRuleLoader;
    @Autowired
    private DatabaseRuleLoader databaseRuleLoader;

    @Test
    void testFileRuleLoader() {
        RuleSessionFactory sessionFactory = fileRuleLoader.getSessionFactory(1L, FileType.JSP);
        RuleSession ruleSession = sessionFactory.newSession();

        AnalysisRuleRequest request = new AnalysisRuleRequest();
        request.setContent("<%@ page attribute=\"value\" %>");
        ruleSession.execute(request);

        AnalysisRuleResult result = request.getResult();

        String detectedString = result.getDetectedString();
        log.debug("{}", detectedString);
        assertEquals("<%@ page attribute=\"value\" %>", detectedString);
    }

    @Test
    void testDatabaseRuleLoader() {
        RuleSessionFactory sessionFactory = databaseRuleLoader.getSessionFactory(1L, FileType.JSP);
        RuleSession ruleSession = sessionFactory.newSession();

        AnalysisRuleRequest request = new AnalysisRuleRequest();
        request.setContent("<%@ page attribute=\"value\" %>");
        ruleSession.execute(request);

        AnalysisRuleResult result = request.getResult();

        String detectedString = result.getDetectedString();
        log.debug("{}", detectedString);
        assertEquals("<%@ page attribute=\"value\" %>", detectedString);
    }
}