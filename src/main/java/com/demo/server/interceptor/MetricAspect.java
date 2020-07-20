package com.demo.server.interceptor;

import com.demo.server.service.base.metrics.EventType;
import com.demo.server.service.base.metrics.MetricEvent;
import com.demo.server.service.base.metrics.MetricService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.lang.reflect.Method;

@Component
@Aspect
@Slf4j
@Order(-100)
public class MetricAspect {

    /**
     * * @see GetMapping
     * * @see PostMapping
     * * @see PutMapping
     * * @see DeleteMapping
     * * @see PatchMapping
     */
    @Pointcut("@annotation(org.springframework.web.bind.annotation.RequestMapping) " +
            "|| @annotation(org.springframework.web.bind.annotation.GetMapping) " +
            "|| @annotation(org.springframework.web.bind.annotation.PostMapping) " +
            "|| @annotation(org.springframework.web.bind.annotation.PutMapping) ")
    public void pointCut() {

    }

    @Around("pointCut()")
    public Object doAround(ProceedingJoinPoint joinPoint) throws Throwable {
        long startTime = System.currentTimeMillis();
        String methodStr = this.getMethodStr(joinPoint);

        // 执行目标方法，Invoke the method.
        Object object = joinPoint.proceed();

        if (StringUtils.isNotBlank(methodStr)) {
            MetricEvent metircData = new MetricEvent();
            metircData.setEventType(EventType.NORMAL);
            metircData.setMetricKey(methodStr);
            metircData.setCost(System.currentTimeMillis() - startTime);

            MetricService.putMetricEvent(metircData);
        }

        return object;
    }

    private String getMethodStr(final JoinPoint joinPoint) {
        Method[] methods = joinPoint.getTarget().getClass().getDeclaredMethods();
        String name = joinPoint.getSignature().getName();

        String className = "";
        String methodName = "";
        String metricKey = "";
        if (StringUtils.isNotBlank(name)) {
            for (Method method : methods) {
                if (name.equals(method.getName())) {
                    className = method.getDeclaringClass().getName();
                    methodName = method.getName();
                    metricKey = className + MetricService.LIMIT_KEY_SPLIT + methodName + "()";
                    /*
                    log.debug("--------------------------");
                    log.debug("className={}", className);
                    log.debug("methodName={}", methodName);
                    log.debug("limitKeyInApp={}", metricKey);
                    log.debug("--------------------------");
                    */
                    return metricKey;
                }
            }
        }
        return null;
    }

}
