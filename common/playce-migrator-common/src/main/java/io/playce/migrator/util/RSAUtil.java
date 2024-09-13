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
 * Jaeeon Bae       8월 17, 2022            First Draft.
 */
package io.playce.migrator.util;

import io.playce.migrator.constant.ErrorCode;
import io.playce.migrator.exception.PlayceMigratorException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.net.util.Base64;

import javax.crypto.Cipher;
import java.security.Key;

@Slf4j
public abstract class RSAUtil {

    /**
     * Encrypt string.
     */
    public static String encrypt(String plainText, Key key) throws PlayceMigratorException {
        String cipherText;

        try {
            Cipher cipher = Cipher.getInstance("RSA");
            cipher.init(Cipher.ENCRYPT_MODE, key);
            cipherText = Base64.encodeBase64String(cipher.doFinal(plainText.getBytes()));
        } catch (Exception e) {
            log.error("Unhandled exception while encrypt plain text.", e);
            throw new PlayceMigratorException(ErrorCode.PM515A);
        }

        return cipherText.replaceAll("\\r|\\n", "");
    }

    /**
     * Decrypt string.
     */
    public static String decrypt(String cipherText, Key key) throws PlayceMigratorException {
        String plainText = null;

        try {
            if (!StringUtils.isEmpty(cipherText)) {
                Cipher cipher = Cipher.getInstance("RSA");
                cipher.init(Cipher.DECRYPT_MODE, key);
                plainText = new String(cipher.doFinal(Base64.decodeBase64(cipherText)));
            }
        } catch (Exception e) {
            log.error("Unhandled exception while decrypt cipher text.", e);
            throw new PlayceMigratorException(ErrorCode.PM515A);
        }

        return plainText;
    }
}