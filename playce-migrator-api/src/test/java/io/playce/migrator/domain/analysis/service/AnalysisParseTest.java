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
 * Jaeeon Bae       9월 23, 2022            First Draft.
 */
package io.playce.migrator.domain.analysis.service;

import io.playce.migrator.analysis.process.component.CommonProvider;
import io.playce.migrator.dto.analysisreport.DeletedApiResponse;
import io.playce.migrator.dto.analysisreport.DeprecatedApiResponse;
import io.playce.migrator.util.CommandUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.PumpStreamHandler;
import org.junit.jupiter.api.Test;
import org.springframework.boot.test.context.SpringBootTest;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@SpringBootTest
public class AnalysisParseTest {

//    @Autowired
//    private AnalysisThreadPoolExecutor executor;

    @Test
    public void parseTest() {
//        String filePath = "/opt/migrator/origin-dir/2/sample2.jar";
//        String filePath = "/opt/migrator/origin-dir/2/DeprecatedTest.class";
        String filePath = "/tmp/work-dir/1/petstore.ear";
        String version = "8";

//        runTask(filePath, version);

        File f = new File(filePath);
        deprscan(version, f);
        deps(f);
    }

    private static void deprscan(String version, File file) {
        CommandLine cl = null;
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            DefaultExecutor executor = new DefaultExecutor();
            PumpStreamHandler streamHandler = new PumpStreamHandler(baos);
            executor.setStreamHandler(streamHandler);

            cl = CommandUtil.getCommandLine(CommandUtil.findCommand("sudo"),
                    CommandUtil.findCommand("sh"),
                    CommonProvider.getJdeprscanFile().getAbsolutePath(),
                    version,
                    file.getAbsolutePath());

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

            int exitCode = executor.execute(cl);

            String result;

            if (exitCode == 0) {
                result = baos.toString();
//                String[] rs = result.split("\n");
//                for(String r: rs) {
//                    if(r.startsWith("error") || r.startsWith("warning") || r.startsWith("Directory")) continue;
                    log.debug("result : [{}]", result);
//                }
                deprscanParse(result);
            } else {
                throw new Exception(baos.toString());
            }
        } catch (Exception e) {
            log.info("jdeprscan execution failed. [Command] : {}", cl.toString());
            log.error("Command execution failed while execute jdeprscan. Error Log => [{}]", e.getMessage());
        }
    }

    /**
     * Deps.
     *
     * @param file the file
     */
    private static void deps(File file) {
        CommandLine cl = null;
        String scriptFile = CommonProvider.getJdepsFile().getAbsolutePath();
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            DefaultExecutor executor = new DefaultExecutor();
            PumpStreamHandler streamHandler = new PumpStreamHandler(baos);
            executor.setStreamHandler(streamHandler);

            cl = CommandUtil.getCommandLine(CommandUtil.findCommand("sudo"),
                    CommandUtil.findCommand("sh"),
                    scriptFile,
                    file.getAbsolutePath());

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
                log.debug("result : [{}]", result);
                depsParse(result);
            } else {
                throw new Exception(baos.toString());
            }
        } catch (Exception e) {
            log.info("jdeps execution failed. [Command] : {}", cl.toString());
            log.error("Command execution failed while execute jdeps. Error Log => [{}]", e.getMessage());
        }
    }

    private static void deprscanParse(String jdeprscanResult) {
        List<DeprecatedApiResponse> deprecatedApiList = new ArrayList<>();

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
                if (scanInfo.length >= 5 && !lineStr.startsWith("error:") && !lineStr.startsWith("warning:")) {
                    useClass = scanInfo[1].replaceAll("/", ".");

                    if (scanInfo[5].indexOf("::") < 0) {
                        referenceClass = scanInfo[5].replaceAll("/", ".");
                    } else {
                        String clazz = scanInfo[5].substring(0, scanInfo[5].indexOf("::")).replaceAll("/", ".");
                        method = scanInfo[5].substring(scanInfo[5].indexOf("::") + 2);
                        method = method.substring(0, method.indexOf("("));

                        if (method.equals("<init>")) {
                            method = "<init> - Constructor";
                        } else if (method.equals("<clinit>")) {
                            method = "<clinit> - Static Initializer";
                        }
                        referenceClass = clazz;
                    }

                    if (scanInfo.length == 6) {
                        forRemoval = true;
                    }

                    if (useClass != null && referenceClass != null && forRemoval != null) {
                        // save Deprecated data
                        DeprecatedApiResponse deprecatedApi = new DeprecatedApiResponse();
                        deprecatedApi.setClassName(useClass);
                        deprecatedApi.setJdkInternalApi(referenceClass);
                        deprecatedApi.setMethod(method);
                        deprecatedApi.setForRemoval(forRemoval);
                        deprecatedApiList.add(deprecatedApi);
                    }
                }
            }
        } catch (Exception e) {
            log.error("Exception occurred while execute DeprecatedScanTask.parse().", e);
        }

        for (DeprecatedApiResponse dep : deprecatedApiList) {
            log.debug(":+:+:+:+: className : " + dep.getClassName() + ", ref : " + dep.getJdkInternalApi() + ", method : " + dep.getMethod() + ", forRemoval : " + dep.isForRemoval());
        }
    }

    /**
     * Parse.
     *
     * @param jdepsResult the jdeps result
     */
    private static void depsParse(String jdepsResult) {
        List<DeletedApiResponse> deletedApiList = new ArrayList<>();
        String className = null;
        String jdkInternalApi = null;
        String replacement = null;

        try (BufferedReader buffer = new BufferedReader(new InputStreamReader(new ByteArrayInputStream(jdepsResult.getBytes())))) {
            String lineStr = null;
            int lineNum = 0;

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

//                    if (removed.getClazz() != null && removed.getApi() != null) {
//                        result.getRemovedList().add(removed);
//                    }
                }

                if (afterDash) {
                    String[] replacementArr = lineStr.split("Use");
                    if (jdkInternalApi.equals(replacementArr[0].trim())) {
                        replacement = replacementArr[1].trim();
                    }
                }
            }

            DeletedApiResponse deletedApi = new DeletedApiResponse();
            deletedApi.setClassName(className);
            deletedApi.setJdkInternalApi(jdkInternalApi);
            deletedApi.setReplacement(replacement);
            deletedApiList.add(deletedApi);
        } catch (Exception e) {
            log.error("Exception occurred while parsing jdpes result.", e);
        }

        // deleted api 데이터 저장
        for (DeletedApiResponse del : deletedApiList) {
            log.debug(":+:+:+:+: className : [{}], jdkInternalApi : [{}], replacement : [{}]", del.getClassName(), del.getJdkInternalApi(), del.getReplacement());
        }
    }
}