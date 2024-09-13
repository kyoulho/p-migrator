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

package io.playce.migrator.migration.process.service;

import io.playce.migrator.dao.entity.MigrationRuleLink;
import io.playce.migrator.dao.mapper.RuleLoaderMapper;
import io.playce.migrator.dao.mybatis.MybatisMigrationRuleLink;
import io.playce.migrator.dao.repository.MigrationRuleLinkRepository;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
@Setter @Getter
@Slf4j
@Transactional
public class MigrationRuleLinkService {
    private final RuleLoaderMapper ruleLoaderMapper;
    private final MigrationRuleLinkRepository migrationRuleLinkRepository;

    public void registMigrationRuleLink(Long migrationHistoryId, Long migrationRuleId, String migrationCondition) {
        MigrationRuleLink ruleLink = new MigrationRuleLink();
        ruleLink.setMigrationHistoryId(migrationHistoryId);
        ruleLink.setMigrationRuleId(migrationRuleId);
        ruleLink.setMigrationCondition(migrationCondition);
        migrationRuleLinkRepository.save(ruleLink);
    }

    public List<MybatisMigrationRuleLink> getRuleLinkMapByFileType(Long migrationHistoryId) {
        return ruleLoaderMapper.selectMigrationRuleLinks(migrationHistoryId);
    }
}