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
 * Dong-Heon Han    Sep 01, 2022		First Draft.
 */

package io.playce.migrator.migration.process.util;

public class CommentUtil {
    public static String addComment(String content, String lineComment) {
        String newContent = lineComment + " Commented out by Playce Migrator\n";
        newContent += lineComment + " " + content + "\n";
        return newContent;
    }

    public static String addXMLComment(String content) {
        String newContent = "<!-- Commented out by Playce Migrator -->\n";
        newContent += "<!-- " + content.replaceAll("</", "< /") + "-->\n";
        return newContent;
    }
}