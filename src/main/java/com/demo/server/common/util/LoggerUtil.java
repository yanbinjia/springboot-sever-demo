package com.demo.server.common.util;

import static com.demo.server.common.constant.AppConstant.LOG_SPLIT;

import javax.servlet.http.HttpServletRequest;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.demo.server.common.constant.AppConstant;

public class LoggerUtil {

	public static enum LogLevel {
		INFO, WARN, ERROR;
	}

	private static Logger logger = LoggerFactory.getLogger(AppConstant.LOGGER_ACCESS);

	public static void accessLog(LogLevel level, HttpServletRequest request, String responseStr, String code,
			long cost) {

		String logMsgStr = RequestUtil.getIp(request) + LOG_SPLIT + request.getRequestURI() + LOG_SPLIT
				+ request.getMethod() + LOG_SPLIT + code + LOG_SPLIT + cost + LOG_SPLIT
				+ JSONObject.toJSONString(RequestUtil.getHttpParameter(request)) + LOG_SPLIT + responseStr;

		switch (level) {
		case INFO:
			logger.info(logMsgStr);
			break;
		case WARN:
			logger.warn(logMsgStr);
			break;
		case ERROR:
			logger.error(logMsgStr);
			break;
		default:
			logger.info(logMsgStr);
			break;
		}

	}

}
