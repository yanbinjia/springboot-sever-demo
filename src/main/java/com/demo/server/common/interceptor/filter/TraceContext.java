/*
 * Copyright (c) 2020 demo ^-^.
 * @Author: yanbinjia@126.com
 * @LastModified: 2020-07-24T14:32:33.738+08:00
 */

package com.demo.server.common.interceptor.filter;

public class TraceContext {

    private static final ThreadLocal<TraceContext> threadLocal = new ThreadLocal<>();

    private long startTime;
    private String traceId;
    private String clientIp;
    private String userId;

    public static TraceContext getInstance() {
        if (threadLocal.get() == null) {
            TraceContext context = new TraceContext();
            threadLocal.set(context);
        }
        return threadLocal.get();
    }

    public void remove() {
        threadLocal.remove();
        TraceMDCHolder.remove();
    }

    public long getStartTime() {
        return startTime;
    }

    public void setStartTime(long startTime) {
        this.startTime = startTime;
    }

    public String getTraceId() {
        return traceId;
    }

    public void setTraceId(String traceId) {
        this.traceId = traceId;
        TraceMDCHolder.putTraceId(traceId);
    }

    public String getClientIp() {
        return clientIp;
    }

    public void setClientIp(String clientIp) {
        this.clientIp = clientIp;
        TraceMDCHolder.putClientIp(clientIp);
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
        TraceMDCHolder.putUserId(userId);
    }

}
