package com.demo.server.dao;

import java.util.List;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.SelectProvider;
import org.springframework.stereotype.Repository;

import com.demo.server.bean.entity.UserInfo;
import com.demo.server.bean.vo.UserInfoResult;
import com.demo.server.dao.provider.UserInfoProvider;

import tk.mybatis.mapper.common.Mapper;

/**
 * 说明
 * 
 * 1.继承Mapper接口(tk.mybatis.mapper.common.Mapper)，Dao继承了一系列tk.mybatis提供的通用方法。
 * 
 * 2.在Mapper的基础上，使用mybatis3的xml或注解方式，继续书写其他(较为复杂或非通用)的方法，参考mybatis文档。
 * 英文:https://mybatis.org/mybatis-3
 * 中文:https://mybatis.org/mybatis-3/zh/index.html
 * 
 * 3.在UserInfoDao(Mapper)中，分别注解、xml方式以及一些用法和写法，书写一些例子代码供参考和学习。
 * 
 */
@Repository
public interface UserInfoDao extends Mapper<UserInfo> {

	// 注解SQL
	@Select("select id,user_name,email,mobile,status,deleted,create_time,update_time from user_info where email=#{email}")
	UserInfo getByEmail(String email);

	// 注解SQL
	@Select("select id,user_name,email,mobile,status,deleted,create_time,update_time from user_info where user_name=#{user_name}")
	UserInfo getByUserName(String userName);

	// ${参数}和#{参数}的应用, ${column} 会被直接替换，而 #{value} 会使用 ? 预处理
	// @Param("column") 用来映射方法的变量名称和sql中的参数名称, 不配置的话默认相同
	@Select("select id,user_name,email,mobile,status,deleted,create_time,update_time ususer_infoer where ${column} = #{value}")
	UserInfo getByColumn(@Param("column") String column, @Param("value") String value);

	// 使用Provider构造复杂SQL，Provider:
	// @SelectProvider、@UpdateProvider、@InsertProvider、@DeleteProvider
	@SelectProvider(type = UserInfoProvider.class, method = "getByMobile")
	UserInfo getByMobile(String mobile);

	@SelectProvider(type = UserInfoProvider.class, method = "getByCondition")
	List<UserInfo> getByCondition(UserInfo userInfo);

	// xmlSQL
	List<UserInfo> getAll();

	// xmlSQL
	UserInfo getById(Integer id);

	// xmlSQL
	UserInfo deleteById(Integer id);

	// xmlSQL 动态 SQL, 结果映射
	List<UserInfoResult> listByCondition(@Param("mobile") String mobile, @Param("nameOrEmail") String nameOrEmail);

}
