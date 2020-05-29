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

	private static Logger stat = LoggerFactory.getLogger("stat");

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
