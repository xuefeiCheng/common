package com.ebupt.portal.canyon.common.util;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Random;

/**
 * 加解密工具类
 *
 * @author chy
 * @date 2019-03-11 10:21
 */
@Slf4j
public class EncryptUtil {

	private static final int MIN_SALT = 3;

	private static final String SALTS = "0123456789ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";

	/**
	 * 对字符串进行md5加密
	 *
	 * @param str
	 *              待加密字符串
	 * @return
	 *              md5后的字符串
	 */
	public static String md5(String str) {
		String encryptStr = "";
		try {
			MessageDigest md5 = MessageDigest.getInstance("MD5");

			byte[] byteArray = str.getBytes(StandardCharsets.UTF_8);
			byte[] md5Bytes = md5.digest(byteArray);
			StringBuilder hexValue = new StringBuilder();
			for (byte b: md5Bytes) {
				int val = ((int) b) & 0xff;
				if (val < 16) {
					hexValue.append("0");
				}
				hexValue.append(Integer.toHexString(val));
			}
			encryptStr = hexValue.toString();
		} catch (NoSuchAlgorithmException e) {
			log.error("md5加密失败:{}", e.getMessage());
		}

		return encryptStr;
	}

	/**
	 * 对字符串加盐进行特殊的MD5运算
	 *  1. 若盐值长度小于等于3位，则md5(salt+str)
	 *  2. 若盐值大于3位，则md5(salt.substring(0, 3) + str + salt.substring(3))
	 *
	 * @param inStr
	 *              字符串
	 * @param salt
	 *              MD5盐值
	 * @return
	 *              md5加密后的值
	 */
	public static String md5WithSpecialSalt(String inStr, String salt) {
		if (StringUtils.isNotEmpty(salt)) {
			if (salt.length() > MIN_SALT) {
				inStr = salt.substring(0, 3) + inStr + salt.substring(3);
			} else {
				inStr = salt + inStr;
			}
		}

		return md5(inStr);
	}

	/**
	 * 对字符串进行加盐并进行md5操作
	 *
	 * @param str
	 *              待加密字符串
	 * @param salt
	 *              盐值
	 * @return
	 *              md5后的值
	 */
	public static String md5WithSalt(String str, String salt) {
		return md5(str + salt);
	}

	/**
	 * 随机生成加密所需盐值
	 *
	 * @param len
	 *              盐值长度
	 * @return
	 *              盐值
	 */
	public static String randomSalt(int len) {
		StringBuilder builder = new StringBuilder();
		Random random = new Random();
		for (int i = 0; i < len; i++) {
			int tmp = random.nextInt(SALTS.length());
			builder.append(SALTS.charAt(tmp));
		}
		return builder.toString();
	}



}
