/*
 * Copyright (c) 2020 demo ^-^.
 * @Author: yanbinjia@126.com
 * @LastModified: 2020-07-29T10:29:43.739+08:00
 */

package com.demo.server.common.runner;

import com.demo.server.common.utils.DateUtil;
import com.demo.server.common.utils.IpUtil;
import com.demo.server.service.base.cache.RedisService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.ApplicationArguments;
import org.springframework.stereotype.Component;

@Component
@Slf4j
public class MyApplicationRunner implements org.springframework.boot.ApplicationRunner {
    @Autowired
    private RedisService redisService;

    @Value("${server.port}")
    private String port;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        boolean redisStatus = redisService.set("DemoServer-balabala",
                "DemoServer-balabala@" + IpUtil.getLocalIp() + ":" + this.port);
        log.info("==============================================");
        log.info(">>> Application startup @{}", DateUtil.getCurrentDateTimeStr());
        log.info(">>> ip=[{}]", IpUtil.getLocalIp());
        log.info(">>> port=[{}]", this.port);
        log.info(">>> redisStatus=[{}]", redisStatus);
        log.info("==============================================");

    }
    /**
     * CommandLineRunner、ApplicationRunner 接口是在容器启动成功后的最后一步回调（类似开机自启动）.
     * CommandLineRunner的使用:
     * 编写一个类去实现CommandLineRunner接口，并覆写run方法，即可接入Spring观察者模式的回调功能，完成自己在容器启动成功之后想做的事情。
     *
     * ApplicationRunner和CommandLineRunner的区别只是在于接收的参数不一样:
     * CommandLineRunner的参数是最原始的参数，没有做任何处理。
     * ApplicationRunner的参数是ApplicationArguments，是对原始参数做了进一步的封装。
     *
     */
}
