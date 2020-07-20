package com.demo.server.config;

import com.demo.server.common.util.DateUtil;
import com.demo.server.common.util.RandomUtil;
import com.demo.server.interceptor.RateLimit;
import com.google.j2objc.annotations.ReflectionSupport;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.reflections.Reflections;
import org.reflections.scanners.MethodAnnotationsScanner;
import org.reflections.scanners.TypeAnnotationsScanner;
import org.reflections.util.ConfigurationBuilder;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
import org.springframework.core.annotation.AnnotatedElementUtils;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Set;

@Configuration
@Slf4j
@Data
public class RateLimitConfig {

    public static String LIMIT_KEY_SPLIT = "->";

    String packageForScan = "com.demo";

    @Bean
    public void init() {
        this.scanAnnotationAndInit();
    }

    private void scanAnnotationAndInit() {
        log.info(">>> Init RateLimitConfig start at {} .", DateUtil.getCurrentDateTimeStr());

        String randomUnknown = RandomUtil.uuidWithoutSeparator();
        String hostAddress = "UnknownIp-" + randomUnknown;
        String hostName = "UnknownHost-" + randomUnknown;
        try {
            hostAddress = InetAddress.getLocalHost().getHostAddress();
            hostName = InetAddress.getLocalHost().getHostName();
        } catch (UnknownHostException e) {
            log.warn(">>> getLocalHost error.", e);
        }

        log.info("hostAddress={},hostName={}", hostAddress, hostName);

        Reflections reflections = new Reflections(new ConfigurationBuilder()
                .forPackages(packageForScan)
                .addScanners(new MethodAnnotationsScanner())
                .addScanners(new TypeAnnotationsScanner()));
        // 获取带有特定注解对应的类
        // Set<Class<?>> classes = reflections.getTypesAnnotatedWith(RateLimit.class);

        // 获取带有特定注解对应的方法
        Set<Method> methods = reflections.getMethodsAnnotatedWith(RateLimit.class);
        if (methods != null) {
            for (Method method : methods) {
                String className = method.getDeclaringClass().getName();
                String methodName = method.getName();
                String classLoaderName = method.getDeclaringClass().getClassLoader().getClass().getName();

                RateLimit annotation = AnnotationUtils.getAnnotation(method, RateLimit.class);
                String msg = annotation.errMsg();
                long qps = annotation.qps();
                String uri = annotation.uri();
                String resource = annotation.resource();

                String limitKey = className + LIMIT_KEY_SPLIT + methodName + "()";

                log.info("--------------------------");
                log.info("className={}", className);
                log.info("methodName={}", methodName);
                log.info("classLoaderName={}", classLoaderName);
                log.info("limitKeyInApp={}", limitKey);
                log.info("RateLimit annotation[msg={},qps={},uri={},resource={}]", msg, qps, uri, resource);
                log.info("--------------------------");

            }
        }

        log.info(">>> Init RateLimitConfig end at {} .", DateUtil.getCurrentDateTimeStr());

    }

}
