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
 * Dong-Heon Han    Oct 08, 2022		First Draft.
 */

package io.playce.migrator.domain.websocket.listener;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jws;
import io.jsonwebtoken.SignatureException;
import io.playce.migrator.component.IWebsocketObserver;
import io.playce.migrator.config.JwtProperties;
import io.playce.migrator.constant.ErrorCode;
import io.playce.migrator.domain.authentication.jwt.token.RawAccessJwtToken;
import io.playce.migrator.domain.authentication.service.CustomUserDetailsService;
import io.playce.migrator.domain.websocket.manager.WebsocketSessionManager;
import io.playce.migrator.dto.authentication.SecurityUser;
import io.playce.migrator.exception.PlayceMigratorException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.support.GenericMessage;
import org.springframework.stereotype.Component;
import org.springframework.web.socket.messaging.*;

import java.util.List;
import java.util.Map;

@Component
@RequiredArgsConstructor
@Slf4j
public class WebSocketEventListener {
    private final JwtProperties jwtProperties;
    private final CustomUserDetailsService customUserDetailsService;
    private final WebsocketSessionManager websocketSessionManager;
    private final Map<String, IWebsocketObserver> websocketObserverMap;

    @EventListener
    public void connect(SessionConnectedEvent event) {
        String sessionId = getSessionId(event);
        String tokenString = getToken(event);
        if(tokenString == null) {
            log.error("token is null");
            return;
        }

        RawAccessJwtToken rawToken = new RawAccessJwtToken(tokenString);
        String username = null;
        try {
            Jws<Claims> jwsClaims = rawToken.parseClaims(jwtProperties.getTokenSigningKey());
            Claims claims = jwsClaims.getBody();
            username = getUsername(claims);
        } catch (PlayceMigratorException e) {
            Throwable cause = e.getCause();
            ErrorCode errorCode = e.getErrorCode();
            if(errorCode == ErrorCode.PM508A && cause instanceof ExpiredJwtException) {
                ExpiredJwtException ex = (ExpiredJwtException) cause;
                Claims claims = ex.getClaims();
                username = (String) claims.get("username");
                if (username == null) {
                    username = getUsername(claims);
                }
            } else if(errorCode == ErrorCode.PM506A && cause instanceof SignatureException) {
                log.error("error: {}", cause.getMessage());
            } else {
                throw e;
            }
        }
//        SecurityUser securityUser = (SecurityUser) customUserDetailsService.loadUserByUsername(username);
//
//        if (securityUser == null) {
//            throw new PlayceMigratorException(ErrorCode.PM501A);
//        }
        websocketSessionManager.addSession(sessionId);
        log.debug("connect session id: {}, user: {}", sessionId, username);
    }

    private static String getUsername(Claims claims) {
        String username;
        Map userMap = claims.get("user", Map.class);
        username = String.valueOf(userMap.get("userLoginId"));
        return username;
    }

    @EventListener
    public void disconnect(SessionDisconnectEvent event) {
        String sessionId = getSessionId(event);
        for(IWebsocketObserver observer: websocketObserverMap.values()) {
            observer.removeSession(sessionId);
        }
        websocketSessionManager.removeSession(sessionId);
        log.debug("disconnect session id: {}", sessionId);
    }

    @EventListener
    public void subscribe(SessionSubscribeEvent event) {
        String sessionId = getSessionId(event);
        String destination = getHeaderValue(event, "simpDestination");
        if(websocketSessionManager.checkLogin(sessionId) && websocketObserverMap.containsKey(destination)) {
            IWebsocketObserver websocketObserver = websocketObserverMap.get(destination);
            websocketObserver.setDestination(destination);
            websocketObserver.addSession(sessionId);
            log.debug("  subscribe session id: {}, destination: {}", sessionId, destination);
        }
    }

    @EventListener
    public void unsubscribe(SessionUnsubscribeEvent event) {
        String sessionId = getSessionId(event);
        String destination = getHeaderValue(event, "simpSubscriptionId");
        if(websocketObserverMap.containsKey(destination)) {
            IWebsocketObserver websocketObserver = websocketObserverMap.get(destination);
            websocketObserver.removeSession(sessionId);
            log.debug("unsubscribe session id: {}, destination: {}", sessionId, destination);
        }
    }

    private String getHeaderValue(AbstractSubProtocolEvent event, String messageHeaderKey) {
        MessageHeaders headers = getMessageHeaders(event);
        return headers.get(messageHeaderKey, String.class);
    }

    private String getSessionId(AbstractSubProtocolEvent event) {
        MessageHeaders headers = getMessageHeaders(event);
        return headers.get("simpSessionId", String.class);
    }

    @SuppressWarnings("rawtypes")
    private String getToken(AbstractSubProtocolEvent event) {
        MessageHeaders headers = getMessageHeaders(event);
        GenericMessage genericMessage =  headers.get("simpConnectMessage", GenericMessage.class);
        assert genericMessage != null;
        Map nativeHeaders = genericMessage.getHeaders().get("nativeHeaders", Map.class);
        assert nativeHeaders != null;
        List tokenList = (List) nativeHeaders.get("token");
        if(tokenList != null) {
            return (String) tokenList.get(0);
        }
        return null;
    }

    private static MessageHeaders getMessageHeaders(AbstractSubProtocolEvent event) {
        return event.getMessage().getHeaders();
    }
}