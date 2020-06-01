package com.demo.server.service.user;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.demo.server.bean.entity.UserInfo;
import com.demo.server.dao.UserInfoDao;

import lombok.extern.slf4j.Slf4j;

/**
 * 用户管理
 *
 * @author xuxinwei
 * @create 2019-07-31
 */
@Service
@Slf4j
public class UserInfoService {

	@Autowired
	private UserInfoDao userInfoDao;

	public List<UserInfo> getAll() {
		List<UserInfo> userList = userInfoDao.getAll();
		log.info("listAll");
		return userList;
	}

	public List<UserInfo> listAll() {
		List<UserInfo> userList = userInfoDao.selectAll();
		log.info("listAll");
		return userList;
	}

	public UserInfo getById(Integer id) {
		UserInfo userInfo = userInfoDao.selectByPrimaryKey(id);
		log.info("getUserById");
		return userInfo;
	}

	public UserInfo getByMobile(String mobile) {
		UserInfo userInfo = null;
		if (StringUtils.isNotBlank(mobile)) {
			userInfo = userInfoDao.getByMobile(mobile);
		}
		return userInfo;
	}

}
