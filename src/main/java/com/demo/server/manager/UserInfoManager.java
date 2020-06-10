package com.demo.server.manager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.demo.server.dao.UserInfoDao;

@Component
public class UserInfoManager {
	@Autowired
	UserInfoDao userInfoDao;

	public long getUserCount() {
		return userInfoDao.selectCount(null);
	}
}
