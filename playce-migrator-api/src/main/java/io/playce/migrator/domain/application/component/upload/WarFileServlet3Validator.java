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
 * Jaeeon Bae       10월 07, 2022            First Draft.
 */
package io.playce.migrator.domain.application.component.upload;

import org.springframework.stereotype.Component;

import java.io.File;

@Component
public class WarFileServlet3Validator implements IFileValidator {

    @Override
    public boolean isSupport(File file) {
        return file.getName().endsWith(".war");
    }

    @Override
    public boolean validate(File file) {
        return true;
//
//        try (ZipFile zipFile = new ZipFile(file)) {
//            Enumeration<? extends ZipEntry> entries = zipFile.entries();
//            while (entries.hasMoreElements()) {
//                ZipEntry zipEntry = entries.nextElement();
//                String name = zipEntry.getName();
//                if (name.equals("WEB-INF/lib/servlet-api.jar")) {
//                    return true;
//                }
//            }
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        return false;
    }
}