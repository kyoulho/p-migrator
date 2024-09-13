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

package io.playce.migrator.rule.loader.service.impl;

import io.playce.migrator.constant.ErrorCode;
import io.playce.migrator.constant.FileType;
import io.playce.migrator.dao.entity.AnalysisProcessingPolicy;
import io.playce.migrator.dao.entity.AnalysisRule;
import io.playce.migrator.dao.entity.MigrationRule;
import io.playce.migrator.dao.entity.Target;
import io.playce.migrator.dao.mapper.RuleLoaderMapper;
import io.playce.migrator.dao.repository.AnalysisRuleRepository;
import io.playce.migrator.dao.repository.MigrationRuleRepository;
import io.playce.migrator.dao.repository.TargetRepository;
import io.playce.migrator.exception.PlayceMigratorException;
import io.playce.migrator.rule.loader.RuleSessionFactory;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.kie.api.io.Resource;
import org.kie.api.io.ResourceType;
import org.kie.internal.builder.KnowledgeBuilder;
import org.kie.internal.builder.KnowledgeBuilderFactory;
import org.kie.internal.io.ResourceFactory;
import org.springframework.stereotype.Service;

import java.io.StringReader;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Service("databaseRuleLoader")
@RequiredArgsConstructor
@Slf4j
public class DatabaseRuleLoader extends AbstractRuleLoader {
    private final RuleLoaderMapper ruleLoaderMapper;
    private final TargetRepository targetRepository;
    private final AnalysisRuleRepository analysisRuleRepository;
    private final MigrationRuleRepository migrationRuleRepository;

    @Override
    public RuleSessionFactory createNewSessionFactory(Long targetId, FileType fileType) {
        Target target = getTarget(targetId);
        List<AnalysisProcessingPolicy> groups = ruleLoaderMapper.selectAnalysisProcessingPolicies(targetId, fileType.name());
        for (AnalysisProcessingPolicy group : groups) {
            return createNewSessionAnalysisFactory(target, group);
        }
        return null;
    }

    private RuleSessionFactory createNewSessionAnalysisFactory(Target target, AnalysisProcessingPolicy group) {
        KnowledgeBuilder builder = KnowledgeBuilderFactory.newKnowledgeBuilder();

        List<AnalysisRule> rules = analysisRuleRepository.findByAnalysisProcessingPolicy(group);
        for (AnalysisRule rule : rules) {
            String content = rule.getAnalysisRuleContent();
            content = setRuleId(content, rule.getAnalysisRuleId());
            Resource resource = ResourceFactory.newReaderResource(new StringReader(content));
            builder.add(resource, ResourceType.DRL);
            log.trace("load rule [{}]", rule.getAnalysisRuleName());
        }
        return createRuleSessionFactory(builder, "check target-id: " + target.getTargetId() + ", file-type: " + group.getProcessingType());
    }

    @Override
    public Map<FileType, RuleSessionFactory> getSessionFactoryMapByTargetId(Long targetId) {
        Target target = getTarget(targetId);
        List<AnalysisProcessingPolicy> groups = ruleLoaderMapper.selectAnalysisProcessingPolicies(targetId, null);

        Map<FileType, RuleSessionFactory> result = new HashMap<>();
        for (AnalysisProcessingPolicy group : groups) {
            FileType fileType = FileType.valueOf(group.getProcessingType());
            RuleSessionFactory factory = createNewSessionAnalysisFactory(target, group);
            result.put(fileType, factory);
        }
        return result;
    }

    @Override
    public Map<Long, RuleSessionFactory> getSessionFactoryMapByMigrationIds(List<Long> migrationRuleIds) {
        Map<Long, RuleSessionFactory> result = new HashMap<>();

        for (Long migrationRuleId : migrationRuleIds) {
            if (result.containsKey(migrationRuleId)) continue;

            Optional<MigrationRule> optionalMigrationRule = migrationRuleRepository.findById(migrationRuleId);
            if (optionalMigrationRule.isPresent()) {
                MigrationRule migrationRule = optionalMigrationRule.get();
                RuleSessionFactory factory = createNewSessionMigrationFactory(migrationRule);
                result.put(migrationRuleId, factory);
            } else {
                log.error("Migration rules do not exist. migrationRuleId: {}", migrationRuleId);
            }
        }
        return result;
    }

    private RuleSessionFactory createNewSessionMigrationFactory(MigrationRule rule) {
        KnowledgeBuilder builder = KnowledgeBuilderFactory.newKnowledgeBuilder();

        String content = rule.getMigrationRuleContent();
        content = setRuleId(content, rule.getMigrationRuleId());
        Resource resource = ResourceFactory.newReaderResource(new StringReader(content));
        builder.add(resource, ResourceType.DRL);

        return createRuleSessionFactory(builder, "check migrationRuleId: " + rule.getMigrationRuleId());
    }

    private Target getTarget(Long targetId) {
        Optional<Target> optionalTarget = targetRepository.findById(targetId);
        if (optionalTarget.isEmpty()) {
            throw new PlayceMigratorException(ErrorCode.PM201R, "targetId: " + targetId);
        }
        return optionalTarget.get();
    }
}