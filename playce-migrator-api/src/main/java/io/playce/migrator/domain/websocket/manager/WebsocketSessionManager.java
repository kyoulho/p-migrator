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

package io.playce.migrator.domain.websocket.manager;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.util.HashSet;
import java.util.Set;

@Component
@Slf4j
public class WebsocketSessionManager {
    private final Object lock = new Object();
    private final Set<String> sessions = new HashSet<>();

    public void addSession(String sessionId) {
        synchronized (lock) {
            sessions.add(sessionId);
            log.debug("add login session: {}", sessionId);
        }
    }

    public void removeSession(String sessionId) {
        synchronized (lock) {
            sessions.remove(sessionId);
            log.debug("remove login session: {}", sessionId);
        }
    }

    public boolean checkLogin(String sessionId) {
        boolean result;
        synchronized (lock) {
            result = sessions.contains(sessionId);
        }
        return result;
    }
}