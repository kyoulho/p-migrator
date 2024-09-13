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
 * Dong-Heon Han    Sep 15, 2022		First Draft.
 */

package io.playce.migrator.analysis.process.component;

import io.playce.migrator.analysis.process.async.AsyncConversionComponent;
import io.playce.migrator.constant.ErrorCode;
import io.playce.migrator.exception.PlayceMigratorException;
import io.playce.migrator.prepare.config.PrepareConfig;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
@Slf4j
public class ConversionComponent {
    private final PrepareConfig prepareConfig;
    private final AsyncConversionComponent asyncConversionComponent;

    public Map<String, Integer> conversionUtf8(Path newTargetDir) {
        log.debug("run conversion root: {}", newTargetDir);

        Map<String, Integer> charsetMap = new HashMap<>();
        try(Stream<Path> stream = Files.walk(newTargetDir)) {
            List<Path> files = stream.filter(Files::isRegularFile)
                    .filter(this::checkExtension).collect(Collectors.toList());
            files.forEach(f -> {
                CompletableFuture<String> future = asyncConversionComponent.conversionToUTF8(f);
                future.join();

                countFiles(charsetMap, future);
            });

            log.debug("{}", charsetMap);
            return charsetMap;
        } catch (IOException e) {
            throw new PlayceMigratorException(ErrorCode.PM374F, e.getMessage(), e);
        }
    }

    private void countFiles(Map<String, Integer> charsetMap, CompletableFuture<String> future) {
        try {
            String charset = future.get();
            if(!charsetMap.containsKey(charset)) {
                charsetMap.put(charset, 0);
            }
            charsetMap.put(charset, charsetMap.get(charset) + 1);
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }

    private boolean checkExtension(Path filePath) {
        List<String> utf8ConversionExtensions = prepareConfig.getUtf8ConversionExtensions();
        String filename = filePath.toString();
        int index = filename.lastIndexOf(".");
        if(index == -1) return false;

        String extension = filename.substring(index + 1).toLowerCase();
        return utf8ConversionExtensions.contains(extension);
    }
}