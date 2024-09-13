package io.playce.migrator.domain.application.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.playce.migrator.domain.authentication.dto.LoginRequest;
import io.playce.migrator.domain.authentication.dto.LoginResponse;
import io.playce.migrator.util.GeneralCipherUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Locale;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
 * Jaeeon Bae       8월 19, 2022            First Draft.
 */
@SpringBootTest
@AutoConfigureMockMvc
class ApplicationControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private WebApplicationContext wac;
    @Autowired

    private ObjectMapper objectMapper;

    @Autowired
    private MessageSource messageSource;

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
    @DisplayName("애플리케이션을 목록을 조회한다.")
    void 어플리케이션_목록_조회() throws Exception {
        Locale korea = Locale.KOREA;

        headers.setAcceptLanguageAsLocales(Collections.singletonList(korea));
        mockMvc.perform(get("/api/projects/1/applications").contentType(MediaType.APPLICATION_JSON_VALUE)
                        .headers(headers))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE));
    }

    @Test
    @DisplayName("애플리케이션 상세 조회한다.")
    void 어플리케이션_상세_조회() throws Exception {
        Locale korea = Locale.KOREA;

        headers.setAcceptLanguageAsLocales(Collections.singletonList(korea));
        mockMvc.perform(get("/api/projects/1/applications/1")
                        .contentType(MediaType.APPLICATION_JSON_VALUE)
                        .headers(headers))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE));
    }
}