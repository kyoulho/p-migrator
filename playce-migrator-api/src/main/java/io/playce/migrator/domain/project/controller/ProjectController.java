package io.playce.migrator.domain.project.controller;

import io.playce.migrator.constant.ErrorCode;
import io.playce.migrator.domain.project.service.ProjectService;
import io.playce.migrator.dto.project.ProjectRequest;
import io.playce.migrator.dto.project.ProjectResponse;
import io.playce.migrator.exception.PlayceMigratorException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping("/api/projects")
public class ProjectController {
    private final ProjectService projectService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<ProjectResponse> getProjects() {
        return projectService.getProjectsWithTargetWithAppCount();
    }

    @GetMapping("/{projectId}")
    @ResponseStatus(HttpStatus.OK)
    public ProjectResponse getProject(@PathVariable Long projectId) {
        return projectService.getProjectWithTargetWithAppCount(projectId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.OK)
    public ProjectResponse registProject(@RequestBody @Validated ProjectRequest projectRequest, BindingResult bindingResult) {
        validate(bindingResult);
        Long projectId = projectService.registProject(projectRequest);
        return projectService.getProject(projectId);
    }

    @PutMapping("/{projectId}")
    @ResponseStatus(HttpStatus.OK)
    public ProjectResponse modifyProject(@PathVariable Long projectId, @RequestBody @Validated ProjectRequest projectRequest, BindingResult bindingResult) {
        validate(bindingResult);
        projectService.modifyProject(projectId, projectRequest);
        return projectService.getProject(projectId);
    }

    @DeleteMapping("/{projectId}")
    @ResponseStatus(HttpStatus.OK)
    public ProjectResponse removeProject(@PathVariable Long projectId) {
        ProjectResponse project = projectService.getProject(projectId);
        projectService.removeProject(projectId);
        return project;
    }

    private void validate(BindingResult bindingResult) {
        if (bindingResult.hasErrors()) {
            throw new PlayceMigratorException(ErrorCode.PM107H, "An error occurred while binding data.");
        }
    }

}
