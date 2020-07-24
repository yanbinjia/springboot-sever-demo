/*
 * Copyright (c) 2020 demo ^-^.
 * @Author: yanbinjia@126.com
 * @LastModified: 2020-07-24T14:32:33.800+08:00
 */

package com.demo.server.common.interceptor.filter;

import org.slf4j.MDC;

public class TraceMDCHolder {

    public static final String TRACE_ID = "traceId";

    public static final String USER_ID = "userId";

    public static final String CLIENT_IP = "clientIp";

    public static void putTraceId(String traceId) {
        MDC.put(TRACE_ID, traceId);
    }

    public static String getTraceId() {
        return MDC.get(TRACE_ID);
    }

    public static void putUserId(String userId) {
        MDC.put(USER_ID, userId);
    }

    public static String getUserId() {
        return MDC.get(USER_ID);
    }

    public static void putClientIp(String clientIp) {
        MDC.put(CLIENT_IP, clientIp);
    }

    public static String getClientIp() {
        return MDC.get(CLIENT_IP);
    }

    public static void remove() {
        MDC.remove(USER_ID);
        MDC.remove(TRACE_ID);
        MDC.remove(CLIENT_IP);
    }
}
