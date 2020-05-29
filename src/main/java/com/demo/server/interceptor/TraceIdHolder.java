package com.demo.server.interceptor;

import org.slf4j.MDC;

public class TraceIdHolder {

  public static final String TRACE_ID = "traceId";

  public static final String USER_ID = "userId";

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

  public static void remove() {
    MDC.remove(USER_ID);
    MDC.remove(TRACE_ID);
  }
}
