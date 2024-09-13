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
 * Dong-Heon Han    Aug 24, 2022		First Draft.
 */

package io.playce.migrator.util;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import io.playce.migrator.constant.ErrorCode;
import io.playce.migrator.exception.PlayceMigratorException;
import org.apache.commons.lang3.StringUtils;

import java.lang.reflect.ParameterizedType;

public class JacksonObjectMapperUtil {

    private static final ObjectMapper MAPPER = new ObjectMapper();

    public static <T> T getObject(ObjectMapper objectMapper, String content) {
        assert content != null;

        try {
            TypeReference<T> javaType = new TypeReference<>(){};
            return objectMapper.readValue(content, javaType);
        } catch (JsonProcessingException e) {
            throw new PlayceMigratorException(ErrorCode.PM601J, e.getMessage(), e);
        }
    }

    public static JsonNode readTree(String json) {
        try {
            return MAPPER.readTree(StringUtils.defaultString(json, "[]"));
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    public static <T>  T treeToObj(JsonNode node, TypeReference<T> typeRef) {
        try {
            return MAPPER.treeToValue(node, (Class<T>)((ParameterizedType) typeRef.getType()).getRawType());
        } catch (JsonProcessingException e) {
            throw new RuntimeException(e);
        }
    }
}