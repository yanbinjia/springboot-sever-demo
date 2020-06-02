package com.demo.server.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import lombok.Data;

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
}
