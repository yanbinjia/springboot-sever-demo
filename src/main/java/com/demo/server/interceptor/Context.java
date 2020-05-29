package com.demo.server.interceptor;

public class Context {

	private static final ThreadLocal<Context> threadLocal = new ThreadLocal<>();

	private long startTime;
	private String traceId;
	private String clientIp;

	public static Context getOrNewInstance() {
		if (threadLocal.get() == null) {
			Context context = new Context();
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
