package com.demo.server.bean.base;

import com.demo.server.common.exception.AppException;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Result<T> {

	// 返回数据对象
	private T data;

	// 返回状态码
	private int code = ResultCode.FAILED.code;

	// 返回消息
	private String msg = ResultCode.FAILED.msg;

	// 服务器unix时间戳(秒)
	private long timestamp = System.currentTimeMillis() / 1000;

	// timestamp 可读时间
	// private String timestamp = DateUtil.getCurrentDateTimeStr();

	public Result(ResultCode responseCode) {
		super();
		this.code = responseCode.code;
		this.msg = responseCode.msg;
	}

	public Result(int code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public Result(T data) {
		this.data = data;
		this.code = ResultCode.SUCCESS.code;
		this.msg = ResultCode.SUCCESS.msg;
	}

	public Result(T data, int code, String msg) {
		this.data = data;
		this.code = code;
		this.msg = msg;
	}

	public void setResultCode(ResultCode resultCode) {
		this.code = resultCode.code;
		this.msg = resultCode.msg;
	}

	public void setResultCode(AppException e) {
		this.code = e.getCode();
		this.msg = e.getMsg();
	}

	public void setExtMsg(String extMsg) {
		this.msg += "(" + extMsg + ")";
	}
}
