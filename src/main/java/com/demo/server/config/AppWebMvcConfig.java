package com.demo.server.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.demo.server.interceptor.TokenInterceptorAdapter;

@Configuration
public class AppWebMvcConfig implements WebMvcConfigurer {

	@Override
	public void addInterceptors(InterceptorRegistry registry) {
		addTokenInterceptor(registry);
	}

	public void addTokenInterceptor(InterceptorRegistry registry) {
		List<String> pathPatterns = new ArrayList<>();
		List<String> excludePatterns = new ArrayList<>();

		pathPatterns.add("/**");

		excludePatterns.add("/ping");
		excludePatterns.add("/pingerror");

		registry.addInterceptor(new TokenInterceptorAdapter()).addPathPatterns(pathPatterns)
				.excludePathPatterns(excludePatterns);

	}

	// ------------------ corsFilter -------------------
	@Bean
	public CorsFilter corsFilter() {
		UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", corsConfig());
		return new CorsFilter(source);
	}

	private CorsConfiguration corsConfig() {
		CorsConfiguration corsConfiguration = new CorsConfiguration();
		corsConfiguration.addAllowedOrigin("*");
		corsConfiguration.addAllowedHeader("*");
		corsConfiguration.addAllowedMethod("*");
		corsConfiguration.setAllowCredentials(true);
		corsConfiguration.setMaxAge(3600L);
		return corsConfiguration;
	}

	// ------------------ fastJsonHttpMessageConverters -------------------
	@Bean
	public HttpMessageConverters fastJsonHttpMessageConverters() {
		// 创建FastJson信息转换对象
		FastJsonHttpMessageConverter fastConverter = new FastJsonHttpMessageConverter();

		// 创建FastJsonConfig对象并设定序列化规则
		FastJsonConfig fastJsonConfig = new FastJsonConfig();
		fastJsonConfig.setSerializerFeatures(SerializerFeature.PrettyFormat);
		fastJsonConfig.setDateFormat("yyyy-MM-dd HH:mm:ss");

		// 解决中文乱码问题,设定Json格式且编码为utf-8
		List<MediaType> fastMedisTypes = new ArrayList<MediaType>();
		fastMedisTypes.add(MediaType.APPLICATION_JSON_UTF8);
		fastConverter.setSupportedMediaTypes(fastMedisTypes);

		// 将转换规则应用于转换对象
		fastConverter.setFastJsonConfig(fastJsonConfig);

		return new HttpMessageConverters(fastConverter);
	}
}
