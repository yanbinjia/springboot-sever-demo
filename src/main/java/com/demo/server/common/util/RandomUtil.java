package com.demo.server.common.util;

import java.util.Random;
import java.util.UUID;

import org.apache.commons.lang3.RandomStringUtils;

public class RandomUtil {

	public static final String NUMBER = "0123456789012345678901234567890123456789";
	public static final String ALPHABET = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ";
	public static final String MIX = ALPHABET + NUMBER;

	public static Random getRandom() {
		return new Random();
	}

	public static String randomString(String baseString, int length) {
		final StringBuilder sb = new StringBuilder();

		if (length < 1) {
			length = 1;
		}
		int baseLength = baseString.length();
		for (int i = 0; i < length; i++) {
			int number = getRandom().nextInt(baseLength);
			sb.append(baseString.charAt(number));
		}
		return sb.toString();
	}

	public static String randomAlphNum(int length) {
		return randomString(MIX, length);
	}

	public static String randomAlphabet(int length) {
		return randomString(ALPHABET, length);
	}

	public static String randomNumeric(int length) {
		return randomString(NUMBER, length);
	}

	/**
	 * match the POSIX [:graph:] regular expression character class. contains all
	 * visible ASCII characters
	 */
	public static String randomGraph(int length) {
		return RandomStringUtils.randomGraph(10);
	}

	public static String uuidWithoutSeparator() {
		return UUID.randomUUID().toString().replace("-", "").toLowerCase();
	}

	public static String uuid() {
		return UUID.randomUUID().toString().toLowerCase();
	}

	public static void main(String[] args) {
		System.out.println("=======RandomUtil===========");
		System.out.println(RandomUtil.randomNumeric(10));
		System.out.println(RandomUtil.randomAlphabet(10));
		System.out.println(RandomUtil.randomAlphNum(10));
		System.out.println(RandomUtil.randomGraph(10));
		System.out.println(RandomUtil.uuid());
		System.out.println(RandomUtil.uuidWithoutSeparator());

		System.out.println("=======Apache Commons-Lang===========");
		// Apache Commons-Lang 包中的 RandomStringUtils 类
		System.out.println(RandomStringUtils.randomAlphabetic(10));
		System.out.println(RandomStringUtils.randomNumeric(10));
		System.out.println(RandomStringUtils.randomAlphanumeric(10));
		System.out.println(RandomStringUtils.randomPrint(10));
		System.out.println(RandomStringUtils.randomAscii(10));
		System.out.println(RandomStringUtils.randomGraph(10));// all visible ASCII characters

		/**
		 * 可移植操作系统接口（Portable Operating System Interface，缩写为POSIX）
		 * 
		 * POSIX字符类型
		 * 
		 * [:alnum:] 字母与数字 [:alpha:] 字母 [:blank:] 空格与制表符 [:cntrl:] 控制字符 [:digit:] 数字
		 * [:graph:] 可打印的与可见的（不包括空格）字符 [:lower:] 小写字符 [:print:] 可打印的字符（包括空白） [:punct:]
		 * 标点 [:space:] 空白 [:upper:] 大写字符 [:xdigit:] 十六进制字符
		 */

	}
}
