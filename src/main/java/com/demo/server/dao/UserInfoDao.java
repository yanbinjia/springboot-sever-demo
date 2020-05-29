package com.demo.server.dao;

import org.apache.ibatis.annotations.Select;
import org.springframework.stereotype.Repository;

import com.demo.server.bean.entity.UserInfo;

import tk.mybatis.mapper.common.Mapper;

/**
 * 继承Mapper接口(tk.mybatis.mapper.common.Mapper),继承一系列列通用方法。
 * 
 * 同时也可以@Select，@Update注解中写sql，也可以使用xml方式写sql。
 *
 */
@Repository
public interface UserInfoDao extends Mapper<UserInfo> {

	@Select("select id,user_name,email from user_info where deleted = 1 and email=#{email}")
	UserInfo getByEmail(String email);

}
