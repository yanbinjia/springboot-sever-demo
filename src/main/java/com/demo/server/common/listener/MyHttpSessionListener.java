package com.demo.server.common.listener;

import lombok.extern.slf4j.Slf4j;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

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