package com.demo.server.interceptor;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.alibaba.fastjson.JSONObject;
import com.demo.server.bean.response.Result;
import com.demo.server.bean.response.ResultCode;
import com.demo.server.common.constant.AppConstant;
import com.demo.server.common.util.LoggerUtil;
import com.demo.server.common.util.LoggerUtil.LogLevel;
import com.demo.server.service.token.TokenService;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TokenInterceptorAdapter extends HandlerInterceptorAdapter {
	@Autowired
	TokenService tokenService;

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		// 相对规范的做法，token放在HTTP请求的头信息Authorization字段: Authorization: Bearer <token>
		// 也可采用简洁的做法是，token放在HTTP请求的头信息token字段：token：<token>
		// 更粗暴一点放在body参数中
		// String token = request.getHeader("Authorization");
		// String token = request.getHeader("token");
		// String token = request.getParameter("token");

		String token = request.getHeader("Authorization");

		Result<String> result = tokenService.checkToken(token);

		if (result.getCode() != ResultCode.SUCCESS.code) {
			// 校验失败
			try {
				// 向客户端写响应信息
				response.setContentType("application/json; charset=utf-8");
				PrintWriter out = response.getWriter();
				String responseStr = JSONObject.toJSONString(result);
				out.print(responseStr);

				// Record access log.
				long cost = System.currentTimeMillis() - Context.getOrNewInstance().getStartTime();
				LoggerUtil.accessLog(LogLevel.WARN, request, responseStr, String.valueOf(result.getCode()), cost);

			} catch (IOException e) {
				log.error("", e);
			}

			// return false 拦截，中断后续处理链，返回客户端
			return false;
		} else {
			// 校验成功

		}

		return true;
	}

	@Override
	public void postHandle(HttpServletRequest request, HttpServletResponse response, Object handler,
			ModelAndView modelAndView) throws Exception {
		super.postHandle(request, response, handler, modelAndView);
	}

	@Override
	public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex)
			throws Exception {
		super.afterCompletion(request, response, handler, ex);
	}

}
