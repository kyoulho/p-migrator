package io.playce.migrator.domain.common.controller;

import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;

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
@SpringBootTest
@AutoConfigureMockMvc
class CommonControllerTest {

//    @Autowired
//    private MockMvc mockMvc;

//    @Autowired
//    private ObjectMapper objectMapper;

//    @Test
//    void 공통코드_조회() throws Exception {
//        // login token 설정
//        String token = "Bearer eyJhbGciOiJIUzUxMiJ9.eyJzdWIiOiJQbGF5Y2UtTWlncmF0b3IgVXNlciBJbmZvLiIsImlzcyI6Imh0dHBzOi8vd3d3LnBsYXktY2UuaW8iLCJ1c2VyIjp7InVzZXJJZCI6MSwibG9naW5JZCI6ImFkbWluIn0sInJvbGVzIjpbIlJPTEVfQURNSU4iXSwiaWF0IjoxNjYwNzk4NTYxLCJleHAiOjE2NjA4MDkzNjF9.Q3G8sc_AKbz453N5N1d5noQLDT2ROSKChu-SlIJxtOvrUbcaRriLPZROhfuKAt7aOlHtY2GKRiaWW_bK99YU3g";
//
//        mockMvc.perform(get("/api/common/codes")
//                        .contentType(MediaType.APPLICATION_JSON_VALUE)
//                        .header(HttpHeaders.AUTHORIZATION, token))
//                .andDo(print())
//                .andExpect(status().isOk())
//                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON_VALUE));
//    }
}