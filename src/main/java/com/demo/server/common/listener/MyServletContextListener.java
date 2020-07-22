/*
 * Copyright (c) 2020 demo ^-^.
 * @Author: yanbinjia@126.com
 * @LastModified: 2020-07-22T16:44:32.880+08:00
 */

package com.demo.server.common.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;
import javax.servlet.annotation.WebListener;

@Configuration
@WebListener
@Slf4j
public class MyServletContextListener implements ServletContextListener {
    @Override
    public void contextInitialized(ServletContextEvent sce) {
        String serverInfo = sce.getServletContext().getServerInfo();
        String virtualServerName = sce.getServletContext().getVirtualServerName();
        log.debug(">>> contextInitialized. serverInfo=[{}], virtualServerName=[{}]", serverInfo, virtualServerName);
    }

    @Override
    public void contextDestroyed(ServletContextEvent sce) {
        String serverInfo = sce.getServletContext().getServerInfo();
        String virtualServerName = sce.getServletContext().getVirtualServerName();
        log.debug(">>> contextDestroyed. serverInfo=[{}], virtualServerName=[{}]", serverInfo, virtualServerName);
    }
}
