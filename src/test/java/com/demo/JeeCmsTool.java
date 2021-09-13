package com.demo;

import java.io.UnsupportedEncodingException;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

import com.google.common.io.BaseEncoding;
import org.apache.commons.codec.binary.Hex;

import javax.crypto.KeyGenerator;
import javax.crypto.SecretKey;

public class JeeCmsTool {

    private String defaultSalt = "";

    public JeeCmsTool(String defaultSalt) {
        this.defaultSalt = defaultSalt;
    }

    public String encodePassword(String rawPass) {
        return encodePassword(rawPass, defaultSalt);
    }

    public String encodePassword(String rawPass, String salt) {
        String saltedPass = mergePasswordAndSalt(rawPass, salt, false);
        MessageDigest messageDigest = getMessageDigest();
        byte[] digest;
        try {
            digest = messageDigest.digest(saltedPass.getBytes("UTF-8"));
        } catch (UnsupportedEncodingException e) {
            throw new IllegalStateException("UTF-8 not supported!");
        }
        return new String(Hex.encodeHex(digest));
    }

    protected final MessageDigest getMessageDigest() {
        String algorithm = "MD5";
        try {
            return MessageDigest.getInstance(algorithm);
        } catch (NoSuchAlgorithmException e) {
            throw new IllegalArgumentException("No such algorithm ["
                    + algorithm + "]");
        }
    }

    protected String mergePasswordAndSalt(String password, Object salt,
                                          boolean strict) {
        if (password == null) {
            password = "";
        }
        if (strict && (salt != null)) {
            if ((salt.toString().lastIndexOf("{") != -1)
                    || (salt.toString().lastIndexOf("}") != -1)) {
                throw new IllegalArgumentException(
                        "Cannot use { or } in salt.toString()");
            }
        }
        if ((salt == null) || "".equals(salt)) {
            return password;
        } else {
            return password + "{" + salt.toString() + "}";
        }
    }

    public String generateShiroRememberMeCipherKey() {
        KeyGenerator keygen = null;
        try {
            keygen = KeyGenerator.getInstance("AES");
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }
        SecretKey deskey = keygen.generateKey();
        return BaseEncoding.base64().encode(deskey.getEncoded());
    }

    public static void main(String[] args) {
        JeeCmsTool jeeCmsTool = new JeeCmsTool("");

        // jeecms v8 重置密码
        System.out.println(jeeCmsTool.encodePassword("OnoTN7d0lSKP"));
        System.out.println(jeeCmsTool.encodePassword("Ka5JOMYQM6kv"));

        // Apache Shiro 反序列化 Shiro550 CVE-2016-4437
        // 生成AES密钥，rememberMeManager 中设置 cipherKey，#{T(org.apache.shiro.codec.Base64).decode('r1fpIHp0Ltfep3zUK4UVZA==')}
        System.out.println(jeeCmsTool.generateShiroRememberMeCipherKey());
    }
}
