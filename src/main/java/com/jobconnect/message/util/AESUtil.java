package com.jobconnect.message.util;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;
import java.util.Base64;

/**
 * AES加解密工具类
 * 用于加密和解密聊天消息内容
 */
public class AESUtil {
    // AES密钥（建议从配置文件或环境变量中读取，这里仅为演示）
    private static final String KEY = "1234567890abcdef";

    /**
     * 加密方法
     * @param data 明文字符串
     * @return 加密后的Base64字符串
     * @throws Exception
     */
    public static String encrypt(String data) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        SecretKeySpec secretKey = new SecretKeySpec(KEY.getBytes(), "AES");
        cipher.init(Cipher.ENCRYPT_MODE, secretKey);
        return Base64.getEncoder().encodeToString(cipher.doFinal(data.getBytes("UTF-8")));
    }

    /**
     * 解密方法
     * @param encrypted 加密后的Base64字符串
     * @return 解密后的明文字符串
     * @throws Exception
     */
    public static String decrypt(String encrypted) throws Exception {
        Cipher cipher = Cipher.getInstance("AES/ECB/PKCS5Padding");
        SecretKeySpec secretKey = new SecretKeySpec(KEY.getBytes(), "AES");
        cipher.init(Cipher.DECRYPT_MODE, secretKey);
        return new String(cipher.doFinal(Base64.getDecoder().decode(encrypted)), "UTF-8");
    }
}
