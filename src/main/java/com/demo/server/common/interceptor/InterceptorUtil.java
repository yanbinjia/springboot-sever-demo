package com.demo.server.common.interceptor;

import lombok.extern.slf4j.Slf4j;
import org.springframework.util.StringUtils;
import org.springframework.web.method.HandlerMethod;

import java.lang.annotation.Annotation;

@Slf4j
public class InterceptorUtil {

    /**
     * 检查是否是需要拦截Controller,只拦截自定义的Controller
     * 框架内部的不进行拦截,如:org.springframework.boot.autoconfigure.web.servlet.error.BasicErrorController)
     */
    public static boolean isNeedIntercept(final HandlerMethod handlerMethod) {
        String controllerName = handlerMethod.getBean().getClass().getName();
        if (!StringUtils.startsWithIgnoreCase(controllerName, "com.demo.server.web.")) {
            log.debug("NoNeedIntercept controller=[{}]", controllerName);
            return false;
        }
        return true;

    }

    public static String getClassName(final HandlerMethod handlerMethod) {
        return handlerMethod.getBean().getClass().getName();
    }

    public static String getMethodName(final HandlerMethod handlerMethod) {
        return handlerMethod.getMethod().getName();
    }

    /**
     * 获取HandlerMethod对象基本信息
     */
    public static String handlerMethodToStr(final HandlerMethod handlerMethod) {
        StringBuilder sb = new StringBuilder();
        sb.append("specific-handler=");
        sb.append(handlerMethod.getBean().getClass().getName());
        sb.append("; ");
        sb.append("method-name=");
        sb.append(handlerMethod.getMethod().getName());
        sb.append("; ");
        sb.append("annotations=");
        for (Annotation ano : handlerMethod.getMethod().getAnnotations()) {
            sb.append(ano.toString() + " ");
        }
        sb.append("; ");

        return sb.toString();
    }

}
