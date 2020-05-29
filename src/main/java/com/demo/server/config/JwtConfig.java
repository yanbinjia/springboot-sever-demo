package com.demo.server.config;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;

import lombok.Data;

@Configuration
@PropertySource("classpath:application.properties")
@ConfigurationProperties(prefix = "jwt")
@Data
public class JwtConfig {
	// secret key
	private String secret = "default";
	// "iss" (Issuer) Claim
	private String iss = "default";
	// "exp" (Expiration Time) Claim
	private long exp = 1000 * 60 * 60 * 12;
}
