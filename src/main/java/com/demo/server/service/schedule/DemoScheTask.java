package com.demo.server.service.schedule;

import java.util.List;

import org.apache.commons.collections4.CollectionUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.demo.server.bean.entity.UserInfo;
import com.demo.server.common.util.DateUtil;
import com.demo.server.dao.UserInfoDao;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class DemoScheTask {
	@Autowired
	UserInfoDao userInfoDao;

	// cron表达式配置方式，(0 5 3 * * ?)每天3点5份执行
	@Scheduled(cron = "0 5 3 * * ?")
	public void printTime() {
		List<UserInfo> userList = userInfoDao.getAll();
		log.info("Task={},RunAt={},CurrentUserCount={}", "printTime", DateUtil.getCurrentDateTimeStr(),
				CollectionUtils.size(userList));
	}

	// @Scheduled(fixedRate = 6000)：上一次开始执行时间点之后 6 秒再执行。
	// @Scheduled(fixedDelay = 6000)：上一次执行完毕时间点之后 6 秒再执行。注意：是上一次完毕后，再等6秒，不是固定周期。用这个!
	// @Scheduled(initialDelay=1000, fixedRate=6000)：第一次延迟1秒执行，之后按fixedRate每6秒执行一次。
	@Scheduled(fixedDelay = 30 * 60 * 1000)
	public void fixedDelayTask() {
		log.info("Task={},RunAt={},UserCount={}", "fixedDelayTask", DateUtil.getCurrentDateTimeStr());
	}
}
