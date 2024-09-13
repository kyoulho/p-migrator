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
 * Dong-Heon Han    Aug 11, 2022		First Draft.
 */

package io.playce.migrator.dto.analysis;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.playce.migrator.util.JacksonObjectMapperUtil;
import io.playce.migrator.util.StringNullChecker;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Setter @Getter
@ToString(exclude = {"credentialPassword", "credentialKeyContent"})
public class AnalysisTaskItem {
    private Long projectId;
    private Long applicationId;
    private String applicationType;
    private Long targetId;
    private String javaVersion; // target java version
    private String originPath;
    private String originFileName;

    private Boolean credentialPasswordYn;
    private String credentialUsername;
    private String credentialPassword;
    private String credentialKeyContent;

    private Long analysisHistoryId;
    private String selectedLibraryNames;

    @Setter(AccessLevel.NONE)
    @Getter(AccessLevel.NONE)
    private List<String> selectedLibraries;
    public List<String> getSelectedLibraries(ObjectMapper objectMapper) {
        if(selectedLibraries == null) {
            if(StringNullChecker.isEmpty(selectedLibraryNames)) {
                return new ArrayList<>();
            }
            this.selectedLibraries = JacksonObjectMapperUtil.getObject(objectMapper, selectedLibraryNames);
        }

        return selectedLibraries;
    }
}