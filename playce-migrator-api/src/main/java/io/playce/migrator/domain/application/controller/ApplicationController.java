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
 * Jaeeon Bae       8월 18, 2022            First Draft.
 */
package io.playce.migrator.domain.application.controller;

import io.playce.migrator.constant.ErrorCode;
import io.playce.migrator.domain.application.service.ApplicationService;
import io.playce.migrator.domain.application.service.ScmApplicationService;
import io.playce.migrator.domain.application.service.UploadApplicationService;
import io.playce.migrator.domain.project.service.ProjectService;
import io.playce.migrator.dto.application.ApplicationRequest;
import io.playce.migrator.dto.application.ApplicationResponse;
import io.playce.migrator.exception.PlayceMigratorException;
import io.playce.migrator.util.FileUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.util.UriUtils;

import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/api/projects/{projectId}")
@RequiredArgsConstructor
public class ApplicationController {

    private final ProjectService projectService;
    private final ApplicationService applicationService;
    private final ScmApplicationService scmApplicationService;
    private final UploadApplicationService uploadApplicationService;

    private void validate(BindingResult bindingResult, Long projectId) {
        if (bindingResult != null && bindingResult.hasErrors()) {
            throw new PlayceMigratorException(ErrorCode.PM107H, "An error occurred while binding data.");
        }
        projectService.getProject(projectId);
    }

    @PostMapping(value = "/applications", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ApplicationResponse createApplication(@PathVariable Long projectId,
                                                 @RequestPart("application") @Validated ApplicationRequest applicationRequest, BindingResult bindingResult,
                                                 @RequestPart(value = "applicationFile", required = false) MultipartFile applicationFile) throws Exception {
        validate(bindingResult, projectId);
        Long applicationId;
        if (applicationRequest.isScmYn()) {
            applicationId = scmApplicationService.createApplication(projectId, applicationRequest);
        } else {
            applicationId = uploadApplicationService.createApplication(projectId, applicationRequest, applicationFile, null);
        }
        return applicationService.getApplicationDetail(projectId, applicationId);
    }

    @PostMapping(value = "/applications/{applicationId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @ResponseStatus(HttpStatus.OK)
    public ApplicationResponse modifyApplication(@PathVariable Long projectId, @PathVariable Long applicationId,
                                                 @RequestPart("application") @Validated ApplicationRequest applicationRequest, BindingResult bindingResult,
                                                 @RequestPart(value = "applicationFile", required = false) MultipartFile applicationFile) {
        validate(bindingResult, projectId);
        Long originApplicationId;
        if (applicationRequest.isScmYn()) {
            originApplicationId = scmApplicationService.modifyApplication(applicationId, applicationRequest);
        } else {
            originApplicationId = uploadApplicationService.modifyApplication(applicationId, applicationRequest, applicationFile);
        }
        return applicationService.getApplicationDetail(projectId, originApplicationId);
    }

    @GetMapping(value = "/applications")
    @ResponseStatus(HttpStatus.OK)
    public List<ApplicationResponse> getApplications(@PathVariable Long projectId) {
        validate(null, projectId);
        return applicationService.getApplicationList(projectId);
    }

    @GetMapping(value = "/applications/{applicationId}")
    @ResponseStatus(HttpStatus.OK)
    public ApplicationResponse getApplicationDetail(@PathVariable Long projectId, @PathVariable Long applicationId) {
        validate(null, projectId);
        return applicationService.getApplicationDetail(projectId, applicationId);
    }

    @DeleteMapping(value = "/applications/{applicationId}")
    @ResponseStatus(HttpStatus.OK)
    public ApplicationResponse removeApplication(@PathVariable Long projectId, @PathVariable Long applicationId) {
        validate(null, projectId);
        ApplicationResponse applicationDetail = getApplicationDetail(projectId, applicationId);
        applicationService.removeApplication(applicationId);
        return applicationDetail;
    }

    @ResponseStatus(HttpStatus.OK)
    @GetMapping("/applications/{applicationId}/download")
    public ResponseEntity<Resource> downloadApplication(@PathVariable Long projectId, @PathVariable Long applicationId) {
        validate(null, projectId);
        ApplicationResponse application = applicationService.getApplicationDetail(projectId, applicationId);

        String filePath = Path.of(application.getOriginPath(), application.getOriginFileName()).toString();
        Resource resource = FileUtil.getFile(filePath);
        String encodeFileName = UriUtils.encode(FileUtil.getName(filePath), StandardCharsets.UTF_8);
        String contentDisposition = "attachment;filename=" + encodeFileName + ";filename*=UTF-8''" + encodeFileName;
        return ResponseEntity.ok().header(HttpHeaders.CONTENT_DISPOSITION, contentDisposition).body(resource);
    }

}