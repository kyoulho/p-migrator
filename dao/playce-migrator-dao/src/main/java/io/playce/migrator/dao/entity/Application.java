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
 * Jaeeon Bae       8월 19, 2022            First Draft.
 */
package io.playce.migrator.dao.entity;

import io.playce.migrator.dao.converter.BooleanToYNConverter;
import lombok.Getter;
import lombok.Setter;

import javax.persistence.*;
import java.time.Instant;

@Entity
@Table(name = "application")
@Getter
@Setter
public class Application {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long applicationId;

    @Column
    private Long targetId;

    @Column
    private Long projectId;

    @Column
    private Long credentialId;

    @Column
    private String applicationName;

    @Column
    private String originPath;

    @Column
    private String originFileName;

    @Column
    private Long originFileSize;

    @Column
    private String description;

    @Column
    @Convert(converter = BooleanToYNConverter.class)
    private Boolean scmYn;

    @Column
    private String applicationType;

    @Column
    private String middlewareType;

    @Column
    private String javaVersion;

    @Column
    private String registLoginId;

    @Column
    private Instant registDatetime;

    @Column
    private String modifyLoginId;

    @Column
    private Instant modifyDatetime;

}