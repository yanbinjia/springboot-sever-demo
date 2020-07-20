package com.demo.server.interceptor;

import java.lang.reflect.Method;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.AnnotationUtils;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import com.demo.server.bean.base.Result;
import com.demo.server.bean.base.ResultCode;
import com.google.common.collect.Maps;
import com.google.common.util.concurrent.RateLimiter;

import lombok.extern.slf4j.Slf4j;

@Component
@Aspect
@Slf4j
@Order(1)
public class RateLimitAspect {

    private Map<String, RateLimiter> rateLimiterMap = Maps.newConcurrentMap();

    @Pointcut("@annotation(com.demo.server.interceptor.RateLimit)")
    public void pointCut() {

    }

    @Around("pointCut()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        // 获取请求uri
        HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes())
                .getRequest();
        String uri = request.getRequestURI();


        // 获取RateLimit注解
        RateLimit rateLimitAnnotation = this.getRateLimitAnnotation(joinPoint);

        if (Objects.nonNull(rateLimitAnnotation)) {
            // 处理注解中uri标记
            if (StringUtils.isNotBlank(rateLimitAnnotation.uri())) {
                uri = rateLimitAnnotation.uri();
            }

            // 获取RateLimiter
            RateLimiter limiter = getRateLimiter(uri, rateLimitAnnotation);
            // 尝试获取令牌
            boolean acquired = limiter.tryAcquire(rateLimitAnnotation.timeout(), rateLimitAnnotation.timeUnit());

            if (!acquired) {
                // 获取失败,返回错误信息
                return new Result<String>(ResultCode.RATE_LIMITED);
            }
        }

        // 执行目标方法，Invoke the method.
        return joinPoint.proceed();
    }

    private RateLimit getRateLimitAnnotation(final JoinPoint joinPoint) {
        Method[] methods = joinPoint.getTarget().getClass().getDeclaredMethods();
        String name = joinPoint.getSignature().getName();
        if (StringUtils.isNotBlank(name)) {
            for (Method method : methods) {
                // method.getAnnotation无法获取到Spring Annotation @AliasFor的预期值
                // RateLimit rateLimitAnnotation = method.getAnnotation(RateLimit.class);
                // 使用Spring的AnnotationUtils工具, Spring Annotation 才会生效，例如 @AliasFor
                RateLimit rateLimitAnnotation = AnnotationUtils.findAnnotation(method, RateLimit.class);
                if (rateLimitAnnotation != null && name.equals(method.getName())) {
                    return rateLimitAnnotation;
                }
            }
        }
        return null;
    }

    private RateLimiter getRateLimiter(String uri, RateLimit rateLimitAnnotation) {
        RateLimiter rateLimiter = rateLimiterMap.get(uri);

        if (Objects.isNull(rateLimiter)) {
            synchronized (this) {
                rateLimiter = rateLimiterMap.get(uri);
                if (Objects.isNull(rateLimiter)) {
                    // warmupPeriod the duration of the period where the {@code RateLimiter} ramps
                    // up its rate, before reaching its stable (maximum) rate
                    long warmupPeriod = 10 * 1000;
                    rateLimiter = RateLimiter.create(rateLimitAnnotation.qps(), warmupPeriod, TimeUnit.MILLISECONDS);
                    rateLimiterMap.put(uri, rateLimiter);

                    log.info(">>> create RateLimiter: uri={},qps={},permitsPerSecond={}", uri,
                            rateLimitAnnotation.qps(), rateLimitAnnotation.permitsPerSecond());
                }
            }
        }
        return rateLimiter;
    }

}
