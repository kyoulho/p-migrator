package io.playce.migrator.domain.exception.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import io.playce.migrator.constant.ErrorCode;
import io.playce.migrator.constant.ErrorType;
import io.playce.migrator.domain.authentication.dto.LoginRequest;
import io.playce.migrator.domain.authentication.dto.LoginResponse;
import io.playce.migrator.dto.exception.ErrorResponse;
import io.playce.migrator.exception.PlayceMigratorException;
import io.playce.migrator.util.GeneralCipherUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.MessageSource;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.filter.CharacterEncodingFilter;

import java.nio.charset.StandardCharsets;
import java.util.Collections;
import java.util.Locale;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
class GlobalExceptionHandlerTest {

    MockMvc mockMvc;
    @Autowired
    WebApplicationContext wac;
    @Autowired
    ObjectMapper objectMapper;
    @Autowired
    MessageSource messageSource;
    HttpHeaders headers = new HttpHeaders();
    static ErrorCode errorCode = ErrorCode.PM101H;


    @BeforeEach
    void login() throws Exception {
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
    @DisplayName("예외 발생시 errorResponse 를 반환한다.")
    void errorResponse() throws Exception {
        String responseJson = mockMvc.perform(
                        get("/api/test/ex")
                                .headers(headers))
                .andDo(print())
                .andExpect(status().is(errorCode.getHttpStatus().value()))
                .andReturn().getResponse().getContentAsString();

        assertDoesNotThrow(() -> objectMapper.readValue(responseJson, ErrorResponse.class));
    }

    @Test
    @DisplayName("locale 설정에 맞는 message 를 반환한다.")
    void errorMessageLocale() throws Exception {
        //given
        Locale korea = Locale.KOREA;
        String message = messageSource.getMessage(ErrorType.messageCode(errorCode), null, null, korea);

        //when
        headers.setAcceptLanguageAsLocales(Collections.singletonList(korea));
        String responseJson = mockMvc.perform(
                        get("/api/test/ex")
                                .headers(headers))
                .andDo(print())
                .andExpect(status().is(errorCode.getHttpStatus().value()))
                .andReturn().getResponse().getContentAsString();

        ErrorResponse errorResponse = objectMapper.readValue(responseJson, ErrorResponse.class);

        //then
        String responseErrorCode = errorResponse.getErrorCode();

        assertThat(responseErrorCode).contains(errorCode.toString());
        assertThat(responseJson).contains(message);

    }

    @TestConfiguration
    static class TestConfig {
        @Bean
        public TestController testController() {
            return new TestController();
        }
    }

    @RestController
    static class TestController {
        @GetMapping("/api/test/ex")
        public void ex() {
            throw new PlayceMigratorException(errorCode);
        }
    }

}
