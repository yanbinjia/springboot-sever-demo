package com.demo.server.service.user;

import java.util.List;

import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.demo.server.bean.entity.UserInfo;
import com.demo.server.bean.enums.UserDeleted;
import com.demo.server.bean.enums.UserStatus;
import com.demo.server.bean.vo.UserInfoParam;
import com.demo.server.bean.vo.UserInfoResult;
import com.demo.server.dao.UserInfoDao;
import com.github.pagehelper.PageHelper;
import com.github.pagehelper.PageInfo;

import lombok.extern.slf4j.Slf4j;

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

	public UserInfo getById(Long id) {
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

	public PageInfo<UserInfoResult> listByCondition(UserInfoParam userInfoParam) {
		// PageHelper 分页查询
		PageInfo<UserInfoResult> result = PageHelper.startPage(userInfoParam.getPageNum(), userInfoParam.getPageSize())
				.doSelectPageInfo(() -> {
					List<UserInfoResult> data = userInfoDao.listByCondition(userInfoParam.getMobile(),
							userInfoParam.getNameOrEmail());
					// 补充状态字段显示值
					data.stream().forEach(userInfo -> {
						userInfo.setDeletedName(UserDeleted.findMsgByCode(userInfo.getDeleted()));
						userInfo.setStatusName(UserStatus.findMsgByCode(userInfo.getStatus()));
					});
				});
		return result;
	}

	@Transactional
	public Boolean saveUser(UserInfo bean) {
		bean.setId(null);
		return userInfoDao.insertSelective(bean) > 0;
	}

	@Transactional
	public Boolean updateUser(UserInfo bean) {
		return userInfoDao.updateByPrimaryKeySelective(bean) > 0;
	}

	@Transactional
	public Boolean deleteUserById(Long id) {
		UserInfo param = new UserInfo();
		param.setId(id);
		param.setDeleted(UserDeleted.DELETED.getCode());
		return userInfoDao.updateByPrimaryKeySelective(param) > 0;
	}
}
