package io.playce.migrator.domain.authentication.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.playce.migrator.config.PlayceMigratorConfig;
import io.playce.migrator.domain.authentication.dto.LoginRequest;
import io.playce.migrator.domain.authentication.dto.LoginResponse;
import io.playce.migrator.util.GeneralCipherUtil;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class AuthenticationControllerTest {

    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @Autowired
    private PlayceMigratorConfig playceMigratorConfig;

    @Test
    @DisplayName("로그인 정상 수행")
    void login() throws Exception {
        // 로그인 정보 설정
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(GeneralCipherUtil.encrypt("admin"));
        loginRequest.setPassword(GeneralCipherUtil.encrypt("admin"));

        mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE));
    }

    @Test
    @DisplayName("버전 정보를 조회한다.")
    void getVersion() throws Exception {
        // 로그인 정보 설정
        LoginRequest loginRequest = new LoginRequest();
        loginRequest.setUsername(GeneralCipherUtil.encrypt("admin"));
        loginRequest.setPassword(GeneralCipherUtil.encrypt("admin"));

        String tokenJson = mockMvc.perform(post("/api/auth/login")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .content(objectMapper.writeValueAsString(loginRequest)))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andReturn().getResponse().getContentAsString();
        LoginResponse loginResponse = objectMapper.readValue(tokenJson, LoginResponse.class);

        HttpHeaders headers = new HttpHeaders();
        headers.setBearerAuth(loginResponse.getToken());

        String versionJson = mockMvc.perform(get("/api/auth/version")
                        .headers(headers))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE))
                .andReturn().getResponse().getContentAsString();

        String version = playceMigratorConfig.getVersion();

        assertThat(versionJson).contains(version);
    }


//    @Test
//    void 리프레시_토큰() throws Exception {
//        // 리프레시 토큰 설정
//        String refreshToken = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJQbGF5Y2UtTWlncmF0b3IgVXNlciBJbmZvLiIsImlzcyI6Imh0dHBzOi8vd3d3LnBsYXktY2UuaW8iLCJsb2dpbklkIjoiYWRtaW4iLCJyb2xlcyI6WyJST0xFX1JFRlJFU0hfVE9LRU4iXSwiaWF0IjoxNjYwNjIwMDA5LCJleHAiOjE2NjA2NjMyMDl9.BTmDaJNR8h_d5-7Eovmb4sAZ2nBW2qP4JdUk4lMPOAO5Hs0igl3kVDsxpjbJLW3l0d4JkVJWcI5fVGhfc-HXFA";
//
//        mockMvc.perform(get("/api/auth/refresh-token")
//                        .contentType(MediaType.APPLICATION_JSON_VALUE)
//                        .header(HttpHeaders.AUTHORIZATION, refreshToken))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE));
//    }
}