package com.demo.server.interceptor;

import java.io.IOException;
import java.io.PrintWriter;
import java.lang.reflect.Method;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.alibaba.fastjson.JSONObject;
import com.demo.server.bean.base.Result;
import com.demo.server.bean.base.ResultCode;
import com.demo.server.common.constant.AppConstant;
import com.demo.server.common.util.LoggerUtil;
import com.demo.server.common.util.LoggerUtil.LogLevel;
import com.demo.server.service.security.TokenService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
public class TokenInterceptor extends HandlerInterceptorAdapter {
	@Autowired
	TokenService tokenService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {
		// -----------------------------------------------------
		// 获取token参数
		// 相对规范的做法，token放在HTTP请求的头信息Authorization字段: 即 Authorization: Bearer <token>
		// 也可采用简洁的做法是，token放在HTTP请求的头信息token字段：即 token：<token>
		// 更粗暴一点放在body参数中，因为token比较长，不建议这么干
		// String token = request.getHeader("Authorization");
		// String token = request.getHeader("token");
		// String token = request.getParameter("token");

		// token
		String token = request.getHeader(AppConstant.AUTH_HEADER_NAME);
		// 需要验证token的请求参数中必须有userId，用于校验token和userId的关系
		String userId = request.getParameter(AppConstant.AUTH_UID_PARAM_NAME);

		// -----------------------------------------------------
		// 检查注解
		if (!(handler instanceof HandlerMethod)) {
			// 如果不是映射到方法直接通过
			return true;
		}
		HandlerMethod handlerMethod = (HandlerMethod) handler;
		Method method = handlerMethod.getMethod();
		// 检查是否有@TokenPass注解，有则跳过校验
		if (method.isAnnotationPresent(TokenPass.class)) {
			return true;
		}

		// -----------------------------------------------------
		// token 验证
		Result<String> result = tokenService.checkToken(token, userId);

		if (result.getCode() != ResultCode.SUCCESS.code) {
			// 校验失败
			try {
				// 向客户端写响应信息
				response.setContentType("application/json; charset=utf-8");
				PrintWriter out = response.getWriter();
				String responseStr = JSONObject.toJSONString(result);
				out.print(responseStr);

				// Record access log.
				long cost = System.currentTimeMillis() - TraceContext.getInstance().getStartTime();
				LoggerUtil.accessLog(LogLevel.WARN, request, responseStr, String.valueOf(result.getCode()), cost);

			} catch (IOException e) {
				log.error("", e);
			}

			// return false 拦截，中断后续处理链，返回客户端
			return false;
		}

		return true;
	}

}
