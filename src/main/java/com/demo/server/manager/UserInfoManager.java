package com.demo.server.manager;

import com.demo.server.dao.UserInfoDao;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class UserInfoManager {
    @Autowired
    UserInfoDao userInfoDao;

    public long getUserCount() {
        return userInfoDao.selectCount(null);
    }
}
