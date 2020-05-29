package com.demo.server.bean.response;

public enum ResultCode {
	FAILED(-1, "未知错误"), 
	SUCCESS(0, "SUCCESS"), 
	SERVER_UNKONW_ERROR(500, "服务器开小差了,请稍后再试"), 
	
	MISS_PARAM(400, "参数异常"), 
	DB_ERROR(10000, "DB操作失败"), 
	SESSION_OVERTIME(302, "会话已过期，请重新登录。"), 
	SESSION_INIT_ERROR(303, "建立会话异常"), 
	STRATEGY_CHECK_ERROR(30001, "检测失败"),

	RESOURCE_NOT_EXIST(50001, "资源不存在"),;

	public final int code;
	public final String msg;

	ResultCode(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public static ResultCode findStatusByCode(int code) {
		for (ResultCode resultCode : ResultCode.values()) {
			if (resultCode.code == code) {
				return resultCode;
			}
		}
		return null;
	}
}
