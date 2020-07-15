package com.demo.server.common.util;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.*;

import com.auth0.jwt.interfaces.DecodedJWT;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class JwtUtilTest {

    String secret = "202idjdhj3ufn";

    static int expireNSec = 10;

    Map<String, String> claimsMap = null;

    {
        claimsMap = new HashMap<>();
        claimsMap.put("userId", "10029291d_承担");
        claimsMap.put("userName", "承担_JL");
        claimsMap.put("iss", "demo_承担");
        System.out.println(">普通 代码块");
    }

    static {
        System.out.println(">static 代码块 expireNSec" + expireNSec);
    }

    public JwtUtilTest() {
        System.out.println(">JwtUtilTest 构造方法");
    }

    /**
     * @Before:每一个测试用例执行之前运行
     * @After:每一个测试用例执行之后运行
     */

    @Before
    public void befor() throws Exception {
        System.out.println("Before");

    }

    @After
    public void after() throws Exception {
        System.out.println("After");

    }

    @BeforeClass
    public static void beforeClass() throws Exception {
        System.out.println("BeforeClass");

    }

    @AfterClass
    public static void afterClass() throws Exception {
        System.out.println("AfterClass");
    }

    @Test
    public void testCreateToken() {
        String token = JwtUtil.createToken(claimsMap, secret, expireNSec);
        log.debug("jwt token: {}", token);

        assertTrue(token != null && token.length() > 0);
    }

    @Test
    public void testVerifyToken() {
        String token = JwtUtil.createToken(claimsMap, secret, expireNSec);
        log.debug("jwt token: {}", token);
        DecodedJWT jwt = JwtUtil.verifyToken(token, secret);

        assertNotNull(jwt);

        DecodedJWT jwtError = JwtUtil.verifyToken(token + "error-token", secret);

        assertNull(jwtError);

    }

    @Test
    public void testDecodeToken() {
        String token = JwtUtil.createToken(claimsMap, secret, expireNSec);
        log.debug("jwt token: {}", token);

        DecodedJWT jwt = JwtUtil.decodeToken(token);

        assertNotNull(jwt);
        assertNotNull(jwt.getSignature());
        assertNotNull(jwt.getPayload());
        assertNotNull(jwt.getHeader());
    }

    @Test
    public void testGetSignature() {
        String token = JwtUtil.createToken(claimsMap, secret, expireNSec);
        log.debug("jwt token: {}", token);

        String signStr = JwtUtil.getSignature(token);
        assertNotNull(JwtUtil.getSignature(token));
        log.debug("jwt signStr: {}", signStr);

    }

    @Test
    public void testGetPayloadPlain() {
        String token = JwtUtil.createToken(claimsMap, secret, expireNSec);
        log.debug("jwt token: {}", token);

        String payloadPain = JwtUtil.getPayloadPlain(token);
        assertNotNull(payloadPain);
        log.debug("jwt payloadPain: {}", payloadPain);

    }

}
