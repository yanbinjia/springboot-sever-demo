/*
 * Copyright (c) 2020 demo ^-^.
 * @Author: yanbinjia@126.com
 * @LastModified: 2020-07-29T10:48:00.376+08:00
 */

package com.demo.server.common.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.context.event.*;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

@Component
@Async
@Slf4j
public class MyApplicationListener implements ApplicationListener<ApplicationEvent> {
    /**
     * springboot启动的过程中会产生一系列事件,我们开发的时候可以自定义一些事件监听处理器, 根据自己的需要在针对每个事件做一些业务处理.
     *
     * @param event
     */
    @Override
    public void onApplicationEvent(ApplicationEvent event) {
        //springboot应用启动且未作任何处理（除listener注册和初始化）的时候发送ApplicationStartingEvent
        if (event instanceof ApplicationStartingEvent) {
            log.info(">>> ApplicationStarting.");
            return;
        }
        //确定springboot应用使用的Environment且context创建之前发送这个事件
        if (event instanceof ApplicationEnvironmentPreparedEvent) {
            log.info(">>> ApplicationEnvironmentPrepared.");
            return;
        }
        //context已经创建且没有refresh发送个事件
        if (event instanceof ApplicationPreparedEvent) {
            log.info(">>> ApplicationPrepared.");
            return;
        }
        //context已经refresh且application and command-line runners（如果有） 调用之前发送这个事件
        if (event instanceof ApplicationStartedEvent) {
            log.info(">>> ApplicationStarted.");
            return;
        }
        //application and command-line runners （如果有）执行完后发送这个事件，此时应用已经启动完毕
        if (event instanceof ApplicationReadyEvent) {
            ApplicationContext context = ((ApplicationReadyEvent) event).getApplicationContext();
            log.info(">>> ApplicationReady.");
            // 系统初始化的一些动作也可以在这里处理
            return;
        }
        //应用启动失败后产生这个事件
        if (event instanceof ApplicationFailedEvent) {
            log.info(">>> ApplicationFailed.");
            return;
        }
    }
}
