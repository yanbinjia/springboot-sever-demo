package com.demo.server.dao.provider;

import org.apache.commons.lang3.StringUtils;
import org.apache.ibatis.jdbc.SQL;

import com.demo.server.bean.entity.UserInfo;

/**
 * 
 * SQL 语句构建器: https://mybatis.org/mybatis-3/zh/statement-builders.html
 * 
 */
public class UserInfoProvider {

	public String getByMobile(String mobile) {
		// SQL构造器构造SQL(也可以使用字符串拼接)
		return new SQL() {
			{
				SELECT("id,user_name,password,email,mobile,status,deleted,create_time,update_time");
				FROM("user_info");
				WHERE("mobile=#{mobile}");
			}
		}.toString();
	}

	public String getByMobileUseStrJoin(String mobile) {
		// 字符串拼接构造SQL
		StringBuilder stringBuilder = new StringBuilder(
				"select id,user_name,password,email,mobile,status,deleted,create_time,update_time from user_info ");

		if (StringUtils.isNotBlank(mobile)) {
			stringBuilder.append("where mobile=#{mobile}");
		}

		return stringBuilder.toString();
	}

	public String getByCondition(UserInfo userInfo) {
		// SQL构造器构造SQL(也可以使用字符串拼接)
		return new SQL() {
			{
				SELECT("id,user_name,password,email,mobile,status,deleted,create_time,update_time");
				FROM("user_info");
				if (StringUtils.isNotBlank(userInfo.getUserName())) {
					WHERE("user_name=#{userName}");
				}
				if (StringUtils.isNotBlank(userInfo.getEmail())) {
					WHERE("email=#{email}");
				}
				if (StringUtils.isNotBlank(userInfo.getMobile())) {
					WHERE("mobile=#{mobile}");
				}
				if (userInfo.getStatus() != null) {
					WHERE("status=#{status}");
				}
				if (userInfo.getDeleted() != null) {
					WHERE("deleted=#{deleted}");
				}
			}
		}.toString();
	}

}
