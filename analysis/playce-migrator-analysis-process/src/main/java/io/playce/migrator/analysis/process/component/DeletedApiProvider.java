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
package io.playce.migrator.analysis.process.component;

import io.playce.migrator.dao.entity.DeletedInfo;
import io.playce.migrator.dao.repository.DeletedInfoRepository;
import io.playce.migrator.util.CommandUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeletedApiProvider extends CommonProvider {

    private final DeletedInfoRepository deletedInfoRepository;

    /**
     * deps 명령어를 통해 deleted api를 가져온다.
     */
    public void deps(File file, Long applicationId) {
        CommandLine cl = null;
        String scriptFile = CommonProvider.getJdepsFile().getAbsolutePath();
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            DefaultExecutor executor = new DefaultExecutor();
            PumpStreamHandler streamHandler = new PumpStreamHandler(baos);
            executor.setStreamHandler(streamHandler);

            cl = CommandUtil.getCommandLine(CommandUtil.findCommand("sudo"),
                    CommandUtil.findCommand("sh"),
                    scriptFile,
                    System.getProperty("user.dir") + File.separator + "jdk",
                    file.isDirectory() ? file.getAbsolutePath() + File.separator : file.getAbsolutePath());

            /*
            cl = new CommandLine(CollectionHelper.findCommand("sudo"))
                    .addArguments(CollectionHelper.findCommand("sh"))
                    .addArguments(CollectionHelper.getJdepsFile().getAbsolutePath())
                    .addArguments(file.getAbsolutePath());
            */

            /*
            cl = new CommandLine(CollectionHelper.findCommand("jdeps"))
                    .addArguments("--jdk-internals")
                    .addArguments(file.getAbsolutePath());
            */

            log.debug("commandLine : [{}]", cl.toString());
            int exitCode = executor.execute(cl);

            String result;

            if (exitCode == 0) {
                result = baos.toString();
//                log.debug("result : [{}]", result);
                depsParse(result, applicationId);
            } else {
                throw new Exception(baos.toString());
            }
        } catch (Exception e) {
            log.info("jdeps execution failed. [Command] : {}", cl.toString());
            log.debug("Command execution failed while execute jdeps. Error Log => [{}]", e.getMessage());
        }
    }

    private void depsParse(String jdepsResult, Long applicationId) {
        List<DeletedInfo> deletedApiList = new ArrayList<>();
        String className = null;
        String jdkInternalApi = null;
        Map<String, String> replacementMap = new HashMap<>();

        try (BufferedReader buffer = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(jdepsResult.getBytes())))) {
            String lineStr = null;
            int lineNum = 1;

            boolean afterWarning = false;
            boolean afterDash = false;
            while ((lineStr = buffer.readLine()) != null) {
                if (lineNum++ == 1) {
                    continue;
                }

                if (lineStr.startsWith("Warning:")) {
                    afterWarning = true;
                    continue;
                }

                if (lineStr.startsWith("-----")) {
                    afterDash = true;
                    continue;
                }

                if (!afterWarning && !afterDash && lineStr.contains("->")) {
                    int i = 0;
                    for (String s : lineStr.split(" ")) {
                        if (!s.equals("->") && !s.equals(" ") && !s.equals("")) {
                            if (i++ == 0) {
                                className = s;
                            } else {
                                jdkInternalApi = s;
                                break;
                            }
                        }
                    }
                }

                if (afterDash) {
                    String[] replacementArr = lineStr.split("Use");
                    replacementMap.computeIfAbsent(replacementArr[0].trim(), k -> replacementArr[1].trim());
                }
            }

            // Deleted data
            DeletedInfo deletedInfo = new DeletedInfo();
            deletedInfo.setUsedClassName(className);
            deletedInfo.setDeletedName(jdkInternalApi);
            deletedInfo.setReplacementComment(replacementMap.get(jdkInternalApi));
            deletedInfo.setApplicationId(applicationId);

            if (deletedInfo.getUsedClassName() != null && deletedInfo.getDeletedName() != null && deletedInfo.getReplacementComment() != null) {
                deletedApiList.add(deletedInfo);
            }
        } catch (Exception e) {
            log.error("Exception occurred while parsing jdpes result.", e);
        }

        // 기존 데이터 삭제
        deletedInfoRepository.deleteByApplicationId(applicationId);

        // deleted api 데이터 저장
        deletedInfoRepository.saveAll(deletedApiList);
    }
}