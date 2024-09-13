package io.playce.migrator.domain.application.service;

import io.playce.migrator.dao.entity.Application;
import io.playce.migrator.dao.repository.ApplicationRepository;
import io.playce.migrator.domain.common.service.SubscriptionService;
import io.playce.migrator.domain.credential.service.CredentialService;
import io.playce.migrator.domain.project.service.ProjectService;
import io.playce.migrator.domain.target.service.TargetService;
import io.playce.migrator.dto.application.ApplicationRequest;
import io.playce.migrator.dto.credential.CredentialRequest;
import io.playce.migrator.dto.project.ProjectRequest;
import io.playce.migrator.dto.target.TargetRequest;
import io.playce.migrator.dto.target.TargetResponse;
import io.playce.migrator.util.LoginUserUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@SpringBootTest
class ScmApplicationServiceTest {
    @Autowired
    private ProjectService projectService;
    @Autowired
    private ScmApplicationService scmApplicationService;
    @Autowired
    private ApplicationRepository applicationRepository;
    @Autowired
    private CredentialService credentialService;
    @Autowired
    private TargetService targetService;
    @MockBean
    private SubscriptionService subscriptionService;
    @MockBean
    private LoginUserUtil loginUserUtil;

    @Test
    @DisplayName("개인키로 SCM 인증에 성공후 어플리케이션을 등록한다")
    @Transactional
    void createApplication() throws IOException {
        // given
        given(subscriptionService.checkSubscription()).willReturn(true);
        given(loginUserUtil.getLoginId()).willReturn("admin");
        TargetRequest targetRequest = new TargetRequest();
        targetRequest.setMiddlewareName("tomcat");
        targetRequest.setOsName("linux");
        targetRequest.setJavaVersion("jdk11");
        TargetResponse targetResponse = targetService.getTarget(targetRequest);

        ProjectRequest projectRequest = new ProjectRequest();
        projectRequest.setTargetId(targetResponse.getTargetId());
        projectRequest.setProjectName("project");
        Long projectId = projectService.registProject(projectRequest);

        String keyContent = Files.readString(Path.of("/Users/jinhyunkyun/.ssh/id_rsa")).trim();
        CredentialRequest credentialRequest = new CredentialRequest();
        credentialRequest.setUsername("root");
        credentialRequest.setPasswordYn(false);
        credentialRequest.setKeyContent(keyContent);
        Long credentialId = credentialService.registCredential(credentialRequest);

        // when
        ApplicationRequest applicationRequest = new ApplicationRequest();
        applicationRequest.setScmYn(true);
        applicationRequest.setScmUrl("git@gitlab-qa.opensourcelab.co.kr:gitlab-instance-0a50da20/osci_scm_test.git");
        applicationRequest.setTargetId(targetResponse.getTargetId());
        applicationRequest.setCredentialId(credentialId);
        applicationRequest.setBranchName("main");
        applicationRequest.setMiddlewareType("tomcat");
        applicationRequest.setJavaVersion("jdk8");

        Long applicationId = scmApplicationService.createApplication(projectId, applicationRequest);
        Application application = applicationRepository.findById(applicationId).get();

        assertThat(application).isNotNull();
        assertThat(application.getOriginFileName()).isEqualTo("main");
    }
}