package io.playce.migrator.domain.project.service;

import io.playce.migrator.constant.ErrorCode;
import io.playce.migrator.constant.TaskType;
import io.playce.migrator.dao.entity.Project;
import io.playce.migrator.dao.mapper.ProjectMapper;
import io.playce.migrator.dao.repository.ProjectRepository;
import io.playce.migrator.domain.application.service.ApplicationService;
import io.playce.migrator.dto.application.ApplicationResponse;
import io.playce.migrator.dto.project.ProjectRequest;
import io.playce.migrator.dto.project.ProjectResponse;
import io.playce.migrator.exception.PlayceMigratorException;
import io.playce.migrator.util.LoginUserUtil;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.Instant;
import java.util.List;

@Slf4j
@Service
@Transactional
@RequiredArgsConstructor
public class ProjectService {
    private final ApplicationService applicationService;
    private final ProjectRepository projectRepository;
    private final ProjectMapper projectMapper;
    private final ModelMapper modelMapper;
    private final LoginUserUtil loginUserUtil;

    private Project getEntity(Long projectId) {
        return projectRepository.findById(projectId)
                .orElseThrow(() -> new PlayceMigratorException(ErrorCode.PM201R, "project ID :" + projectId + " not exist"));
    }

    public ProjectResponse getProject(Long projectId) {
        return modelMapper.map(getEntity(projectId), ProjectResponse.class);
    }

    public ProjectResponse getProjectWithTargetWithAppCount(Long projectId) {
        return projectMapper.selectProjectWithTargetWithAppCount(projectId)
                .orElseThrow(() -> new PlayceMigratorException(ErrorCode.PM201R, "project ID :" + projectId + " not exist"));
    }

    public List<ProjectResponse> getProjectsWithTargetWithAppCount() {
        return projectMapper.selectProjectsWithTargetWithAppCount();
    }

    public Long registProject(ProjectRequest projectRequest) {

        Project project = modelMapper.map(projectRequest, Project.class);
        project.setRegistLoginId(loginUserUtil.getLoginId());
        project.setRegistDatetime(Instant.now());
        project.setModifyLoginId(loginUserUtil.getLoginId());
        project.setModifyDatetime(Instant.now());

        return projectRepository.save(project).getProjectId();
    }

    public void modifyProject(Long projectId, ProjectRequest projectRequest) {
        Project project = getEntity(projectId);

        modelMapper.map(projectRequest, project);
        project.setModifyLoginId(loginUserUtil.getLoginId());
        project.setModifyDatetime(Instant.now());
    }

    public void removeProject(Long projectId) {
        // 프로젝트에 속한 애플리케이션이 작업 중인지 확인한다.
        List<ApplicationResponse> applicationList = applicationService.getApplicationList(projectId);
        for (ApplicationResponse app : applicationList) {
            applicationService.checkProgressingJob(app.getApplicationId(), TaskType.Analysis.name());
            applicationService.checkProgressingJob(app.getApplicationId(), TaskType.Migration.name());
        }

        Project entity = getEntity(projectId);
        projectRepository.delete(entity);
    }
}
