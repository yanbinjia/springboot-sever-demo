package com.demo.server.service.security;

import java.util.HashMap;
import java.util.Map;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.TokenExpiredException;
import com.auth0.jwt.impl.PublicClaims;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.auth0.jwt.interfaces.JWTVerifier;
import com.demo.server.bean.base.Result;
import com.demo.server.bean.base.ResultCode;
import com.demo.server.bean.vo.Token;
import com.demo.server.common.constant.AppConstant;
import com.demo.server.common.util.JwtUtil;
import com.demo.server.common.util.RandomUtil;
import com.demo.server.config.JwtConfig;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TokenService {
	@Autowired
	JwtConfig jwtConfig;

	// token 最小长度
	private int tokenLengthMin = 20;

	/**
	 * 本例采用 jwt token
	 * 
	 * @param token
	 * @return
	 */
	public Result<String> checkToken(String token, String userId) {
		Result<String> result = new Result<>(ResultCode.SEC_TOKEN_ERROR);

		if (StringUtils.isBlank(token) || token.length() < tokenLengthMin || StringUtils.isBlank(userId)) {
			log.warn("checkToken, param error,userId={},token={},", userId, token);
			result.setResultCode(ResultCode.SEC_TOKEN_PARAM);
			return result;
		}

		token = tokenStrProcess(token);

		// 基本解析
		DecodedJWT jwt = null;
		String userIdInToken = "";

		jwt = JwtUtil.decodeToken(token);

		if (jwt != null) {
			userIdInToken = jwt.getClaim(AppConstant.JWT_CLAIM_USER_ID).asString();
		}

		// token中uid与请求参数userId不匹配! 防止越权
		if (!StringUtils.equals(userId, userIdInToken) || StringUtils.isBlank(userId)) {
			result.setResultCode(ResultCode.SEC_TOKEN_MISSUID);
			log.warn("checkToken, token中uid与请求参数中uid不匹配, userId={},userIdInToken={}", userId, userIdInToken);
			return result;
		}

		// 验证
		DecodedJWT jwtForVerify = null;
		if (jwt != null && StringUtils.isNotBlank(userId)) {
			// jwtForVerify = JwtUtil.verifyToken(token, jwtConfig.getSecretKey());
			try {
				Algorithm algorithm = Algorithm.HMAC256(jwtConfig.getSecretKey());
				JWTVerifier verifier = JWT.require(algorithm).build();
				jwtForVerify = verifier.verify(token);
			} catch (TokenExpiredException e) {
				// 过期
				result.setResultCode(ResultCode.SEC_TOKEN_EXPIRE);
				return result;
			} catch (Exception e) {
				// 验证失败
				log.warn("checkToken JWT verifyToken fail : {}", token, e);
			}
		}

		if (jwtForVerify != null) {
			// 验证成功
			result.setResultCode(ResultCode.SUCCESS);
		} else {
			// 验证失败
			result.setResultCode(ResultCode.SEC_TOKEN_ERROR);
		}

		return result;
	}

	public Token createToken(String userId) {

		if (StringUtils.isBlank(userId)) {
			return null;
		}

		Token token = this.buildNewToken(userId);

		return token;
	}

	public Token refreshToken(String userId, String refreshToken) {

		if (StringUtils.isBlank(userId) || StringUtils.isBlank(refreshToken)) {
			return null;
		}

		// -----------------------------------------------
		// 校验refreshToken
		boolean refreshIsValid = true;
		// TODO: 存储中检查refreshToken是否存在？是否过期？是否禁用？
		// ... ...

		if (!refreshIsValid) {
			return null;
		}

		// TODO: 校验refreshToken时，从存储中取出过期时间refreshTokenExpireAt
		long refreshTokenExpireAt = this.getMillisBeforeOrAfterSeconds(jwtConfig.getRefreshExpireAfterSecs());

		// -----------------------------------------------
		// 重新生成Token
		Token token = this.buildNewTokenRefresh(userId, refreshToken, refreshTokenExpireAt);

		return token;
	}

	/*
	 * HTTP请求的头信息 Authorization:Bearer <token> , 解析出<token>
	 * 
	 */
	public String tokenStrProcess(String token) {
		String tokenAferProcess = token;
		if (StringUtils.isNotBlank(token) && token.startsWith("Bearer")) {
			String[] tokenArray = StringUtils.splitByWholeSeparator(token, null);
			if (tokenArray.length >= 2) {
				tokenAferProcess = tokenArray[1].trim();
			}
		}
		return tokenAferProcess;
	}

	private Token buildNewToken(String userId) {
		Token token = null;

		String jwtStr = this.createJwtToken(userId);

		String refreshTokenStr = this.generateRefreshToken();

		token = new Token();
		long accessTokenExpireAt = this.getMillisBeforeOrAfterSeconds(jwtConfig.getExpireAfterSecs());
		long refreshTokenExpireAt = this.getMillisBeforeOrAfterSeconds(jwtConfig.getRefreshExpireAfterSecs());
		token.setUserId(userId);
		token.setAccessToken(jwtStr);
		token.setRefreshToken(refreshTokenStr);
		token.setAccessTokenExpireAt(accessTokenExpireAt);
		token.setRefreshTokenExpireAt(refreshTokenExpireAt);

		// TODO: 更新token存储,包括jwtId,refreshToken,refreshTokenExpireAt

		return token;
	}

	private Token buildNewTokenRefresh(String userId, String refreshToken, long refreshTokenExpireAt) {
		Token token = null;

		String jwtStr = this.createJwtToken(userId);

		String refreshTokenStr = refreshToken;

		token = new Token();
		long accessTokenExpireAt = this.getMillisBeforeOrAfterSeconds(jwtConfig.getExpireAfterSecs());
		token.setAccessToken(jwtStr);
		token.setRefreshToken(refreshTokenStr);
		token.setAccessTokenExpireAt(accessTokenExpireAt);
		token.setRefreshTokenExpireAt(refreshTokenExpireAt);// 使用原refreshToken的过期时间

		// TODO: 更新token存储,包括jwtId,refreshToken,refreshTokenExpireAt

		return token;
	}

	private String createJwtToken(String userId) {
		String jwtStr = "";

		// The "jti" (JWT ID) claim provides a unique identifier for the JWT.
		// The "jti" claim can be used to prevent the JWT from being replayed.
		// 可以用来标识和记录颁发的jwt
		String jwtId = this.generateJwtId();

		Map<String, String> claimsMap = new HashMap<>();
		claimsMap.put(AppConstant.JWT_CLAIM_USER_ID, userId);
		claimsMap.put(PublicClaims.JWT_ID, jwtId);
		claimsMap.put(PublicClaims.ISSUER, jwtConfig.getIss());

		jwtStr = JwtUtil.createToken(claimsMap, jwtConfig.getSecretKey(), jwtConfig.getExpireAfterSecs());

		return jwtStr;
	}

	private String generateJwtId() {
		return RandomUtil.uuidWithoutSeparator();
	}

	private String generateRefreshToken() {
		return RandomUtil.uuidWithoutSeparator();
	}

	private long getMillisBeforeOrAfterSeconds(int seconds) {
		return System.currentTimeMillis() + seconds * 1000L; // 记得加L
	}
}
