package com.demo.server.service.schedule;

import com.demo.server.common.utils.DateUtil;
import com.demo.server.manager.UserInfoManager;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import javax.annotation.PostConstruct;

@Service
@Slf4j
public class DemoScheTask {
    @Autowired
    UserInfoManager userInfoManager;

    @PostConstruct // 初始化执行顺序:构造方法->@Autowired->@PostConstruct
    public void init() {
    }

    // cron表达式配置方式，(0 5 3 * * ?)每天3点5份执行
    @Scheduled(cron = "0 5 3 * * ?")
    public void printTime() {
        log.info("Task={},RunAt={},CurrentUserCount={}", "printTime", DateUtil.getCurrentDateTimeStr(),
                userInfoManager.getUserCount());
    }

    // @Scheduled(fixedRate = 6000)：上一次开始执行时间点之后 6 秒再执行。
    // @Scheduled(fixedDelay = 6000)：上一次执行完毕时间点之后 6 秒再执行。注意：是上一次完毕后，再等6秒，不是固定周期。用这个!
    // @Scheduled(initialDelay=1000, fixedRate=6000)：第一次延迟1秒执行，之后按fixedRate每6秒执行一次。
    @Scheduled(fixedDelay = 30 * 60 * 1000)
    public void fixedDelayTask() {
        log.info("Task={},RunAt={}", "fixedDelayTask", DateUtil.getCurrentDateTimeStr());
    }
}
