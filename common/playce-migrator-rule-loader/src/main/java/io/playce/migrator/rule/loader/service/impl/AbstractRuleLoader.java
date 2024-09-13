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

import io.playce.migrator.constant.FileType;
import io.playce.migrator.rule.loader.RuleSessionFactory;
import io.playce.migrator.rule.loader.service.IRuleLoader;
import lombok.extern.slf4j.Slf4j;
import org.kie.api.KieBase;
import org.kie.internal.builder.KnowledgeBuilder;
import org.kie.internal.builder.KnowledgeBuilderFactory;

@Slf4j
public abstract class AbstractRuleLoader implements IRuleLoader {
//    private final Map<String, RuleSessionFactory> sessionFactoryMap = new ConcurrentHashMap<>();
//    private final Object lock = new Object();

    @Override
    public RuleSessionFactory getSessionFactory(Long targetId, FileType fileType) {
        return createNewSessionFactory(targetId, fileType);
//        String key = fileType.getKey(targetId);
//        if(!sessionFactoryMap.containsKey(key)) {
//            RuleSessionFactory ruleSessionFactory = createNewSessionFactory(targetId, fileType);
//            synchronized (lock) {
//                sessionFactoryMap.put(key, ruleSessionFactory);
//            }
//        }
//
//        RuleSessionFactory ruleSessionFactory;
//        synchronized (lock) {
//            ruleSessionFactory = sessionFactoryMap.get(key);
//        }
//
//        return ruleSessionFactory;
    }

    public String setRuleId(String content, Long ruleId) {
        return content.replace("${ruleId}", ruleId + "L");
    }

    public RuleSessionFactory createRuleSessionFactory(KnowledgeBuilder builder, String errorMessage) {
        KieBase kieBase;
        try {
            kieBase = builder.newKieBase();
        } catch (IllegalArgumentException e) {
            log.error("Rule content error, {}", errorMessage);
            kieBase = KnowledgeBuilderFactory.newKnowledgeBuilder().newKieBase();
        }
        return new RuleSessionFactory(kieBase);
    }

    public abstract RuleSessionFactory createNewSessionFactory(Long targetId, FileType fileType);
}