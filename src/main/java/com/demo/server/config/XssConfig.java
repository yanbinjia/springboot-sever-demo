/*
 * Copyright (c) 2020 demo ^-^.
 * @Author: yanbinjia@126.com
 * @LastModified: 2020-07-29T11:25:26.664+08:00
 */

package com.demo.server.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:security.properties")
@ConfigurationProperties(prefix = "xss")
@Data
public class XssConfig {
    private boolean turnOn = true;
    // excludes config use regex
    private String excludes = "";
    // action support:escape,clean,reject
    private String action = "escape";
}
