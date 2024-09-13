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

package io.playce.migrator.analysis.process.async;

import com.ibm.icu.text.CharsetDetector;
import com.ibm.icu.text.CharsetMatch;
import io.playce.migrator.constant.ErrorCode;
import io.playce.migrator.exception.PlayceMigratorException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.MalformedInputException;
import java.nio.charset.StandardCharsets;
import java.nio.charset.UnmappableCharacterException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.List;
import java.util.concurrent.CompletableFuture;

@Component
@Slf4j
public class AsyncConversionComponent {

    @Async("workerExecutor")
    public CompletableFuture<String> conversionToUTF8(Path f) {
        String charset = guessCharset(f);
        return conversionFile(f, charset, StringUtils.EMPTY);
    }

    private static CompletableFuture<String> conversionFile(Path f, String charset, String indent) {
        CompletableFuture<String> result = new CompletableFuture<>();
        if(StandardCharsets.UTF_8.name().equals(charset)) {
            return CompletableFuture.completedFuture(charset);
        }
        log.trace("{}{}: {}", indent, charset, f);

        try {
            List<String> lines = Files.readAllLines(f, Charset.forName(charset));
            Files.write(f, lines, StandardCharsets.UTF_8, StandardOpenOption.CREATE);
            return CompletableFuture.completedFuture(charset);
        } catch (MalformedInputException e) {
            return conversionFile(f, StandardCharsets.ISO_8859_1.name(), " -> ");
        } catch (UnmappableCharacterException e) {
            return conversionFile(f, StandardCharsets.UTF_8.name(), " -> ");
        } catch (IOException e) {
            log.error("charset {}, file: {} - {}", charset, f, e.getMessage());
            throw new PlayceMigratorException(ErrorCode.PM304F, e.getMessage(), e);
        }
    }

    private String guessCharset(Path f) {
        CharsetDetector detector = new CharsetDetector();
        try(FileInputStream fis = new FileInputStream(f.toFile())) {
            byte[] bytes = fis.readAllBytes();
            detector.setText(bytes);
            CharsetMatch detect = detector.detect();
            return detect.getName();
        } catch (IOException e) {
            throw new PlayceMigratorException(ErrorCode.PM304F, e.getMessage(), e);
        }
    }
}