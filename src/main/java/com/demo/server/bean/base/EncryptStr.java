package com.demo.server.bean.base;

public class EncryptStr {
	String value = "";

	public EncryptStr(String value) {
		this.value = value;
	}

	@Override
	public String toString() {
		return this.value;
	}

	public String getValue() {
		return value;
	}

	public void setValue(String value) {
		this.value = value;
	}

}
