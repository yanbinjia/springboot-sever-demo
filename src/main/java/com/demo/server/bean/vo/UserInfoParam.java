package com.demo.server.bean.vo;

import com.demo.server.bean.base.Pager;

import lombok.Data;

@Data
public class UserInfoParam extends Pager {
	// 姓名或邮箱
	private String nameOrEmail;
	// mobile
	private String mobile;
}
