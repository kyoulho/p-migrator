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
 * Dong-Heon Han    Oct 12, 2022		First Draft.
 */

package io.playce.migrator.component;

import lombok.extern.slf4j.Slf4j;
import org.springframework.messaging.MessageHeaders;
import org.springframework.messaging.simp.SimpMessageHeaderAccessor;
import org.springframework.messaging.simp.SimpMessageType;
import org.springframework.messaging.simp.SimpMessagingTemplate;

import java.util.HashSet;
import java.util.Set;

@Slf4j
public abstract class AbstractWebsocketObserver implements IWebsocketObserver {
    private final Object lock = new Object();
    private final Set<String> sessions = new HashSet<>();
    private String destination;

    @Override
    public void addSession(String sessionId) {
        synchronized (lock) {
            sessions.add(sessionId);
            log.debug("add session: {}", sessionId);
        }
    }

    @Override
    public void removeSession(String sessionId) {
        synchronized (lock) {
            if(sessions.remove(sessionId)) {
                log.debug("remove session: {}", sessionId);
            }
        }
    }

    @Override
    public void setDestination(String destination) {
        if(destination.startsWith("/user")) {
            destination = destination.substring(5);
        }
        this.destination = destination;
    }

    protected void notification(Object message, SimpMessagingTemplate messagingTemplate) {
        synchronized (lock) {
            sessions.forEach(session -> {
                messagingTemplate.convertAndSendToUser(session, destination, message, createHeaders(session));
                log.debug("send '{}' - {}", destination, message);
            });
        }
    }

    public MessageHeaders createHeaders(String sessionId) {
        SimpMessageHeaderAccessor headerAccessor = SimpMessageHeaderAccessor.create(SimpMessageType.MESSAGE);
        headerAccessor.setSessionId(sessionId);
        headerAccessor.setLeaveMutable(true);

        return headerAccessor.getMessageHeaders();
    }
}