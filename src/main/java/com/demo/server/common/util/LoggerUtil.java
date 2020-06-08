package com.demo.server.common.util;

import static com.demo.server.common.constant.AppConstant.LOG_SPLIT;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.demo.server.bean.base.Result;
import com.demo.server.common.constant.AppConstant;

public class LoggerUtil {

	public static enum LogLevel {
		INFO, WARN, ERROR;
	}

	private static Logger accessLogger = LoggerFactory.getLogger(AppConstant.LOGGER_ACCESS);

	private static Logger exceptionLogger = LoggerFactory.getLogger(LoggerUtil.class);

	public static void accessLog(LogLevel level, HttpServletRequest request, String responseStr, String code,
			long cost) {

		String logMsgStr = RequestUtil.getIp(request) + LOG_SPLIT + request.getRequestURI() + LOG_SPLIT
				+ request.getMethod() + LOG_SPLIT + code + LOG_SPLIT + cost + LOG_SPLIT
				+ JSONObject.toJSONString(RequestUtil.getHttpParameter(request)) + LOG_SPLIT + responseStr;

		switch (level) {
		case INFO:
			accessLogger.info(logMsgStr);
			break;
		case WARN:
			accessLogger.warn(logMsgStr);
			break;
		case ERROR:
			accessLogger.error(logMsgStr);
			break;
		default:
			accessLogger.info(logMsgStr);
			break;
		}

	}

	public static void exceptionLog(HttpServletRequest request, Result<?> result, Throwable t) {

		String logMsgStr = RequestUtil.getIp(request) + LOG_SPLIT + request.getRequestURI() + LOG_SPLIT
				+ request.getMethod() + LOG_SPLIT + result.getCode() + LOG_SPLIT
				+ JSONObject.toJSONString(RequestUtil.getHttpParameter(request)) + LOG_SPLIT
				+ JSONObject.toJSONString(result);

		exceptionLogger.error(logMsgStr, t);

	}

}
