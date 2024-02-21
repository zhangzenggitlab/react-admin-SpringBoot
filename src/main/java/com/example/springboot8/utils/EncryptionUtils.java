package com.example.springboot8.utils;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.*;
import javax.crypto.spec.SecretKeySpec;
import java.nio.charset.StandardCharsets;
import java.security.InvalidAlgorithmParameterException;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.util.Base64;

/**
 * 加解密工具
 *
 */
@Component
public class EncryptionUtils {
    private static final String ALGORITHM = "AES"; // 加密算法为AES
    private static final int KEY_SIZE = 128; // AES密钥长度为128位
    @Value("${Encryption.secretKey}")
    private String secretKey; // 自定义密钥

    public  String encrypt(String id) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), ALGORITHM);

        cipher.init(Cipher.ENCRYPT_MODE, secretKeySpec);
        byte[] encryptedData = cipher.doFinal(id.getBytes());

        return Base64.getUrlEncoder().encodeToString(encryptedData);
    }

    public  String decrypt(String encrypted) throws Exception {
        Cipher cipher = Cipher.getInstance(ALGORITHM);
        SecretKeySpec secretKeySpec = new SecretKeySpec(secretKey.getBytes(StandardCharsets.UTF_8), ALGORITHM);

        cipher.init(Cipher.DECRYPT_MODE, secretKeySpec);
        byte[] decodedData = Base64.getUrlDecoder().decode(encrypted);
        byte[] decryptedData = cipher.doFinal(decodedData);

        return new String(decryptedData);
    }
}
