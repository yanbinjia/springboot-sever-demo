package com.demo.server.common.util;

import com.google.common.base.Charsets;
import com.google.common.hash.Hashing;

public class HashUtil {

	public static String md5(String sourceStr) {
		return Hashing.md5().newHasher().putString(sourceStr, Charsets.UTF_8).hash().toString();
	}

	public static String sha256(String sourceStr) {
		// hashString -> shortcut for newHasher().putString(input, charset).hash()
		// Hashing.sha256().newHasher().putString(sourceStr, Charsets.UTF_8).toString();
		return Hashing.sha256().hashString(sourceStr, Charsets.UTF_8).toString();
	}

	public static void main(String[] args) {
		String sourceStr = "1234";
		System.out.println(sourceStr);
		System.out.println(HashUtil.md5(sourceStr));
		System.out.println(HashUtil.sha256(sourceStr));

	}

}
