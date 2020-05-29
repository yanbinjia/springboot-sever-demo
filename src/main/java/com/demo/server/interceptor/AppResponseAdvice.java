package com.demo.server.interceptor;

import static com.demo.server.common.constant.AppConstant.LOG_SPLIT;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import com.demo.server.common.util.RequestUtil;

@ControllerAdvice
public class AppResponseAdvice implements ResponseBodyAdvice<Object> {

	private static Logger stat = LoggerFactory.getLogger("stat");

	@Override
	public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
		return converterType.isAssignableFrom(FastJsonHttpMessageConverter.class);
	}

	@Override
	public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
			Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest req,
			ServerHttpResponse response) {
		long cost = System.currentTimeMillis() - Context.getOrNewInstance().getStartTime();
		ServletServerHttpRequest servletServerHttpRequest = (ServletServerHttpRequest) req;
		HttpServletRequest request = servletServerHttpRequest.getServletRequest();

		String logMsgStr = "";
		String codeStr = "";

		try {
			if (body instanceof Result<?>) {
				codeStr = String.valueOf(((Result<?>) body).getCode());
				logMsgStr = RequestUtil.getIp(request) + LOG_SPLIT + request.getRequestURI() + LOG_SPLIT
						+ request.getMethod() + LOG_SPLIT + codeStr + LOG_SPLIT + cost + LOG_SPLIT
						+ JSONObject.toJSONString(RequestUtil.getHttpParameter(request)) + LOG_SPLIT
						+ JSONObject.toJSONString(body);

				stat.info(logMsgStr);

			} else {
				codeStr = "";
				logMsgStr = RequestUtil.getIp(request) + LOG_SPLIT + request.getRequestURI() + LOG_SPLIT
						+ request.getMethod() + LOG_SPLIT + codeStr + LOG_SPLIT + cost + LOG_SPLIT
						+ JSONObject.toJSONString(RequestUtil.getHttpParameter(request)) + LOG_SPLIT
						+ JSONObject.toJSONString(body);

				stat.warn(logMsgStr);
			}

			return body;

		} catch (Exception e) {
			codeStr = String.valueOf(ResultCode.SERVER_UNKONW_ERROR.code);
			logMsgStr = RequestUtil.getIp(request) + LOG_SPLIT + request.getRequestURI() + LOG_SPLIT
					+ request.getMethod() + LOG_SPLIT + codeStr + LOG_SPLIT + cost + LOG_SPLIT
					+ JSONObject.toJSONString(RequestUtil.getHttpParameter(request)) + LOG_SPLIT
					+ JSONObject.toJSONString(body);

			stat.error(logMsgStr, e);
			return body;
		}
	}

}