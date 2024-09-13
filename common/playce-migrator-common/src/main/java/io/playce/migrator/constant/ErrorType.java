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
 * Dong-Heon Han    Aug 20, 2022		First Draft.
 */

package io.playce.migrator.constant;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum ErrorType {
    H("http"),
    R("resource"),
    F("file"),
    D("db"),
    A("auth"),
    J("json"),
    T("task"),
    S("scm"),
    SUB("subscription"),
    ;

    private final String value;

    public static String messageCode(ErrorCode errorCode) {
        String strCode = errorCode.name();
        String msgCode = strCode.substring(0, 5);
        String strMsgType = strCode.substring(5);
        ErrorType errorType = ErrorType.valueOf(strMsgType);

        return "error." + errorType.getValue() + "." + msgCode.toLowerCase();
    }
}