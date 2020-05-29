package com.demo.server.common.util;

import java.io.UnsupportedEncodingException;

import com.google.common.base.Charsets;
import com.google.common.io.BaseEncoding;

public class Base64Util {

	/*
	 * Base64 是一种基于 64 个可打印字符来表示二进制数据的表示方法.
	 * 
	 * 标准的Base64编码后可能出现字符+和/,并且有可能用=补齐位数,在URL中就不能直接作为参数，
	 * 
	 * 所以又有一种"url safe"/"web safe"的base64编码，其实就是把字符+和/分别变成-和_
	 * 
	 * base64(): A-Z a-z 0-9 + / ; base64Url(): A-Z a-z 0-9 - _
	 * 
	 */
	public static String base64Encode(String inputStr) throws UnsupportedEncodingException {
		String outputStr = "";
		outputStr = BaseEncoding.base64().encode(inputStr.getBytes(Charsets.UTF_8));
		return outputStr;
	}

	public static String base64Decode(String inputStr) throws UnsupportedEncodingException {
		String outputStr = "";
		outputStr = new String(BaseEncoding.base64().decode(inputStr), Charsets.UTF_8);
		return outputStr;
	}

	public static String base64UrlEncode(String inputStr) throws UnsupportedEncodingException {
		String outputStr = "";
		outputStr = BaseEncoding.base64Url().encode(inputStr.getBytes(Charsets.UTF_8));
		return outputStr;
	}

	public static String base64UrlDecode(String inputStr) throws UnsupportedEncodingException {
		String outputStr = "";
		outputStr = new String(BaseEncoding.base64Url().decode(inputStr), Charsets.UTF_8);
		return outputStr;
	}

	public static void main(String[] args) throws UnsupportedEncodingException {
		String plainStr = " {\"exp\":1590656498,\"userId\":\"10029291d承担\",\"iat\":1590656496}";
		String base64UrlStrForCheck = "IHsiZXhwIjoxNTkwNjU2NDk4LCJ1c2VySWQiOiIxMDAyOTI5MWTmib_mi4UiLCJpYXQiOjE1OTA2NTY0OTZ9";
		String base64Str = Base64Util.base64Encode(plainStr);
		String base64UrlStr = Base64Util.base64UrlEncode(plainStr);

		String plainStrForCheck = Base64Util.base64UrlDecode(base64UrlStrForCheck);
		System.out.println("plainStr: " + plainStr);
		System.out.println("base64Str: " + base64Str);
		System.out.println("base64UrlStr: " + base64UrlStr);

		System.out.println("base64UrlStrForCheck: " + base64UrlStrForCheck);
		System.out.println("plainStrForCheck: " + plainStrForCheck);
	}
}
