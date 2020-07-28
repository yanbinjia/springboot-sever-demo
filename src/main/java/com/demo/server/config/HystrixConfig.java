/*
 * Copyright (c) 2020 demo ^-^.
 * @Author: yanbinjia@126.com
 * @LastModified: 2020-07-28T19:02:46.278+08:00
 */

package com.demo.server.config;

import com.netflix.hystrix.contrib.javanica.aop.aspectj.HystrixCommandAspect;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.EnableAspectJAutoProxy;

@Configuration
@EnableAspectJAutoProxy // 开启 AOP 代理的支持
public class HystrixConfig {

    @Bean
    public HystrixCommandAspect hystrixCommandAspect() {
        return new HystrixCommandAspect();
    }
}