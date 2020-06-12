package com.demo.server.common.util;

import javax.crypto.Cipher;
import javax.crypto.spec.SecretKeySpec;

import org.apache.commons.codec.binary.Hex;
import org.apache.commons.lang3.StringUtils;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class AesUtil {

	public static final String KEY_ALGORITHM = "AES";
	public static final String DEFAULT_CIPHER_ALGORITHM = "AES/ECB/PKCS5Padding";// 默认的加密算法
	public static final String CHARSET_UTF8 = "UTF-8";

	/**
	 * AES 加密操作
	 *
	 * @return 返回16进制的加密数据
	 */
	public static String encrypt(final String content, final String password) {
		String encryptStr = content;
		if (StringUtils.isAnyBlank(content, password)) {
			return encryptStr;
		}
		try {
			// 创建密码器
			Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
			byte[] byteContent = content.getBytes(CHARSET_UTF8);
			// 初始化为加密模式的密码器
			cipher.init(Cipher.ENCRYPT_MODE, getSecretKey(password));
			// 加密
			byte[] result = cipher.doFinal(byteContent);
			// 转为16进制
			encryptStr = Hex.encodeHexString(result);
		} catch (Exception ex) {
			log.error("AES encrypt error.", ex);
		}

		return encryptStr;
	}

	/**
	 * AES 解密操作
	 *
	 */
	public static String decrypt(final String content, final String password) {
		String decryptStr = content;
		if (StringUtils.isAnyBlank(content, password)) {
			return decryptStr;
		}
		try {
			// 实例化
			Cipher cipher = Cipher.getInstance(DEFAULT_CIPHER_ALGORITHM);
			// 使用密钥初始化，设置为解密模式
			cipher.init(Cipher.DECRYPT_MODE, getSecretKey(password));
			// 执行操作
			byte[] result = cipher.doFinal(Hex.decodeHex(content.toCharArray()));
			// 转为16进制
			decryptStr = new String(result, CHARSET_UTF8);
		} catch (Exception ex) {
			log.error("AES decrypt error.", ex);
		}
		return decryptStr;
	}

	private static SecretKeySpec getSecretKey(final String password) {
		return new SecretKeySpec(password.getBytes(), KEY_ALGORITHM);
	}

	public static void main(String[] args) {
		String content = "123445666666";
		String password = "fdb2ae47d2505be9"; // 秘钥长度为16(AES128)或32(AES256)
		String encrypt = AesUtil.encrypt(content, password);
		System.out.println("content =" + content);
		System.out.println("encrypt =" + encrypt);
		String decrypt = AesUtil.decrypt(encrypt, password);
		System.out.println("decrypt =" + decrypt);

	}

}
