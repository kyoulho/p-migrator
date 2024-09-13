/*
 * Copyright 2020 The Playce-RoRo Project.
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
 * Sang-cheon Park	2020. 3. 30.		First Draft.
 */
package io.playce.migrator.util;

import io.playce.migrator.constant.ErrorCode;
import io.playce.migrator.dto.authentication.SecurityUser;
import io.playce.migrator.exception.PlayceMigratorException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;

import static io.playce.migrator.util.ObjectChecker.requireNonNull;
import static io.playce.migrator.util.ObjectChecker.requireNotSameClass;

@Component
public class LoginUserUtil {

    public SecurityUser getLoginUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

        requireNonNull(authentication, new PlayceMigratorException(ErrorCode.PM501A, "authentication cannot be null"));

        Object principal = authentication.getPrincipal();

        requireNotSameClass(principal, String.class, new PlayceMigratorException(ErrorCode.PM511A, "Principal is anonymous"));

        return (SecurityUser) principal;
    }

    public String getLoginId() {
        return getLoginUser().getUserLoginId();
    }
}