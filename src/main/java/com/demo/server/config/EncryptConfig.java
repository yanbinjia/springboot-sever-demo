package com.demo.server.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import lombok.Data;

@Configuration
@PropertySource("classpath:security.properties")
@ConfigurationProperties(prefix = "db.encrypt")
@Data
public class EncryptConfig {
	private String key = "fdb2ae47d2505be9";
}
