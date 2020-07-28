/*
 * Copyright (c) 2020 demo ^-^.
 * @Author: yanbinjia@126.com
 * @LastModified: 2020-07-28T19:03:01.635+08:00
 */

package com.demo.server.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:security.properties")
@ConfigurationProperties(prefix = "sign")
@Data
public class SignConfig {
    // secret key
    private String secretKey = "fdb2ae47d2505be99f33197a37bb54cb";
    // 过期时间,单位秒
    private int timestampExpireSecs = 10 * 60;
    // 是否打印校验日志信息
    private boolean printCheckInfo = false;
    // 是否打开
    private boolean turnOn = false;
    // 签名算法,support:sha256,md5
    private String algorithm = "md5";
}
