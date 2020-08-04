package com.demo.server.common.utils;

import com.alibaba.fastjson.JSONObject;
import com.demo.server.bean.base.Result;
import com.demo.server.common.constant.AppConstant;
import org.apache.commons.lang.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;

import static com.demo.server.common.constant.AppConstant.LOG_SPLIT;

public class LogUtil {

    public enum LogLevel {
        INFO, WARN, ERROR
    }

    private static final Logger accessLogger = LoggerFactory.getLogger(AppConstant.LOGGER_ACCESS);

    private static final Logger appLogger = LoggerFactory.getLogger(LogUtil.class);

    public static int LOG_MAX_LEN = 300;

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
        logSb.append(JSONObject.toJSONString(RequestUtil.getParameterMap(request)));
        logSb.append(LOG_SPLIT);
        logSb.append(maxLenDeal(responseStr));

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
        logSb.append(JSONObject.toJSONString(RequestUtil.getParameterMap(request)));
        logSb.append(LOG_SPLIT);
        logSb.append(maxLenDeal(JSONObject.toJSONString(result)));

        appLogger.error(logSb.toString(), t);
    }

    /**
     * logStr截断, 防止日志内容过多
     *
     * @param logStr
     * @return 截断后日志字符串
     */
    public static String maxLenDeal(String logStr) {
        if (StringUtils.isNotBlank(logStr) && logStr.length() >= LOG_MAX_LEN + 10) {
            logStr = logStr.substring(0, LOG_MAX_LEN) + "...}";
        }
        return logStr;
    }

    public static void debug(String format, Object... arguments) {
        if (appLogger.isDebugEnabled()) {
            appLogger.debug(format, arguments);
        }
    }

}
