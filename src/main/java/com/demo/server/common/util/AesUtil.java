package com.demo.server.common.util;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;

public class AesUtil {

	private static final String KEY = "D9436FC1340D4FB0";

	private static final String KEY_ALGORITHM = "AES";
	private static final String DEFAULT_CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";// 默认的加密算法

	/**
	 * AES 加密操作
	 *
	 * @return 返回16进制的加密数据
	 */
	public static String encrypt(String content) {
		try {
			if (StringUtils.isBlank(content)) {
				return content;
			}
			Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);// 创建密码器

			byte[] byteContent = content.getBytes("utf-8");

			cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(KEY));// 初始化为加密模式的密码器

			byte[] result = cipher.doFinal(byteContent);// 加密

			return Hex.encodeHexString(result);// 转为16进制

		} catch (Exception ex) {
			ex.printStackTrace();
		}

		return null;
	}

	/**
	 * AES 解密操作
	 *
	 */
	public static String decrypt(String content) {

		if (StringUtils.isBlank(content)) {
			return content;
		}

		try {
			// 实例化
			Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);

			// 使用密钥初始化，设置为解密模式
			cipher.init(Cipher.DECRYPT_MODE, getSecretKey(KEY));

			// 执行操作
			byte[] result = cipher.doFinal(Hex.decodeHex(content.toCharArray()));

			return new String(result, "utf-8");
		} catch (Exception ex) {
			ex.printStackTrace();
		}
		return null;
	}

	/**
	 * 生成加密秘钥
	 *
	 */
	private static SecretKeySpec getSecretKey(final String password) {

		return new SecretKeySpec(password.getBytes(), KEY_ALGORITHM);
	}

}
