package com.demo.server.service.security;

import java.util.Date;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.demo.server.bean.response.Result;
import com.demo.server.bean.response.ResultCode;
import com.demo.server.common.constant.AppConstant;
import com.demo.server.common.util.JwtUtil;
import com.demo.server.config.JwtConfig;

@Service
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
	public Result<String> checkToken(String token) {
		Result<String> result = new Result<>(ResultCode.SEC_TOKEN_ERROR);

		if (StringUtils.isBlank(token) || token.length() < tokenLengthMin) {
			result.setData("");
			result.setCode(ResultCode.SEC_TOKEN_ERROR.code);
			result.setMsg(ResultCode.SEC_TOKEN_ERROR.msg);
			return result;
		}

		token = tokenStrProcess(token);

		// 基本解析
		DecodedJWT jwt = null;
		String sign = "";
		Date expireAt = null;
		String userId = "";

		jwt = JwtUtil.decodeToken(token);

		if (jwt != null) {
			sign = jwt.getSignature();
			expireAt = jwt.getExpiresAt();
			userId = jwt.getClaim(AppConstant.JWT_CLAIM_USER_ID).toString();
		}

		// 验证
		DecodedJWT jwtForVerify = null;
		if (jwt != null && StringUtils.isNotBlank(userId)) {
			jwtForVerify = JwtUtil.verifyToken(token, jwtConfig.getSecretKey());
		}

		if (jwtForVerify != null) {
			// 验证成功
			result.setData("");
			result.setCode(ResultCode.SUCCESS.code);
			result.setMsg(ResultCode.SUCCESS.msg);
		} else {
			// 验证失败
			result.setData("");
			result.setCode(ResultCode.SEC_TOKEN_ERROR.code);
			result.setMsg(ResultCode.SEC_TOKEN_ERROR.msg);
		}

		return result;
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
}
