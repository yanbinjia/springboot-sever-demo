package com.demo.server.bean.enumer;

public enum UserDeleted {
	NULL(-1, "未知"), //
	NORMAL(0, "正常"), //
	DELETED(1, "删除");//

	private Integer code;
	private String msg;

	UserDeleted(Integer code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public static String findMsgByCode(Integer code) {
		String msg = "未知";
		for (UserDeleted element : UserDeleted.values()) {
			if (element.code == code) {
				msg = element.getMsg();
			}
		}
		return msg;
	}

	public Integer getCode() {
		return code;
	}

	public void setCode(Integer code) {
		this.code = code;
	}

	public String getMsg() {
		return msg;
	}

	public void setMsg(String msg) {
		this.msg = msg;
	}

}
