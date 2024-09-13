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
package io.playce.migrator.domain.common.controller;

import io.playce.common.subscription.dto.Subscription;
import io.playce.migrator.domain.common.service.CommonService;
import io.playce.migrator.domain.common.service.SubscriptionService;
import io.playce.migrator.dto.common.CommonCode;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(value = "/api/common", produces = MediaType.APPLICATION_JSON_VALUE)
@RequiredArgsConstructor
public class CommonController {
    private final CommonService commonService;

    private final SubscriptionService subscriptionService;

    @GetMapping(value = "/codes")
    @ResponseStatus(HttpStatus.OK)
    public List<CommonCode> getCommonCodes(@RequestParam(value = "keyword", required = false) String keyword) {
        return commonService.getCommonCodes(keyword);
    }

    @GetMapping(value = "/subscription")
    @ResponseStatus(HttpStatus.OK)
    public Subscription getSubscription() {
        return subscriptionService.getSubscription();
    }
}