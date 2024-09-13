package io.playce.migrator.domain.target.service;

import io.playce.migrator.dao.repository.TargetRepository;
import io.playce.migrator.dto.target.TargetRequest;
import io.playce.migrator.dto.target.TargetResponse;
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
class TargetServiceTest {
    @Autowired
    TargetService targetService;
    @Autowired
    TargetRepository targetRepository;
    @MockBean
    LoginUserUtil loginUserUtil;

    @Test
    @DisplayName("컬럼으로 타겟을 조회한다.")
    void getTarget() {
        //given
        TargetResponse target = targetService.getTarget(1L);
        String osName = target.getOsName();
        String middlewareName = target.getMiddlewareName();
        String middlewareVersion = target.getMiddlewareVersion();
        String javaVersion = target.getJavaVersion();
        Boolean containerizationYn = target.getContainerizationYn();

        TargetRequest targetRequest = new TargetRequest();
        targetRequest.setOsName(osName);
        targetRequest.setMiddlewareName(middlewareName);
        targetRequest.setMiddlewareVersion(middlewareVersion);
        targetRequest.setJavaVersion(javaVersion);
        targetRequest.setContainerizationYn(containerizationYn);

        TargetResponse byCond = targetService.getTarget(targetRequest);

        assertThat(target.getOsName()).isEqualTo(byCond.getOsName());
        assertThat(target.getMiddlewareName()).isEqualTo(byCond.getMiddlewareName());
        assertThat(target.getMiddlewareVersion()).isEqualTo(byCond.getMiddlewareVersion());
    }

    @Test
    @DisplayName("새로운 타겟을 저장후 반환한다.")
    void getNewTarget() {
        // given
        given(loginUserUtil.getLoginId()).willReturn("admin");

        long orginCount = targetRepository.count();

        // when
        String osName = "linux";
        String middlewareName = "tomcat";
        String middlewareVersion = "10.0";
        String javaVersion = "11";
        Boolean containerizationYn = true;

        TargetRequest targetRequest = new TargetRequest();
        targetRequest.setOsName(osName);
        targetRequest.setMiddlewareName(middlewareName);
        targetRequest.setMiddlewareVersion(middlewareVersion);
        targetRequest.setJavaVersion(javaVersion);
        targetRequest.setContainerizationYn(containerizationYn);

        targetService.getTarget(targetRequest);
        //then
        long afterCount = targetRepository.count();

        assertThat(orginCount + 1).isEqualTo(afterCount);
    }
}