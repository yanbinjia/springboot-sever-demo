package com.demo.server.config;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.http.HttpMessageConverters;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.MediaType;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;
import org.springframework.web.filter.CorsFilter;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import com.alibaba.fastjson.parser.ParserConfig;
import com.alibaba.fastjson.serializer.SerializerFeature;
import com.alibaba.fastjson.support.config.FastJsonConfig;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.demo.server.common.interceptor.SignInterceptor;
import com.demo.server.common.interceptor.TokenInterceptor;

@Configuration
public class AppWebMvcConfig implements WebMvcConfigurer {

    @Autowired
    private TokenInterceptor tokenInterceptor;
    @Autowired
    private SignInterceptor signInterceptor;

    //
    // 下面代码与使用@Autowired等价,相当于配置xml注册Bean实例
    // @Autowired比较简洁清楚，TokenInterceptor需要加@Component注解生成实例
    // 之前版本使用@Bean方式，后改为@Autowired写法
    //
    // @Bean
    // HandlerInterceptor tokenInterceptor() {
    // return new TokenInterceptor();
    // }

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        // 拦截顺序:
        // 1.加入(注册)的顺序就是拦截器执行的顺序;
        // 2.按顺序执行所有拦截器的preHandle,所有的preHandle执行完再执行全部postHandle,最后是afterCompletion;
        addTokenInterceptor(registry);
        addSignInterceptor(registry);
    }

    public void addTokenInterceptor(InterceptorRegistry registry) {
        List<String> pathPatterns = new ArrayList<>();
        List<String> excludePatterns = new ArrayList<>();

        pathPatterns.add("/**");

        excludePatterns.add("/ping");
        excludePatterns.add("/login");
        excludePatterns.add("/open/api/**");

        registry.addInterceptor(tokenInterceptor).addPathPatterns(pathPatterns).excludePathPatterns(excludePatterns);

    }

    public void addSignInterceptor(InterceptorRegistry registry) {
        List<String> pathPatterns = new ArrayList<>();
        List<String> excludePatterns = new ArrayList<>();

        pathPatterns.add("/**");

        excludePatterns.add("/ping");
        excludePatterns.add("/login");
        excludePatterns.add("/open/api/**");

        registry.addInterceptor(signInterceptor).addPathPatterns(pathPatterns).excludePathPatterns(excludePatterns);

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

        ParserConfig.getGlobalInstance().setSafeMode(true);

        return new HttpMessageConverters(fastConverter);
    }

}
