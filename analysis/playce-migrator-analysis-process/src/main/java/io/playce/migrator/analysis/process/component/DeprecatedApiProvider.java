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

import io.playce.migrator.constant.DeprecatedType;
import io.playce.migrator.dao.entity.DeprecatedInfo;
import io.playce.migrator.dao.repository.DeprecatedInfoRepository;
import io.playce.migrator.util.CommandUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;
import org.springframework.stereotype.Component;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
@RequiredArgsConstructor
public class DeprecatedApiProvider extends CommonProvider {

    private final DeprecatedInfoRepository deprecatedInfoRepository;

    /**
     * jdeprscan 명령어를 통해 deprecated api를 가져온다.
     */
    public void deprscan(String version, File file, Long applicationId) {
        String javaVersion = parseJavaVersion(version);
        String scriptFile = CommonProvider.getJdeprscanFile().getAbsolutePath();

        CommandLine cl = null;
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            DefaultExecutor executor = new DefaultExecutor();
            PumpStreamHandler streamHandler = new PumpStreamHandler(baos);
            executor.setStreamHandler(streamHandler);

            cl = CommandUtil.getCommandLine(CommandUtil.findCommand("sudo"),
                    CommandUtil.findCommand("sh"),
                    scriptFile,
                    System.getProperty("user.dir") + File.separator + "jdk",
                    javaVersion,
                    file.isDirectory() ? file.getAbsolutePath() + File.separator : file.getAbsolutePath());

            /*
            cl = new CommandLine(CollectionHelper.findCommand("sudo"))
                    .addArguments(CollectionHelper.findCommand("sh"))
                    .addArguments(CollectionHelper.getJdeprscanFile().getAbsolutePath())
                    .addArguments(version.toString())
                    .addArguments(assessmentFile.getAbsolutePath());
            */
            /*
            cl = new CommandLine(CollectionHelper.findCommand("jdeprscan"))
                    .addArguments("--release")
                    .addArguments(version.toString())
                    .addArguments(file.getAbsolutePath())
                    .addArguments("2> /dev/null");
            */

            log.debug("commandLine : [{}]", cl.toString());
            int exitCode = executor.execute(cl);

            String result;

            if (exitCode == 0) {
                result = baos.toString();
//                log.debug("result : [{}]", result);
                deprscanParse(result, applicationId);
            } else {
                throw new Exception(baos.toString());
            }
        } catch (Exception e) {
            log.info("jdeprscan execution failed. [Command] : {}", cl.toString());
            log.debug("Command execution failed while execute jdeprscan. Error Log => [{}]", e.getMessage());
        }
    }

    private void deprscanParse(String jdeprscanResult, Long applicationId) {
        List<DeprecatedInfo> deprecatedApiList = new ArrayList<>();

        try (BufferedReader buffer = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(jdeprscanResult.getBytes())))) {
            String lineStr = null;
            int lineNum = 0;

            String useClass = null;
            String referenceClass = null;
            String method = null;
            Boolean forRemoval = false;
            while ((lineStr = buffer.readLine()) != null) {
                if (lineNum++ == 1) {
                    continue;
                }

                String[] scanInfo = lineStr.split(" ");
                if (scanInfo.length >= 6 && !lineStr.startsWith("error:") && !lineStr.startsWith("warning:")) {
                    useClass = scanInfo[1].replaceAll("/", ".");

                    if (!scanInfo[5].contains("::")) {
                        referenceClass = scanInfo[5].replaceAll("/", ".");
                    } else {
                        String clazz = scanInfo[5].substring(0, scanInfo[5].indexOf("(")).replaceAll("/", ".");
                        method = scanInfo[5].substring(scanInfo[5].indexOf("::") + 2);
                        method = method.substring(0, method.indexOf("("));

                        if (method.equals("<init>")) {
                            method = "<init> - Constructor";
                        } else if (method.equals("<clinit>")) {
                            method = "<clinit> - Static Initializer";
                        }
                        referenceClass = clazz;
                    }

                    if (scanInfo.length == 7) {
                        forRemoval = true;
                    }

                    if (useClass != null && referenceClass != null && forRemoval != null) {
                        // Deprecated data
                        DeprecatedInfo deprecatedInfo = new DeprecatedInfo();
                        deprecatedInfo.setUsedClassName(useClass);
                        deprecatedInfo.setDeprecatedName(referenceClass);
                        deprecatedInfo.setDeprecatedType(method == null ? DeprecatedType.CLASS.name() : DeprecatedType.METHOD.name());
                        deprecatedInfo.setForRemoval(forRemoval);
                        deprecatedInfo.setApplicationId(applicationId);
                        deprecatedApiList.add(deprecatedInfo);
                    }
                }

                // 초기화
                method = null;
                forRemoval = false;
            }
        } catch (Exception e) {
            log.error("Exception occurred while execute DeprecatedScanTask.parse().", e);
        }

        // 기존 데이터 삭제
        deprecatedInfoRepository.deleteByApplicationId(applicationId);

        // 신규 데이터 추가
        deprecatedInfoRepository.saveAll(deprecatedApiList);
    }
}