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
 * Dong-Heon Han    Aug 19, 2022		First Draft.
 */

package io.playce.migrator.dao.entity;

import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import javax.persistence.*;

@Entity
@Table(name = "analysis_history_detail")
@Setter @Getter @ToString
public class AnalysisHistoryDetail {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long analysisHistoryDetailId;

    private Long analysisHistoryId;

    private Integer analysisLineNumber;
    private String analysisFilePath;
    private String analysisOriginFilePath;
    private String analysisFileName;
    private String detectedString;
    private String overrideComment;

    private Long analysisRuleId;
}