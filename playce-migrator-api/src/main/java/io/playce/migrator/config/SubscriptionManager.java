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
 * Author			Date				Description
 * ---------------	----------------	------------
 * Dong-Heon Han    Oct 31, 2022		First Draft.
 */

package io.playce.migrator.config;

import io.playce.common.subscription.dto.Subscription;
import io.playce.common.subscription.enums.SubscriptionStatusType;
import io.playce.common.subscription.manager.SubscriptionLoader;
import io.playce.migrator.constant.ErrorCode;
import io.playce.migrator.exception.PlayceMigratorException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletRequest;
import java.util.List;

@Component
@Aspect
@RequiredArgsConstructor
@Slf4j
public class SubscriptionManager {
    private final SubscriptionLoader subscriptionLoader;
    private final List<String> allowUris = List.of("/api/auth", "/api/common");

    @Before("@annotation(org.springframework.web.bind.annotation.PostMapping)" +
            " || @annotation(org.springframework.web.bind.annotation.PatchMapping)" +
            " || @annotation(org.springframework.web.bind.annotation.DeleteMapping)" +
            " || @annotation(org.springframework.web.bind.annotation.PutMapping)")
//    @Before("@annotation(org.springframework.web.bind.annotation.GetMapping)")
    public void checkSubscriptionForOther(JoinPoint joinPoint) {
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes()).getRequest();

        String requestURI = request.getRequestURI();
        if(checkUrl(requestURI)) return;

        checkSubscription(subscriptionLoader.getSubscription());
    }

    private boolean checkUrl(String url) {
        for(String uri: allowUris) {
            if(url.contains(uri)) return true;
        }
        return false;
    }

    private void checkSubscription(Subscription subscription) {
        if(subscription.getSubscriptionStatusType() == SubscriptionStatusType.SUBSCRIPTION_NOT_FOUND) {
            throw new PlayceMigratorException(ErrorCode.PM901SUB);
        }

        if(subscription.getSubscriptionStatusType() == SubscriptionStatusType.SUBSCRIPTION_INVALID) {
            throw new PlayceMigratorException(ErrorCode.PM902SUB);
        }

        if(subscription.getSubscriptionStatusType() == SubscriptionStatusType.SIGNATURE_NOT_MATCH) {
            throw new PlayceMigratorException(ErrorCode.PM903SUB);
        }

        updateStatus(subscriptionLoader.getSubscription());
        if(subscription.getSubscriptionStatusType() == SubscriptionStatusType.SUBSCRIPTION_EXPIRED) {
            throw new PlayceMigratorException(ErrorCode.PM904SUB);
        }
    }

    private void updateStatus(Subscription subscription) {
        if(subscription.getExpireDate() < System.currentTimeMillis()) {
            subscription.setSubscriptionStatusType(SubscriptionStatusType.SUBSCRIPTION_EXPIRED);
        }
    }
}