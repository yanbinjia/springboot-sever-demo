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
import com.demo.server.common.exception.AppException;
import com.demo.server.common.util.JwtUtil;
import com.demo.server.common.util.RandomUtil;
import com.demo.server.config.JwtConfig;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TokenService {
	@Autowired
	JwtConfig jwtConfig;

	/**
	 * 校验token
	 * 
	 * @param token
	 * @return
	 */
	public Result<String> checkToken(String token, String userId) {
		Result<String> result = new Result<>(ResultCode.SEC_TOKEN_ERROR);

		if (StringUtils.isBlank(token) || StringUtils.isBlank(userId)) {
			log.warn("checkToken, param error, userId={},token={},", userId, token);
			result.setResultCode(ResultCode.SEC_TOKEN_PARAM);
			return result;
		}

		token = tokenStrProcess(token);

		// 基本解析
		DecodedJWT jwt = null;
		String userIdInToken = "";
		String tokenFor = "";

		jwt = JwtUtil.decodeToken(token);

		if (jwt != null) {
			userIdInToken = jwt.getClaim(AppConstant.JWT_CLAIM_USER_ID).asString();
			tokenFor = jwt.getClaim(AppConstant.JWT_CLAIM_FOR).asString();
		} else {
			result.setResultCode(ResultCode.SEC_TOKEN_ERROR);
			log.warn("checkToken, decodeToken error, userId={},token={}", userId, token);
			return result;
		}

		// tokenFor校验,是否为access
		if (!StringUtils.equals(tokenFor, AppConstant.JWT_CLAIM_FOR_ACC)) {
			result.setResultCode(ResultCode.SEC_TOKEN_ERROR);
			log.warn("checkToken, token中tokenFor不匹配, userId={},tokenFor={}", userId, tokenFor);
			return result;
		}

		// token中uid与请求参数userId不匹配! 防止越权
		if (!StringUtils.equals(userId, userIdInToken)) {
			result.setResultCode(ResultCode.SEC_TOKEN_MISSUID);
			log.warn("checkToken, token中uid与请求参数中uid不匹配, userId={},userIdInToken={}", userId, userIdInToken);
			return result;
		}

		// 验证
		DecodedJWT jwtForVerify = null;
		if (jwt != null && StringUtils.isNotBlank(userId)) {
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
		}

		return result;
	}

	/**
	 * 登录成功后，生成Token(accessToken&refreshToken)
	 * 
	 * @param userId
	 * @return
	 */
	public Token createToken(String userId) {
		Token token = null;

		if (StringUtils.isBlank(userId)) {
			return null;
		}

		token = this.buildNewToken(userId);

		return token;
	}

	/**
	 * 使用refreshToken，刷新生成新的accessToken
	 * 
	 * @param userId
	 * @param refreshToken
	 * @return
	 */
	public Token refreshToken(String userId, String refreshToken) {

		if (StringUtils.isBlank(userId) || StringUtils.isBlank(refreshToken)) {
			throw new AppException(ResultCode.SEC_TOKEN_PARAM);
		}

		// -----------------------------------------------
		// 校验refreshToken
		boolean refreshIsValid = true;
		// TODO: 存储中检查refreshToken是否存在？是否过期？是否禁用？
		// 基本解析
		DecodedJWT jwt = null;
		String userIdInToken = "";
		String tokenFor = "";

		jwt = JwtUtil.decodeToken(refreshToken);

		if (jwt != null) {
			userIdInToken = jwt.getClaim(AppConstant.JWT_CLAIM_USER_ID).asString();
			tokenFor = jwt.getClaim(AppConstant.JWT_CLAIM_FOR).asString();
		} else {
			log.warn("refreshToken, decodeToken error, userId={},token={}", userId, refreshToken);
			throw new AppException(ResultCode.SEC_TOKEN_PARAM);
		}

		// tokenFor校验,是否为access
		if (!StringUtils.equals(tokenFor, AppConstant.JWT_CLAIM_FOR_REF)) {
			log.warn("refreshToken, token中tokenFor不匹配, userId={},tokenFor={}", userId, tokenFor);
			throw new AppException(ResultCode.SEC_TOKEN_PARAM);
		}

		// token中uid与请求参数userId不匹配! 防止越权
		if (!StringUtils.equals(userId, userIdInToken)) {
			log.warn("refreshToken, token中uid与请求参数中uid不匹配, userId={},userIdInToken={}", userId, userIdInToken);
			throw new AppException(ResultCode.SEC_TOKEN_MISSUID);
		}

		// 验证
		DecodedJWT jwtForVerify = null;
		if (jwt != null && StringUtils.isNotBlank(userId)) {
			try {
				Algorithm algorithm = Algorithm.HMAC256(jwtConfig.getSecretKey());
				JWTVerifier verifier = JWT.require(algorithm).build();
				jwtForVerify = verifier.verify(refreshToken);
			} catch (TokenExpiredException e) {
				// 过期
				log.warn("refreshToken JWT verifyToken fail : {}", refreshToken, e);
				throw new AppException(ResultCode.SEC_TOKEN_EXPIRE);
			} catch (Exception e) {
				// 验证失败
				log.warn("refreshToken JWT verifyToken fail : {}", refreshToken, e);
				throw new AppException(ResultCode.SEC_TOKEN_ERROR);
			}
		}

		if (jwtForVerify != null) {
			refreshIsValid = true;
		}

		// TODO: 校验refreshToken时，refreshToken的状态以及过期时间等可以对用户有更多操作

		// -----------------------------------------------
		// 重新生成Token
		Token token = this.buildNewTokenRefresh(userId, refreshToken);

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

		String accessTokenStr = this.createAccessToken(userId);
		String refreshTokenStr = this.createRefreshToken(userId);

		token = new Token();
		token.setUserId(userId);
		token.setAccessToken(accessTokenStr);
		token.setRefreshToken(refreshTokenStr);

		// TODO: 更新token存储,包括jwtId,refreshToken,refreshTokenExpireAt

		return token;
	}

	private Token buildNewTokenRefresh(String userId, String refreshToken) {
		Token token = null;

		String accessToken = this.createAccessToken(userId);

		token = new Token();
		token.setAccessToken(accessToken);
		token.setRefreshToken(refreshToken);

		// TODO: 更新token存储,包括jwtId,refreshToken,refreshTokenExpireAt

		return token;
	}

	private String createAccessToken(String userId) {
		return this.createJwtToken(userId, 1);
	}

	private String createRefreshToken(String userId) {
		return this.createJwtToken(userId, 2);
	}

	private String createJwtToken(String userId, int type) {
		String jwtStr = "";

		// The "jti" (JWT ID) claim provides a unique identifier for the JWT. The "jti"
		// claim can be used to prevent the JWT from being replayed.
		// 可以用来标识和记录颁发的jwt
		String jwtId = this.generateJwtId();

		Map<String, String> claimsMap = new HashMap<>();
		claimsMap.put(AppConstant.JWT_CLAIM_USER_ID, userId);
		claimsMap.put(PublicClaims.JWT_ID, jwtId);
		claimsMap.put(PublicClaims.ISSUER, jwtConfig.getIss());

		int expireAfterNSecs = 3600;

		if (type == 1) {
			// access token
			claimsMap.put(AppConstant.JWT_CLAIM_FOR, AppConstant.JWT_CLAIM_FOR_ACC);
			expireAfterNSecs = jwtConfig.getExpireAfterSecs();
		} else if (type == 2) {
			// refresh token
			claimsMap.put(AppConstant.JWT_CLAIM_FOR, AppConstant.JWT_CLAIM_FOR_REF);
			expireAfterNSecs = jwtConfig.getRefreshExpireAfterSecs();
		}

		jwtStr = JwtUtil.createToken(claimsMap, jwtConfig.getSecretKey(), expireAfterNSecs);

		return jwtStr;
	}

	private String generateJwtId() {
		return RandomUtil.uuidWithoutSeparator();
	}
}
