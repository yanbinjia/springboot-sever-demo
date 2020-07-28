/*
 * Copyright (c) 2020 demo ^-^.
 * @Author: yanbinjia@126.com
 * @LastModified: 2020-07-22T15:51:12.895+08:00
 */

package com.demo.server.common.listener;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;

import javax.servlet.ServletRequestEvent;
import javax.servlet.ServletRequestListener;
import javax.servlet.annotation.WebListener;
import javax.servlet.http.HttpServletRequest;

@Configuration
@WebListener
@Slf4j
public class MyServletRequestListener implements ServletRequestListener {
    @Override
    public void requestDestroyed(ServletRequestEvent sre) {
        HttpServletRequest request = (HttpServletRequest) sre.getServletRequest();
        String uri = request.getRequestURI();
        String remoteAddr = request.getRemoteAddr();

        log.debug(">>> requestDestroyed uri=[{}],remoteAddr=[{}]", uri, remoteAddr);
    }

    @Override
    public void requestInitialized(ServletRequestEvent sre) {
        HttpServletRequest request = (HttpServletRequest) sre.getServletRequest();
        String uri = request.getRequestURI();
        String remoteAddr = request.getRemoteAddr();

        log.debug(">>> requestInitialized uri=[{}],remoteAddr=[{}]", uri, remoteAddr);
    }
    /**
     *
     * 需要在 SpringBootApplication 上使用 @ServletComponentScan 注解,
     * 使Servlet、Filter、Listener可通过 @WebServlet、@WebFilter、@WebListener自动注册。
     *
     * 参考如下说明:
     * The @ServletComponentScan Annotation in Spring Boot
     * https://www.baeldung.com/spring-servletcomponentscan
     *
     * Using @ServletComponentScan in Spring Boot:
     *
     * You might wonder since we can use those annotations in most Servlet containers without any configuration,
     * why do we need @ServletComponentScan?
     * The problem lies in embedded Servlet containers.
     * Due to the fact that embedded containers do not support @WebServlet, @WebFilter and @WebListener annotations,
     * Spring Boot, relying greatly on embedded containers,
     * introduced this new annotation @ServletComponentScan to support some dependent jars that use these 3 annotations.
     *
     * The Spring Boot app is pretty simple.
     * We add @ServletComponentScan to enable scanning for @WebFilter, @WebListener and @WebServlet:
     *
     * @ServletComponentScan
     * @SpringBootApplication
     * public class SpringBootAnnotatedApp {
     *     public static void main(String[] args) {
     *         SpringApplication.run(SpringBootAnnotatedApp.class, args);
     *     }
     * }
     *
     */
}
