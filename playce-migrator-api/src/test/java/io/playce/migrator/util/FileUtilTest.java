package io.playce.migrator.util;

import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
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
 * Dong-Heon Han    Aug 23, 2022		First Draft.
 */

@Slf4j
class FileUtilTest {
    @Test
    public void zipEntiresNames() {
        File file = new File("test-files/jar/sample2.jar");
        List<String> libraryNamesFromZipfile = FileUtil.getLibraryNamesFromZipfile(file);
        for(String libraryName: libraryNamesFromZipfile) {
            log.debug(libraryName);
            assertEquals("jar", libraryName.substring(libraryName.length()-3));
        }
        assertEquals(12, libraryNamesFromZipfile.size());
    }
}