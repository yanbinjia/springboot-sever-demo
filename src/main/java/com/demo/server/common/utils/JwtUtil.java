package com.demo.server.common.utils;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator.Builder;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * https://tools.ietf.org/html/rfc7519
 */
public class JwtUtil {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtil.class);

    public static String createToken(Map<String, String> claimsMap, String secret, int expireAfterNSecs) {
        String tokenStr = "";
        Date issueAtDate = new Date();
        Date expiresAtDate = DateUtil.getDateBeforeOrAfterSeconds(issueAtDate, expireAfterNSecs);
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            Builder jwtBuilder = JWT.create().withIssuedAt(issueAtDate).withExpiresAt(expiresAtDate);
            if (claimsMap != null) {
                claimsMap.forEach((k, v) -> {
                    jwtBuilder.withClaim(k, v);
                });
            }
            tokenStr = jwtBuilder.sign(algorithm);
        } catch (Exception e) {
            logger.error("JWT createToken error. ", e);
        }

        return tokenStr;
    }

    public static DecodedJWT verifyToken(String token, String secret) {
        DecodedJWT jwt = null;
        try {
            Algorithm algorithm = Algorithm.HMAC256(secret);
            JWTVerifier verifier = JWT.require(algorithm).build(); // Reusable verifier instance
            jwt = verifier.verify(token);
        } catch (Exception e) {
            logger.error("JWT verifyToken fail : {}", token, e);
        }
        return jwt;
    }

    public static DecodedJWT decodeToken(String token) {
        DecodedJWT jwt = null;
        try {
            jwt = JWT.decode(token);
        } catch (Exception e) {
            logger.error("JWT decodeToken fail : {}", token, e);
        }
        return jwt;
    }

    public static String getSignature(String token) {
        String sign = "";
        try {
            sign = JWT.decode(token).getSignature();
        } catch (Exception e) {
            logger.error("JWT getSignature fail : {}", token, e);
        }
        return sign;
    }

    public static String getPayloadPlain(String token) {
        String payload = "";
        try {
            payload = Base64Util.base64UrlDecode(JWT.decode(token).getPayload());
        } catch (Exception e) {
            logger.error("JWT getPayload fail : {}", token, e);
        }
        return payload;
    }

    public static void main(String[] args) throws Exception {
        String secret = "53fc4a1e39016bce7444119453e5e346s";
        Map<String, String> claimsMap = new HashMap<>();
        claimsMap.put("userId", "10029291");
        claimsMap.put("userName", "承担_JL");
        claimsMap.put("iss", "demo_承担");

        String token = JwtUtil.createToken(claimsMap, secret, 60 * 60 * 24);
        System.out.println(token);

        Thread.sleep(1000 * 1);

        ///
        token = "" + "eyJ0eXAiOiJKV1QiLCJhbGciOiJIUzI1NiJ9"
                + ".eyJpc3MiOiJkZW1vIiwiZXhwIjoxNTkxMzQwODA4LCJpYXQiOjE1OTEzMzcyMDgsInVzZXJJZCI6InlhbmJpbmppYSIsImp0aSI6ImFhZTNjODE1MDhjODRmYWY4MTE0ZmQ1M2QwMjNhYWM3In0"
                + ".ciOXl-WVR_Lbjk6NIS8dFYDw_2Crap5An4KOEFfT6Do";

        DecodedJWT jwtDecoded = JwtUtil.decodeToken(token);

        System.out.println(jwtDecoded.getExpiresAt());

        DecodedJWT jwt = JwtUtil.verifyToken(token, secret);
        if (jwt != null) {
            System.out.println("userId: " + jwt.getClaim("userId").asString());
            System.out.println("expires at: " + jwt.getExpiresAt());
            System.out.println("sign: " + jwt.getSignature());
        }

        System.out.println("payload: " + JwtUtil.getPayloadPlain(token));

        System.out.println("sign: " + JwtUtil.getSignature(token));
        System.out.println("sign: " + JwtUtil.getSignature(token).length());
    }
}
