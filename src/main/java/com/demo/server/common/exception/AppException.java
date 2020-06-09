package com.demo.server.common.exception;

import com.demo.server.bean.base.ResultCode;

import lombok.Data;

@Data
public class AppException extends RuntimeException {

	private static final long serialVersionUID = 1L;

	private int code;
	private String msg;

	public AppException() {
		super();
	}

	public AppException(int code, String msg) {
		super(msg);
		this.code = code;
		this.msg = msg;
	}

	public AppException(ResultCode resultCode) {
		super(resultCode.msg);
		this.code = resultCode.code;
		this.msg = resultCode.msg;
	}

	public AppException(ResultCode resultCode, String extMsg) {
		super(resultCode.msg + " (" + extMsg + ")");
		this.code = resultCode.code;
		this.msg = resultCode.msg + " (" + extMsg + ")";
	}

	public AppException(String msg) {
		super(msg);
		this.code = ResultCode.FAILED.code;
		this.msg = msg;
	}

}
