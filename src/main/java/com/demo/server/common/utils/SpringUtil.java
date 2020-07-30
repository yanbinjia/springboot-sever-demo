/*
 * Copyright (c) 2020 demo ^-^.
 * @Author: yanbinjia@126.com
 * @LastModified: 2020-07-28T11:29:01.716+08:00
 */

package com.demo.server.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;
import org.springframework.web.context.WebApplicationContext;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.servlet.support.RequestContextUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

@Component
@Slf4j
public class SpringUtil implements ApplicationContextAware {
    /**
     * SpringBoot中获取ApplicationContext方法
     * <p>
     * 1.直接使用Autowired注入:
     * //@Component public class SpringUtil {
     * //@Autowired private ApplicationContext applicationContext;
     * <p>
     * 2.实现spring提供的接口ApplicationContextAware:
     * spring 在bean初始化后会判断是否ApplicationContextAware子类,调用setApplicationContext()方法,会将容器中ApplicationContext传入
     * <p>
     * 3.在SpringBoot的启动类中,获取并设置ApplicationContext:
     * ApplicationContext applicationContext = SpringApplication.run(xxxApplication.class, args);
     * SpringUtil.setApplicationContext(context);
     */
    private static ApplicationContext applicationContext;

    @Override
    public void setApplicationContext(ApplicationContext applicationContext) throws BeansException {
        SpringUtil.applicationContext = applicationContext;
        log.info(">>> setApplicationContext.");
    }

    public static HttpServletRequest getRequest() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attributes.getRequest();
    }

    public static HttpServletResponse getResponse() {
        ServletRequestAttributes attributes = (ServletRequestAttributes) RequestContextHolder.getRequestAttributes();
        return attributes.getResponse();
    }

    public static WebApplicationContext getWebApplicationContext() {
        return getWebApplicationContext(getRequest());
    }

    public static WebApplicationContext getWebApplicationContext(HttpServletRequest request) {
        WebApplicationContext webApplicationContext = RequestContextUtils.findWebApplicationContext(request);
        return webApplicationContext;
    }

    public static ApplicationContext getApplicationContext() {
        return applicationContext;
    }

    public static Object getBean(String name) {
        return getApplicationContext().getBean(name);
    }

    public static <T> T getBean(Class<T> clazz) {
        return getApplicationContext().getBean(clazz);
    }

    public static <T> T getBean(String name, Class<T> clazz) {
        return getApplicationContext().getBean(name, clazz);
    }

}
