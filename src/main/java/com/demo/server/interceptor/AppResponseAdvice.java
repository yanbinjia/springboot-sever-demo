package com.demo.server.interceptor;

import javax.servlet.http.HttpServletRequest;

import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.demo.server.bean.response.Result;
import com.demo.server.bean.response.ResultCode;
import com.demo.server.common.util.LoggerUtil;
import com.demo.server.common.util.LoggerUtil.LogLevel;

/**
 * ResponseBodyAdvice接口是在Controller执行return之后，在response返回给客户端之前，执行的对response的一些处理。
 * 比如:可以实现对response数据的统一封装、修改/增加返回值、加解密等操作。
 * 
 * AppResponseAdvice，用来实现请求和响应的数据统一日志功能。也可以在这进行response封装或修改处理。
 * 
 *
 */
@ControllerAdvice
public class AppResponseAdvice implements ResponseBodyAdvice<Object> {

	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
		return converterType.isAssignableFrom(FastJsonHttpMessageConverter.class);
	}

	@Override
	public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
			Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest req,
			ServerHttpResponse resp) {

		long cost = System.currentTimeMillis() - Context.getOrNewInstance().getStartTime();

		HttpServletRequest request = ((ServletServerHttpRequest) req).getServletRequest();

		String codeStr = "";
		String responseStr = "";

		try {
			if (body instanceof Result<?>) {
				codeStr = String.valueOf(((Result<?>) body).getCode());
				responseStr = JSONObject.toJSONString(body);

				// Record access log.
				LoggerUtil.accessLog(LogLevel.INFO, request, responseStr, codeStr, cost);

			} else {
				codeStr = "";
				responseStr = JSONObject.toJSONString(body);

				// Record access log.
				LoggerUtil.accessLog(LogLevel.WARN, request, responseStr, codeStr, cost);
			}

			return body;

		} catch (Exception e) {
			codeStr = String.valueOf(ResultCode.SERVER_UNKONW_ERROR.code);
			responseStr = JSONObject.toJSONString(body);

			// Record access log.
			LoggerUtil.accessLog(LogLevel.ERROR, request, responseStr, codeStr, cost);

			return body;
		}
	}

}
