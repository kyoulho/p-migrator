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
 * Jaeeon Bae       8월 18, 2022            First Draft.
 */
package io.playce.migrator.dto.application;

import lombok.Getter;
import lombok.Setter;
import org.hibernate.validator.constraints.ScriptAssert;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@ScriptAssert(lang = "javascript",
        script = "(_this.scmYn == true &&_this.credentialId !== null && _this.scmUrl !== null && _this.branchName !== null)" +
                " || (_this.scmYn == false && _this.orginPath !== null && _this.orginFileName !== null)")
public class ApplicationRequest {
    private String originPath;
    private String originFileName;
    private int originFileSize;

    private String scmUrl;
    private String branchName;

    private String description;
    private boolean projectTargetUseYn;
    private boolean scmYn;

    // source
    @NotEmpty
    private String middlewareType;
    @NotEmpty
    private String javaVersion;

    // credential
    private Long credentialId;

    // target
    @NotNull
    private Long targetId;

}