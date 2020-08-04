package com.demo.server.common.utils;

import com.google.common.base.Charsets;
import com.google.common.io.BaseEncoding;

import java.io.UnsupportedEncodingException;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.UUID;

public class Base64Util {

    private Base64Util() {
    }

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

        // 在Java 8中，Base64编码已经成为Java类库的标准。Java 8 内置了 Base64 编码的编码器和解码器。
        // 使用基本编码
        String base64encodedString = Base64.getEncoder().encodeToString("runoob?java8".getBytes(StandardCharsets.UTF_8));
        System.out.println("Base64 编码字符串 (基本) :" + base64encodedString);

        // 解码
        byte[] base64decodedBytes = Base64.getDecoder().decode(base64encodedString);

        System.out.println("原始字符串: " + new String(base64decodedBytes, StandardCharsets.UTF_8));
        base64encodedString = Base64.getUrlEncoder().encodeToString("runoob?java8".getBytes(StandardCharsets.UTF_8));
        System.out.println("Base64 编码字符串 (URL) :" + base64encodedString);

        StringBuilder stringBuilder = new StringBuilder();

        for (int i = 0; i < 10; ++i) {
            stringBuilder.append(UUID.randomUUID().toString());
        }

        byte[] mimeBytes = stringBuilder.toString().getBytes(StandardCharsets.UTF_8);
        String mimeEncodedString = Base64.getMimeEncoder().encodeToString(mimeBytes);
        System.out.println("Base64 编码字符串 (MIME) :" + mimeEncodedString);
    }
}
