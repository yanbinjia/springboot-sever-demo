package com.demo.server.interceptor;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang3.StringUtils;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.alibaba.fastjson.JSONObject;
import com.demo.server.bean.response.Result;
import com.demo.server.bean.response.ResultCode;
import com.demo.server.common.util.LoggerUtil;
import com.demo.server.common.util.LoggerUtil.LogLevel;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class TokenInterceptorAdapter extends HandlerInterceptorAdapter {

	@Override
	public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
			throws Exception {

		String tokenStr = request.getParameter("token");

		if (StringUtils.isBlank(tokenStr)) {
			try {
				response.setContentType("application/json; charset=utf-8");
				PrintWriter out = response.getWriter();

				Result<String> result = new Result<>();
				result.setData("");
				result.setCode(ResultCode.USER_TOKEN_BASE.code);
				result.setMsg(ResultCode.USER_TOKEN_BASE.msg);

				String responseStr = JSONObject.toJSONString(result);

				out.print(responseStr);

				// Record access log.
				long cost = System.currentTimeMillis() - Context.getOrNewInstance().getStartTime();
				LoggerUtil.accessLog(LogLevel.WARN, request, responseStr, String.valueOf(result.getCode()), cost);

				return false; // return false 拦截, 拦截后直接Response客户端
			} catch (IOException e) {
				log.error("", e);
			}
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
