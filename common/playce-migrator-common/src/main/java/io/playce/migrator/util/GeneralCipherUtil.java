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
import org.apache.commons.net.util.Base64;

import java.io.DataInputStream;
import java.io.InputStream;
import java.security.Key;
import java.security.KeyFactory;
import java.security.spec.PKCS8EncodedKeySpec;
import java.security.spec.X509EncodedKeySpec;

@Slf4j
public abstract class GeneralCipherUtil {

    private static Key privateKey;
    private static Key publicKey;

    /**
     * Encrypt string.
     */
    public static String encrypt(String plainText) throws PlayceMigratorException {
        if (publicKey == null) {
            try {
                InputStream is = GeneralCipherUtil.class.getClassLoader().getResourceAsStream("migrator_general.pub");

                if (is == null) {
                    throw new RuntimeException("Public key file does not exist.");
                }

                DataInputStream dis = new DataInputStream(is);
                byte[] keyBytes = new byte[dis.available()];
                dis.readFully(keyBytes);

                KeyFactory factory = KeyFactory.getInstance("RSA");

                publicKey = factory.generatePublic(new X509EncodedKeySpec(Base64.decodeBase64(keyBytes)));
            } catch (Exception e) {
                log.error("Unhandled exception occurred while load private key(migrator_general.pub).", e);
                throw new PlayceMigratorException(ErrorCode.PM515A);
            }
        }

        return RSAUtil.encrypt(plainText, publicKey);
    }

    /**
     * Decrypt string.
     */
    public static String decrypt(String cipherText) throws PlayceMigratorException {
        if (privateKey == null) {
            try {
                InputStream is = GeneralCipherUtil.class.getClassLoader().getResourceAsStream("migrator_general.priv");

                if (is == null) {
                    throw new RuntimeException("Private key file does not exist.");
                }

                DataInputStream dis = new DataInputStream(is);
                byte[] keyBytes = new byte[dis.available()];
                dis.readFully(keyBytes);

                KeyFactory factory = KeyFactory.getInstance("RSA");

                privateKey = factory.generatePrivate(new PKCS8EncodedKeySpec(Base64.decodeBase64(keyBytes)));
            } catch (Exception e) {
                log.error("Unhandled exception occurred while load private key(migrator_general.priv).", e);
                throw new PlayceMigratorException(ErrorCode.PM515A);
            }
        }

        return RSAUtil.decrypt(cipherText, privateKey);
    }

    /**
     * The entry point of application.
     */
    public static void main(String[] args) {
        // Base64  encode Test
//        System.err.println(new String(Base64.encodeBase64(FileUtils.readFileToByteArray(new File("/opt/roro/keys/migrator_general_private.key")))));

        // encrypt / decrypt Test
        String password = "admin";
        String encryptedPassword = encrypt(password);

        System.err.println("Encrypted Password : " + encryptedPassword);
        System.err.println("Decrypted Password : " + decrypt(encryptedPassword));
    }
}
