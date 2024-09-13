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
 * Dong-Heon Han    Sep 01, 2022		First Draft.
 */

package io.playce.migrator.migration.process.util;

import io.playce.migrator.dto.analysis.AnalysisRuleRequest;
import io.playce.migrator.dto.analysis.AnalysisRuleResult;
import io.playce.migrator.dto.migration.MigrationRuleRequest;
import io.playce.migrator.dto.migration.MigrationRuleResult;

public class ResultUtil {
    public static MigrationRuleResult makeMigrationRuleResult(Long ruleId, MigrationRuleRequest request) {
        MigrationRuleResult result = new MigrationRuleResult();
        result.setMigrationRuleId(ruleId);
        result.setLineNumber(request.getStartLineNumber());
        request.setResult(result);
        return result;
    }

    public static String getValueFromKeyword(String content, String keyword) {
        int index = content.indexOf(keyword);
        return content.substring(index, index + keyword.length());
    }

    public static AnalysisRuleResult makeAnalysisRuleResult(Long ruleId, AnalysisRuleRequest request) {
        AnalysisRuleResult result = new AnalysisRuleResult();
        result.setAnalysisRuleId(ruleId);
        result.setLineNumber(request.getStartLineNumber());
        request.setResult(result);
        return result;
    }
}