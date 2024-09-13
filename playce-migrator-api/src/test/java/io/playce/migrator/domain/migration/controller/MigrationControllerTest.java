package io.playce.migrator.domain.migration.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.playce.migrator.constant.ErrorCode;
import io.playce.migrator.dao.repository.MigrationHistoryRepository;
import io.playce.migrator.dao.repository.MigrationRuleLinkRepository;
import io.playce.migrator.domain.authentication.dto.LoginRequest;
import io.playce.migrator.domain.authentication.dto.LoginResponse;
import io.playce.migrator.exception.PlayceMigratorException;
import io.playce.migrator.util.GeneralCipherUtil;
import io.playce.migrator.util.LoginUserUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@Transactional
@AutoConfigureMockMvc
class MigrationControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private WebApplicationContext wac;
    @Autowired
    MigrationController migrationController;
    @MockBean
    LoginUserUtil loginUserUtil;
    @Autowired
    MigrationHistoryRepository migrationHistoryRepository;
    @Autowired
    MigrationRuleLinkRepository migrationRuleLinkRepository;
    @Autowired
    private ObjectMapper objectMapper;
    private HttpHeaders headers = new HttpHeaders();

    @BeforeEach
    void 로그인() throws Exception {
        // mock mvc 설정
        mockMvc = MockMvcBuilders.webAppContextSetup(wac)
                .addFilter(new CharacterEncodingFilter(StandardCharsets.UTF_8.name(), true))
                .build();

        // 로그인 정보 설정
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(GeneralCipherUtil.encrypt("admin"));
        loginRequest.setPassword(GeneralCipherUtil.encrypt("admin"));

        String tokenJson = mockMvc.perform(
                        post("/api/auth/login")
                                .contentType(MediaType.APPLICATION_JSON_VALUE)
                                .content(objectMapper.writeValueAsString(loginRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andReturn().getResponse().getContentAsString();
        LoginResponse loginResponse = objectMapper.readValue(tokenJson, LoginResponse.class);
        headers.setBearerAuth(loginResponse.getToken());
    }

    @Test
    @DisplayName("전환 요청시 전환 이력 생성 및 전환 조건을 저장한다.")
    void requestMigration() {
        // given
        long projectId = 1L;
        long applicationId = 1L;
        String migrationCondition = "[\n" +
                "  {\n" +
                "    \"migrationRuleId\": 1,\n" +
                "    \"migrationCondition\": {\n" +
                "      \"source\": \"192.168.4.16\",\n" +
                "      \"target\": \"192.168.4.17\"\n" +
                "    }\n" +
                "  }]";
        given(loginUserUtil.getLoginId()).willReturn("admin");
        long beforeHistoryCount = migrationHistoryRepository.count();
        long beforeRuleLinkCount = migrationRuleLinkRepository.count();

        //when
        migrationController.requestMigration(projectId, applicationId, migrationCondition);

        //then
        long afterHistoryCount = migrationHistoryRepository.count();
        long afterRuleLinkCount = migrationRuleLinkRepository.count();

        assertEquals(beforeHistoryCount + 1, afterHistoryCount);
        assertEquals(beforeRuleLinkCount + 1, afterRuleLinkCount);
//        assertTrue(AopUtils.isAopProxy(migrationController));
    }

    @Test
    @DisplayName("잘못된 전환 조건 요청시 예외 발생")
    void requestMigrationWithEx() {
        // given
        long projectId = 1L;
        long applicationId = 1L;
        String migrationCondition = "[\n" +
                "  {\n" +
                "    \"migrationRuleIdxxx\": 1,\n" +
                "    \"migrationCondition\": {\n" +
                "      \"source\": \"192.168.4.16\",\n" +
                "      \"target\": \"192.168.4.17\"\n" +
                "    }\n" +
                "  }]";

        //when then
        PlayceMigratorException e = assertThrows(PlayceMigratorException.class, () -> migrationController.requestMigration(projectId, applicationId, migrationCondition));
        assertEquals(e.getErrorCode(), ErrorCode.PM107H);
    }

    @Test
    @DisplayName("마이그레이션 룰 상세 조회")
    void getMigrationRule() throws Exception {
        int migrationRuleId = 1;

        mockMvc.perform(get("/api/settings/migration-rules/" + migrationRuleId)
                .contentType(MediaType.APPLICATION_JSON_VALUE)
                .headers(headers))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE));
    }

    @Test
    @DisplayName("마이그레이션 변경 파일 목록 조회")
    void getChangedFiles() throws Exception {
        int projectId = 1;
        int applicationId = 1;
        int migrationHistoryId = 1;
        String url = "/api/projects/" + projectId  + "/applications/" + applicationId + "/migration-histories/" + migrationHistoryId + "/changed-files";

        mockMvc.perform(get(url)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .headers(headers))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE));
    }

    @Test
    @DisplayName("마이그레이션 Diff 파일 내용 조회")
    void getDiffFiles() throws Exception {
        int projectId = 1;
        int applicationId = 1;
        int migrationHistoryId = 1;
        int changedFileId = 1;

        String url = "/api/projects/" + projectId  + "/applications/"
                + applicationId + "/migration-histories/" + migrationHistoryId + "/changed-files-content/" + changedFileId;

        mockMvc.perform(get(url)
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .headers(headers))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE));
    }
}