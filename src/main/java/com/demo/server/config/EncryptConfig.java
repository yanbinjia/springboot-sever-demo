/*
 * Copyright (c) 2020 demo ^-^.
 * @Author: yanbinjia@126.com
 * @LastModified: 2020-07-28T19:02:38.834+08:00
 */

package com.demo.server.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:security.properties")
@ConfigurationProperties(prefix = "db.encrypt")
@Data
public class EncryptConfig {
    private String key = "fdb2ae47d2505be9";
}
