package com.demo.server.interceptor;

public class TraceContext {

	private static final ThreadLocal<TraceContext> threadLocal = new ThreadLocal<>();

	private long startTime;
	private String traceId;
	private String clientIp;

	public static TraceContext getInstance() {
		if (threadLocal.get() == null) {
			TraceContext context = new TraceContext();
			threadLocal.set(context);
		}
		return threadLocal.get();
	}

	public void remove() {
		threadLocal.remove();
		TraceIdHolder.remove();
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
		TraceIdHolder.putTraceId(traceId);
	}

	public String getClientIp() {
		return clientIp;
	}

	public void setClientIp(String clientIp) {
		this.clientIp = clientIp;
	}

}
