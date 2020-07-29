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

    public static String hmacSha256(String sourceStr, String key) {
        return Hashing.hmacSha256(key.getBytes(Charsets.UTF_8)).hashString(sourceStr, Charsets.UTF_8).toString();
    }

    public static void main(String[] args) {
        String keyStr = "241bc6be6b512cc5164c21754bfd7d4c";
        String sourceStr = "1234";
        System.out.println("sourceStr=" + sourceStr + ", md5=" + HashUtil.md5(sourceStr));
        System.out.println("sourceStr=" + sourceStr + ", sha256=" + HashUtil.sha256(sourceStr));
        System.out.println("sourceStr=" + sourceStr + ", hmacSha256=" + HashUtil.hmacSha256(sourceStr, keyStr));

        System.out.println();

        long timestamp = System.currentTimeMillis();
        String paramStr = "id=4" + "&timestamp=" + timestamp + "&key=" + keyStr;
        System.out.println("paramStr:" + paramStr);
        System.out.println("md5=" + HashUtil.md5(paramStr));
        System.out.println("sha256=" + HashUtil.hmacSha256(paramStr, keyStr));

    }

}
