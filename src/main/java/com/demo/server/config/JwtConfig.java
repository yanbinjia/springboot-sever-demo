package com.demo.server.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import lombok.Data;

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
	//
	private int refreshExpireAfterSecs = 60 * 60 * 24 * 15;
}
