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
package io.playce.migrator.util;

import io.playce.migrator.dto.common.CommandResult;
import io.playce.migrator.exception.PlayceMigratorException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.exec.CommandLine;
import org.apache.commons.exec.DefaultExecutor;
import org.apache.commons.exec.Executor;
import org.apache.commons.exec.PumpStreamHandler;
import org.apache.commons.exec.environment.EnvironmentUtils;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.nio.file.Path;
import java.util.Map;

@Slf4j
public class CommandUtil {

    private static Map<String, String> environment;
    private static String username;
    private static boolean isSudoer;

    static {
        try {
            setEnvironment();
        } catch (IOException e) {
            log.error("Unhandled exception occurred while initialize CommandUtil.", e);
        }
        try {
            checkUsername();
        } catch (Exception e) {
            log.error("Unhandled exception occurred while initialize CommandUtil.", e);
        }
    }

    private static void setEnvironment() throws IOException {
        environment = EnvironmentUtils.getProcEnvironment();
        String path = environment.get("PATH");

        path = "/usr/local/bin:" + path;
        environment.put("PATH", path);
    }


    private static void checkUsername() throws InterruptedException {
        username = System.getProperty("user.name");

        if ("root".equals(username)) {
            isSudoer = true;
        } else {
            try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
                DefaultExecutor executor = new DefaultExecutor();
                PumpStreamHandler streamHandler = new PumpStreamHandler(baos);
                executor.setStreamHandler(streamHandler);

                int exitCode = executor.execute(new CommandLine(findCommand("sudo")).addArguments(new String[]{"-n", "echo", username}));

                if (exitCode == 0) {
                    String result = baos.toString().replaceAll("\\n", "").replaceAll("\\r", "");

                    if (result.trim().equals(username)) {
                        isSudoer = true;
                    }
                }
            } catch (Exception e) {
                log.warn("Unable to check [{}] is sudoer or not. ({})", username, e.getMessage());
            }
        }

        log.info("Playce Migrator Runtime Information - Username : [{}], Is Sudoer : [{}]", username, isSudoer);
    }

    public static String findCommand(String command) {
        try (ByteArrayOutputStream baos = new ByteArrayOutputStream()) {
            DefaultExecutor executor = new DefaultExecutor();
            PumpStreamHandler streamHandler = new PumpStreamHandler(baos);
            executor.setStreamHandler(streamHandler);

            int exitCode = executor.execute(new CommandLine("which").addArgument(command), environment);

            if (exitCode == 0) {
                return baos.toString().replaceAll("\\n", "").replaceAll("\\r", "");
            }
        } catch (Exception e) {
            log.warn("[{}] command cannot be found using /usr/bin/which. Command will be used without path.", command);
        }

        return command;
    }

    public static CommandResult run(String command, PlayceMigratorException exception) {
        return run(command, null, exception);
    }

    public static CommandResult run(String command, Path workPath, PlayceMigratorException exception) {
        CommandLine commandLine = CommandLine.parse(command);
        log.info("Execute Command [{}]", commandLine);

        CommandResult result = new CommandResult();
        try (ByteArrayOutputStream stdOut = new ByteArrayOutputStream();
             ByteArrayOutputStream errOut = new ByteArrayOutputStream();
        ) {
            Executor executor = new DefaultExecutor();
            PumpStreamHandler streamHandler = new PumpStreamHandler(stdOut, errOut);
            executor.setStreamHandler(streamHandler);
            if(workPath != null) {
                executor.setWorkingDirectory(workPath.toFile());
            }

            int exitCode = executor.execute(commandLine);
            result.setExitCode(exitCode);

            if (exitCode == 0) {
                result.setStandardOut(stdOut.toString());
            } else {
                result.setErrorOut(errOut.toString());
            }
        } catch (Exception e) {
            exception.initCause(e);
            throw exception;
        }
        return result;
    }

    public static CommandLine getCommandLine(String... commands) {
        CommandLine cl = null;

        if (commands[0].endsWith("sudo")) {
            if (!"root".equals(username) && isSudoer) {
                cl = new CommandLine(commands[0]);
            }
        } else {
            cl = new CommandLine(commands[0]);
        }

        for (int i = 1; i < commands.length; i++) {
            if (cl == null) {
                cl = new CommandLine(commands[i]);
            } else {
                cl = cl.addArguments(commands[i]);
            }
        }
        return cl;
    }
}