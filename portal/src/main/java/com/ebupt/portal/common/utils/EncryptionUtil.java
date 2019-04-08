package com.ebupt.portal.common.utils;

import lombok.extern.slf4j.Slf4j;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@Slf4j
public class EncryptionUtil {

    public static String md5(String inStr) {
        String encryptStr = "";

        try {
            MessageDigest md5 = MessageDigest.getInstance("MD5");

            byte[] byteArray = inStr.getBytes();
            byte[] md5Bytes = md5.digest(byteArray);
            StringBuilder hexValue = new StringBuilder();
            for (byte b: md5Bytes) {
                int val = ((int) b) & 0xff;
                if (val < 16)
                    hexValue.append("0");
                hexValue.append(Integer.toHexString(val));
            }
            encryptStr = hexValue.toString();
        } catch (NoSuchAlgorithmException e) {
            log.error("md5加密失败:{}", e.getMessage());
        }

        return encryptStr;

    }

}
