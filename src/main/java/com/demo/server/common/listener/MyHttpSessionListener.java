/*
 * Copyright (c) 2020 demo ^-^.
 * @Author: yanbinjia@126.com
 * @LastModified: 2020-07-22T23:03:27.071+08:00
 */

package com.demo.server.common.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

@Configuration
@WebListener
@Slf4j
public class MyHttpSessionListener implements HttpSessionListener {
    @Override
    public void sessionCreated(HttpSessionEvent se) {
        String id = se.getSession().getId();
        long creationTime = se.getSession().getCreationTime();
        log.debug(">>> sessionCreated id=[{}],createTime=[{}]", id, creationTime);
    }

    @Override
    public void sessionDestroyed(HttpSessionEvent se) {
        String id = se.getSession().getId();
        long creationTime = se.getSession().getCreationTime();
        log.debug(">>> sessionDestroyed id=[{}],createTime=[{}]", id, creationTime);
    }
}
