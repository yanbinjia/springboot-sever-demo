/*
 * Copyright (c) 2020 demo ^-^.
 * @Author: yanbinjia@126.com
 * @LastModified: 2020-07-28T19:02:50.989+08:00
 */

package com.demo.server.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

@Configuration
@PropertySource("classpath:security.properties")
@ConfigurationProperties(prefix = "jwt")
@Data
public class JwtConfig {
    // secret key
    private String secretKey = "c624785c3e137c9c091b6ad2188c423d";
    // "iss" (Issuer) Claim
    private String iss = "default";
    // "exp" (Expiration Time) Claim, 单位秒
    private int expireAfterSecs = 60 * 60 * 12;
    // refresh token 过期时间, 单位秒
    private int refreshExpireAfterSecs = 60 * 60 * 24 * 15;
}
