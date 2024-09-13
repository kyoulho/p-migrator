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
 * Dong-Heon Han    Aug 18, 2022		First Draft.
 */

package io.playce.migrator.scheduler.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;

@Configuration
public class TaskExecutorConfig {
    @Bean @Autowired
    public Executor analysisExecutor(ThreadConfig threadConfig) {
        ThreadConfig.ThreadPoolProperties threadPoolProperties = threadConfig.getAnalysisProperties();
        return getThreadPoolTaskExecutor(threadPoolProperties);
    }
    @Bean @Autowired
    public Executor migrationExecutor(ThreadConfig threadConfig) {
        ThreadConfig.ThreadPoolProperties threadPoolProperties = threadConfig.getMigrationProperties();
        return getThreadPoolTaskExecutor(threadPoolProperties);
    }
    @Bean @Autowired
    public Executor workerExecutor(ThreadConfig threadConfig) {
        ThreadConfig.ThreadPoolProperties threadPoolProperties = threadConfig.getWorkerProperties();
        return getThreadPoolTaskExecutor(threadPoolProperties);
    }

    private static ThreadPoolTaskExecutor getThreadPoolTaskExecutor(ThreadConfig.ThreadPoolProperties threadPoolProperties) {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(threadPoolProperties.getCorePoolSize());
        executor.setMaxPoolSize(threadPoolProperties.getMaxPoolSize());
        executor.setQueueCapacity(threadPoolProperties.getQueueCapacity());
        executor.setThreadNamePrefix(threadPoolProperties.getNamePrefix());
        executor.initialize();
        return executor;
    }
}