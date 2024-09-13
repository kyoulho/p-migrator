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

package io.playce.migrator.dao.entity;

import io.playce.migrator.dao.converter.BooleanToYNConverter;
import lombok.Getter;
import lombok.Setter;
import org.hibernate.annotations.ColumnDefault;
import org.hibernate.annotations.Where;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "analysis_rule")
@Setter @Getter
@Where(clause = "delete_yn='N'")
public class AnalysisRule {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long analysisRuleId;

    private String analysisRuleName;
    private String importance;
    private String analysisRuleContent;
    private String comment;
    private String todo;
    private String link;
    private String description;

    @ManyToOne
    @JoinColumn(name = "analysis_processing_policy_id")
    private AnalysisProcessingPolicy analysisProcessingPolicy;

    private String registLoginId;
    private Instant registDatetime;
    private String modifyLoginId;
    private Instant modifyDatetime;

    @ColumnDefault("N")
    @Convert(converter = BooleanToYNConverter.class)
    private Boolean deleteYn;
}