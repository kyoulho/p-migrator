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

import io.playce.migrator.constant.ErrorCode;
import io.playce.migrator.dao.entity.MigrationRule;
import io.playce.migrator.dao.repository.MigrationRuleRepository;
import io.playce.migrator.dto.migration.MigrationRuleResponse;
import io.playce.migrator.exception.PlayceMigratorException;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Setter
@Getter
@Slf4j
public class MigrationRuleService {
    private final MigrationRuleRepository migrationRuleRepository;

    private final ModelMapper modelMapper;

    public MigrationRuleResponse getMigrationRule(Long migrationRuleId) {
        MigrationRule migrationRule = migrationRuleRepository.findById(migrationRuleId).orElseThrow(() ->
                new PlayceMigratorException(ErrorCode.PM201R, "Migration rule does not exits. id: " + migrationRuleId));
        return modelMapper.map(migrationRule, MigrationRuleResponse.class);
    }

    public List<MigrationRuleResponse> getMigrationRules() {
        return migrationRuleRepository.findAll().stream()
                .sorted(Comparator.comparing(MigrationRule::getMigrationRuleId).reversed())
                .map(migrationRule -> modelMapper.map(migrationRule, MigrationRuleResponse.class))
                .collect(Collectors.toList());
    }

}