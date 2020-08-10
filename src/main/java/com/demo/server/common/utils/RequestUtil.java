package com.demo.server.common.utils;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.Map.Entry;

public class RequestUtil {

    private static final Logger logger = LoggerFactory.getLogger(RequestUtil.class);

    private static String getClientIp(HttpServletRequest request) {
        return IpUtil.getClientIp(request);
    }

    public static String getIp(HttpServletRequest request) {
        return getClientIp(request);
    }

    public static Map<String, String> getParameterMap(HttpServletRequest request) {

        Map<String, String[]> map = request.getParameterMap();
        Map<String, String> paramMap = new HashMap<>();
        for (Entry<String, String[]> entry : map.entrySet()) {
            String name = entry.getKey();
            String[] value = entry.getValue();
            if (value.length > 1) {
                logger.warn("http request param has too many value,name:{},value:{}", name, Arrays.asList(value));
                continue;
            }
            paramMap.put(name, value[0]);
        }
        return paramMap;
    }

    public static Map<String, String> getParameterMapEndWith(HttpServletRequest request, String tag) {
        Map<String, String[]> map = request.getParameterMap();
        Map<String, String> paramMap = new HashMap<>();
        for (Entry<String, String[]> entry : map.entrySet()) {
            String name = entry.getKey();
            String[] value = entry.getValue();
            if (value.length > 1) {
                logger.warn("http request param has too many value,name:{},value:{}", name, Arrays.asList(value));
                continue;
            }
            if (name.endsWith(tag)) {
                paramMap.put(name, value[0]);
            }
        }
        return paramMap;
    }

    public static Map<String, String> getParameterMapStartWith(HttpServletRequest request, String tag) {
        Map<String, String[]> map = request.getParameterMap();
        Map<String, String> paramMap = new HashMap<>();
        for (Entry<String, String[]> entry : map.entrySet()) {
            String name = entry.getKey();
            String[] value = entry.getValue();
            if (value.length > 1) {
                logger.warn("http request param has too many value,name:{},value:{}", name, Arrays.asList(value));
                continue;
            }
            if (name.startsWith(tag)) {
                paramMap.put(name.replaceFirst(tag, ""), value[0]);
            }
        }
        return paramMap;
    }

}