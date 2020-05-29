package com.demo.server.common.util;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.auth0.jwt.JWT;
import com.auth0.jwt.JWTCreator.Builder;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;

public class JwtUtil {
	private static Logger logger = LoggerFactory.getLogger(JwtUtil.class);

	public static String createToken(Map<String, String> claimsMap, String secret, int expireAfterNSecs) {
		String tokenStr = "";
		Date issueAtDate = new Date();
		Date expiresAtDate = DateUtils.getDateBeforeOrAfterSeconds(issueAtDate, expireAfterNSecs);
		try {
			Algorithm algorithm = Algorithm.HMAC256(secret);
			Builder jwtBuilder = JWT.create().withIssuedAt(issueAtDate).withExpiresAt(expiresAtDate);
			if (claimsMap != null) {
				claimsMap.forEach((k, v) -> {
					jwtBuilder.withClaim(k, v);
				});
			}
			tokenStr = jwtBuilder.sign(algorithm);
		} catch (JWTCreationException e) {
			logger.error("JWT createToken error: Invalid Signing configuration/Couldn't convert Claims. ", e);
		}

		return tokenStr;
	}

	public static DecodedJWT verifyToken(String token, String secret) {
		DecodedJWT jwt = null;
		try {
			Algorithm algorithm = Algorithm.HMAC256(secret);
			JWTVerifier verifier = JWT.require(algorithm).build(); // Reusable verifier instance
			jwt = verifier.verify(token);
		} catch (JWTVerificationException e) {
			logger.error("JWT verifyToken fail : Invalid signature/claims/expired. ", e);
		}
		return jwt;
	}

	public static DecodedJWT decodeToken(String token) {
		DecodedJWT jwt = null;
		try {
			jwt = JWT.decode(token);
		} catch (JWTDecodeException e) {
			logger.error("JWT decodeToken fail : Invalid token. ", e);
		}
		return jwt;
	}

	public static String getSignature(String token) {
		String sign = "";
		try {
			sign = JWT.decode(token).getSignature();
		} catch (JWTDecodeException e) {
			logger.error("JWT getSignature fail : Invalid token. ", e);
		}
		return sign;
	}

	public static String getPayloadPlain(String token) {
		String payload = "";
		try {
			payload = Base64Util.base64UrlDecode(JWT.decode(token).getPayload());
		} catch (JWTDecodeException | UnsupportedEncodingException e) {
			logger.error("JWT getPayload fail : Invalid token/Encoding unsupport. ", e);
		}
		return payload;
	}

	public static void main(String[] args) throws Exception {
		String secret = "202idjdhj3ufn";
		Map<String, String> claimsMap = new HashMap<>();
		claimsMap.put("userId", "10029291d_承担");
		claimsMap.put("userName", "承担_JL");
		claimsMap.put("iss", "demo_承担");

		String token = JwtUtil.createToken(claimsMap, secret, 2);
		System.out.println(token);

		Thread.sleep(1000 * 1);

		DecodedJWT jwt = JwtUtil.verifyToken(token, secret);
		if (jwt != null) {
			System.out.println("userId: " + jwt.getClaim("userId").asString());
			System.out.println("expires at: " + jwt.getExpiresAt());
			System.out.println("sign: " + jwt.getSignature());
		}

		System.out.println("payload: " + JwtUtil.getPayloadPlain(token));
		System.out.println("sign: " + JwtUtil.getSignature(token));

	}
}
