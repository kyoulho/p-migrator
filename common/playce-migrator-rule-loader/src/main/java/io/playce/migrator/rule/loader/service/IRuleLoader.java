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
 * Dong-Heon Han    Aug 08, 2022		First Draft.
 */

package io.playce.migrator.rule.loader.service;

import io.playce.migrator.constant.FileType;
import io.playce.migrator.rule.loader.RuleSessionFactory;

import java.util.List;
import java.util.Map;

public interface IRuleLoader {
    RuleSessionFactory getSessionFactory(Long targetId, FileType fileType);

    Map<FileType, RuleSessionFactory> getSessionFactoryMapByTargetId(Long targetId);
    Map<Long, RuleSessionFactory> getSessionFactoryMapByMigrationIds(List<Long> migrationRuleIds);
}