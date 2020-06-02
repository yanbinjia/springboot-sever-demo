package com.demo.server.service.token;

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

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class TokenService {
	@Autowired
	JwtConfig jwtConfig;

	// token 最小长度
	private int tokenLengthMin = 20;

	public Result<String> checkToken(String token) {
		Result<String> result = new Result<>(-1, "");

		if (StringUtils.isBlank(token) || token.length() < tokenLengthMin) {
			result.setData("");
			result.setCode(ResultCode.USER_TOKEN_BASE.code);
			result.setMsg(ResultCode.USER_TOKEN_BASE.msg);
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
			jwtForVerify = JwtUtil.verifyToken(token, jwtConfig.getSecret());
		}

		if (jwtForVerify != null) {
			// 验证成功
			result.setData("");
			result.setCode(ResultCode.SUCCESS.code);
			result.setMsg(ResultCode.SUCCESS.msg);
		} else {
			// 验证失败
			result.setData("");
			result.setCode(ResultCode.USER_TOKEN_ERROR.code);
			result.setMsg(ResultCode.USER_TOKEN_ERROR.msg);
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
