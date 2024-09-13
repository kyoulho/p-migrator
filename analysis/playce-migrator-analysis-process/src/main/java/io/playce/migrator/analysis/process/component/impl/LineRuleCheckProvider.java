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
 * Dong-Heon Han    Sep 21, 2022		First Draft.
 */

package io.playce.migrator.analysis.process.component.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.playce.migrator.analysis.process.async.AnalysisRuleCheckComponent;
import io.playce.migrator.analysis.process.component.IAnalysisProvider;
import io.playce.migrator.analysis.process.service.AnalysisPostService;
import io.playce.migrator.constant.ErrorCode;
import io.playce.migrator.constant.FileType;
import io.playce.migrator.constant.JobStatus;
import io.playce.migrator.constant.JobStep;
import io.playce.migrator.dto.analysis.AnalysisRuleResult;
import io.playce.migrator.dto.analysis.AnalysisRuleResults;
import io.playce.migrator.dto.analysis.AnalysisTaskItem;
import io.playce.migrator.dto.progress.JobProgress;
import io.playce.migrator.exception.PlayceMigratorException;
import io.playce.migrator.rule.loader.RuleSessionFactory;
import io.playce.migrator.util.PathUtils;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static io.playce.migrator.util.WebSocketSender.sendMessage;

@Component
@RequiredArgsConstructor
@Slf4j
public class LineRuleCheckProvider implements IAnalysisProvider {
    private final AnalysisRuleCheckComponent analysisRuleCheckComponent;
    private final AnalysisPostService analysisPostService;
    private final ObjectMapper objectMapper;
    private final AnalysisStatusComponent analysisStatusComponent;

    @Override
    public void check(AnalysisTaskItem item, FileType type, Path appPath, RuleSessionFactory factory, JobProgress progress) {
        try {
            if(type == FileType.CLASS && appPath.toString().endsWith("zip")) return; // source 파일은 decompile 된 파일이 없다.

            List<AnalysisRuleResult> analysisRuleResults = analysisProcess(appPath, item, type, factory, progress);
            analysisPostService.saveAnalysisHistoryDetails(item, analysisRuleResults);
        } catch (RuntimeException e) {
            throw new PlayceMigratorException(ErrorCode.PM701T, e);
        }
    }

    private List<AnalysisRuleResult> analysisProcess(Path appPath, AnalysisTaskItem item, FileType type, RuleSessionFactory factory, JobProgress progress) {
        List<AnalysisRuleResult> analysisRuleResults = analysisByType(appPath, type, factory, progress);
        List<AnalysisRuleResult> result = new ArrayList<>(analysisRuleResults);

        //선택된 라이브러리
        List<String> selectedLibraries = item.getSelectedLibraries(objectMapper);
        for (String library : selectedLibraries) {
            Path libPath = PathUtils.getSubLibrariesPath(appPath, library);
            if (!libPath.toFile().exists()) continue;

            analysisRuleResults = analysisByType(libPath, type, factory, progress);
            result.addAll(analysisRuleResults);
        }
        return result;
    }

    private List<AnalysisRuleResult> analysisByType(Path work, FileType type, RuleSessionFactory factory, JobProgress progress) {
        //class 파일의 경우 decompile 된 java 파일로 rule 체크한다.
        String extension = type == FileType.CLASS ? FileType.JAVA.lower() : type.lower();
        try(Stream<Path> workStream = PathUtils.getWorkStream(type, work)) {
            List<Path> files = workStream.filter(Files::isRegularFile)
                    .filter(f -> f.getFileName().toString().endsWith(extension))
                    .filter(f -> {
                        sendMessage(analysisStatusComponent, progress, JobStep.STEP4, JobStatus.PROC, "analyzing... " + f.getFileName());
                        return true;
                    })
                    .collect(Collectors.toList());
            if(files.isEmpty()) return new ArrayList<>();

            List<CompletableFuture<AnalysisRuleResults>> futures = files.stream()
                    .map(f -> analysisRuleCheckComponent.apply(f, factory, StandardCharsets.UTF_8)).collect(Collectors.toList());

            List<AnalysisRuleResult> summaryRuleResult = new ArrayList<>();
            futures.stream().map(CompletableFuture::join).filter(rs -> !rs.getResults().isEmpty()).forEach(rs-> summaryRuleResult.addAll(rs.getResults()));
            return summaryRuleResult;
        } catch (IOException e) {
            throw new PlayceMigratorException(ErrorCode.PM271R, e);
        }
    }
}