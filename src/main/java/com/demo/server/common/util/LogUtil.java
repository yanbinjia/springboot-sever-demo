package com.demo.server.common.util;

import static com.demo.server.common.constant.AppConstant.LOG_SPLIT;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alibaba.fastjson.JSONObject;
import com.demo.server.bean.base.Result;
import com.demo.server.common.constant.AppConstant;

public class LogUtil {

    public static enum LogLevel {
        INFO, WARN, ERROR;
    }

    private static Logger accessLogger = LoggerFactory.getLogger(AppConstant.LOGGER_ACCESS);

    private static Logger exceptionLogger = LoggerFactory.getLogger(LogUtil.class);

    public static void accessLog(LogLevel level, HttpServletRequest request, String responseStr, String code, long cost) {

        StringBuilder logSb = new StringBuilder();
        logSb.append(RequestUtil.getIp(request));
        logSb.append(LOG_SPLIT);
        logSb.append(request.getRequestURI());
        logSb.append(LOG_SPLIT);
        logSb.append(request.getMethod());
        logSb.append(LOG_SPLIT);
        logSb.append(code);
        logSb.append(LOG_SPLIT);
        logSb.append(JSONObject.toJSONString(RequestUtil.getHttpParameter(request)));
        logSb.append(LOG_SPLIT);
        logSb.append(responseStr(responseStr));

        String logMsgStr = logSb.toString();

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

        StringBuilder logSb = new StringBuilder();
        logSb.append(RequestUtil.getIp(request));
        logSb.append(LOG_SPLIT);
        logSb.append(request.getRequestURI());
        logSb.append(LOG_SPLIT);
        logSb.append(request.getMethod());
        logSb.append(LOG_SPLIT);
        logSb.append(result.getCode());
        logSb.append(LOG_SPLIT);
        logSb.append(JSONObject.toJSONString(RequestUtil.getHttpParameter(request)));
        logSb.append(LOG_SPLIT);
        logSb.append(responseStr(JSONObject.toJSONString(result)));

        exceptionLogger.error(logSb.toString(), t);

    }

    public static String responseStr(String responseStr) {
        if (StringUtils.isNotBlank(responseStr) && responseStr.length() > 250) {
            responseStr = responseStr.substring(0, 240) + "...}";
        }
        return responseStr;
    }

}
