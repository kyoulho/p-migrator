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
 * Dong-Heon Han    Aug 12, 2022		First Draft.
 */

package io.playce.migrator.prepare.zip.component;

import io.playce.migrator.constant.ErrorCode;
import io.playce.migrator.exception.PlayceMigratorException;
import io.playce.migrator.prepare.config.PrepareConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.benf.cfr.reader.api.CfrDriver;
import org.benf.cfr.reader.bytecode.analysis.parse.utils.Pair;
import org.benf.cfr.reader.util.getopt.GetOptParser;
import org.benf.cfr.reader.util.getopt.Options;
import org.benf.cfr.reader.util.getopt.OptionsImpl;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
@Slf4j
public class DecompileComponent {
    private final UnzipComponent unzipComponent;
    private final PrepareConfig prepareConfig;

    public void decompile(Path sourceFile, Path targetDir) {
        String[] args = new String[]{sourceFile.toString(),
                "--silent", "true",
                "--outputdir", targetDir.toString()};

        GetOptParser getOptParser = new GetOptParser();
        Pair<List<String>, Options> processedArgs = getOptParser.parse(args, OptionsImpl.getFactory());
        List<String> files = processedArgs.getFirst();
        Options options = processedArgs.getSecond();

        CfrDriver cfrDriver = new CfrDriver.Builder().withBuiltOptions(options).build();
        try {
            cfrDriver.analyse(files);
        } catch (NoClassDefFoundError e) {
            log.error(e.getMessage(), e);
        }
    }

    public void processSourceFile(Path sourceFile, Path targetDir, Path decompressDir) {
        unzipComponent.decompress(sourceFile, targetDir);
        decompile(sourceFile, decompressDir);
    }

    public void processSubJarFiles(Path unzipDir, String unzipPath, String decompilePath) {
        processSubFiles(unzipDir, "jar", "sub-jars", unzipPath, decompilePath);
    }

    public void processSubFiles(Path unzipDir, String fileExtension, String workDeployPath, String unzipPath, String decompilePath) {
        try (Stream<Path> stream = Files.walk(unzipDir)) {
            List<Path> files = stream.filter(Files::isRegularFile)
                    .filter(f -> f.getFileName().toString().endsWith(fileExtension))
                    .filter(this::checkIgnoreFiles)
                    .collect(Collectors.toList());
            for (Path file : files) {
                log.debug("sub-file: {}", file);
                String name = file.getFileName().toString();
                Path libZipDir = Path.of(unzipDir.toString(), workDeployPath, name, unzipPath);
                Path libDecomp = Path.of(unzipDir.toString(), workDeployPath, name, decompilePath); // + File.separator + "lib" + File.separator + "decompile_" + file.getFileName();
                processSourceFile(file, libZipDir, libDecomp);
            }
        } catch (IOException e) {
            throw new PlayceMigratorException(ErrorCode.PM310F, e.getMessage());
        }
    }

    private boolean checkIgnoreFiles(Path jarPath) {
        String fileName = jarPath.getFileName().toString();
        List<String> ignoredJarPrefix = prepareConfig.getIgnoredJarPrefix();
        for (String jarPrefix : ignoredJarPrefix) {
            if(fileName.startsWith(jarPrefix)) {
                log.debug("filtered library: {}", fileName);
                return false;
            }
        }
        return true;
    }
}