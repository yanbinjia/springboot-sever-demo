package com.demo.server.bean.response;

public enum ResultCode {
	// -------------基础错误码------------------
	FAILED(-1, "FAILED"), // 未知错误
	SUCCESS(0, "SUCCESS"), // 操作成功

	SYSTEM_ERROR(500, "系统错误"), //
	SERVICE_UNAVAILABLE(502, "服务不可用"), //

	PARAM_ERROR(400, "参数错误"), //
	SIGN_ERROR(401, "签名验证失败"), //
	NO_AUTH(403, "认证信息错误"), //
	NOT_EXIST(404, "资源不存在"), //
	RATE_LIMITED(429, "频率超限"), //

	// -------------基础错误码------------------
	DB_ERROR(10000, "DB操作失败"), //
	STRATEGY_CHECK_ERROR(30001, "检测失败"), //

	// -------------安全错误码------------------
	// 80XXX
	SEC_TOKEN_BASE(80101, "用户凭证信息错误，无凭证或凭证无效。"), //
	SEC_TOKEN_ERROR(80102, "用户凭证信息错误，凭证无效或已过期。"), //

	SEC_SIGN_ERROR(80201, "签名信息错误，签名验证失败。"), //
	SEC_SIGN_EXPIRE(80202, "签名信息错误，签名时间戳过期。"),//
	;

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
