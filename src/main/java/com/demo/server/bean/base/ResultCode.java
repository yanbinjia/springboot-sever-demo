package com.demo.server.bean.base;

public enum ResultCode {
	// -------------基础错误码------------------
	FAILED(-1, "FAILED"), // 未知错误
	SUCCESS(0, "SUCCESS"), // 操作成功

	SYSTEM_ERROR(500, "系统错误,请稍后重试."), //
	SERVICE_UNAVAILABLE(502, "服务不可用,请稍后重试."), //

	PARAM_ERROR(400, "参数错误."), //
	SIGN_ERROR(401, "签名验证失败."), //
	NO_AUTH(403, "认证信息错误."), //
	NOT_EXIST(404, "资源不存在."), //
	METHOD_NOT_ALLOWED(405, "方法不被允许."), //
	RATE_LIMITED(429, "频率超限."), //

	// -------------基础错误码------------------
	DB_ERROR(10000, "DB操作失败"), //

	// -------------安全错误码------------------
	// 80XXX
	SEC_TOKEN_ERROR(80101, "凭证错误,token无效."), //
	SEC_TOKEN_PARAM(80102, "凭证错误,token与userId不能为空."), //
	SEC_TOKEN_MISSUID(80103, "凭证错误,token与userId不匹配."), //
	SEC_TOKEN_EXPIRE(80104, "凭证错误,token过期."), //
	SEC_TOKEN_CREATE(80105, "生成token错误."), //
	SEC_TOKEN_REFRESH(80106, "刷新token错误."), //

	SEC_SIGN_ERROR(80201, "签名错误,签名验证失败."), //
	SEC_SIGN_EXPIRE(80202, "签名错误,签名时间戳过期."),//
	;

	public final int code;
	public final String msg;

	ResultCode(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public static ResultCode findByCode(int code) {
		for (ResultCode resultCode : ResultCode.values()) {
			if (resultCode.code == code) {
				return resultCode;
			}
		}
		return null;
	}
}
