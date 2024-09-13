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
 * Author            Date                Description
 * ---------------  ----------------    ------------
 * Jaeeon Bae       9월 26, 2022            First Draft.
 */
package io.playce.migrator.dao.entity;

import io.playce.migrator.dao.converter.BooleanToYNConverter;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;

@Entity
@Table(name = "deprecated_info")
@Getter
@Setter
public class DeprecatedInfo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long deprecatedInfoId;
    private String usedClassName;
    private String deprecatedType;
    private String deprecatedName;
    @Convert(converter = BooleanToYNConverter.class)
    private Boolean forRemoval;
    private Long applicationId;
}