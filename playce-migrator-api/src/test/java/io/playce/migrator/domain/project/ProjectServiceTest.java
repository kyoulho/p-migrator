package io.playce.migrator.domain.project;

import io.playce.migrator.dao.entity.Project;
import io.playce.migrator.dao.repository.ProjectRepository;
import io.playce.migrator.dao.repository.TargetRepository;
import io.playce.migrator.domain.project.service.ProjectService;
import io.playce.migrator.dto.project.ProjectRequest;
import io.playce.migrator.dto.project.ProjectResponse;
import io.playce.migrator.util.LoginUserUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@SpringBootTest
@Transactional
class ProjectServiceTest {

    @Autowired
    ProjectService projectService;
    @Autowired
    ProjectRepository projectRepository;
    @Autowired
    TargetRepository targetRepository;
    @MockBean
    LoginUserUtil loginUserUtil;


    @Test
    @DisplayName("프로젝트를 타겟아이디와 저장한다.")
    void registProject() {
        //given
        given(loginUserUtil.getLoginId()).willReturn("admin");

        String projectName = "default";
        String description = "no description";
        Long targetId = 1L;

        //when

        ProjectRequest projectRequest = new ProjectRequest();
        projectRequest.setProjectName(projectName);
        projectRequest.setDescription(description);
        projectRequest.setTargetId(targetId);

        Long projectId = projectService.registProject(projectRequest);

        //then
        ProjectResponse projectResponse = projectService.getProjectWithTargetWithAppCount(projectId);

        assertThat(projectResponse.getProjectName()).isEqualTo(projectName);
    }

    @Test
    @DisplayName("프로젝트 조회시 타겟 정보도 함께 조회된다.")
    void getProjectWithTarget() {
        //given
        given(loginUserUtil.getLoginId()).willReturn("admin");

        // when
        ProjectResponse projectResponse = projectService.getProjectWithTargetWithAppCount(1L);

        //then
        assertThat(projectResponse.getRegistLoginId()).isEqualTo("admin");
        assertThat(projectResponse.getTarget().getTargetId()).isNotNull();
        assertThat(projectResponse.getTarget().getOsName()).isNotNull();
        System.out.println(projectResponse.getTarget());
    }


    @Test
    @DisplayName("프로젝트 정보를 수정한다.")
     void modifyProject() {
        //given
        given(loginUserUtil.getLoginId()).willReturn("admin");

        ProjectResponse projectResponse = projectService.getProjectWithTargetWithAppCount(1L);
        Long targetId = projectResponse.getTarget().getTargetId();

        // when
        Long projectId = projectResponse.getProjectId();
        String projectName = projectResponse.getProjectName();
        String newDescription = "new description";

        ProjectRequest projectRequest = new ProjectRequest();
        projectRequest.setProjectName(projectName);
        projectRequest.setDescription(newDescription);
        projectRequest.setTargetId(targetId);

        projectService.modifyProject(projectId, projectRequest);

        //then
        Project project = projectRepository.findById(projectId).get();
        assertThat(project.getDescription()).isEqualTo(newDescription);
    }
}
