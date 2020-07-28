/*
 * Copyright (c) 2020 demo ^-^.
 * @Author: yanbinjia@126.com
 * @LastModified: 2020-07-24T14:40:16.684+08:00
 */

package com.demo.server.common.interceptor.advice;

import com.alibaba.fastjson.JSONObject;
import com.alibaba.fastjson.support.spring.FastJsonHttpMessageConverter;
import com.demo.server.bean.base.Result;
import com.demo.server.bean.base.ResultCode;
import com.demo.server.common.interceptor.filter.TraceContext;
import com.demo.server.common.util.LogUtil;
import com.demo.server.common.util.LogUtil.LogLevel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpRequest;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletRequest;

/**
 * ResponseBodyAdvice接口是在Controller执行return之后，在response返回给客户端之前，执行的对response的一些处理。
 * 比如:可以实现对response数据的统一封装、修改/增加返回值、加解密等操作。
 * <p>
 * AppResponseAdvice，用来实现请求和响应的数据统一日志功能。也可以在这进行response封装或修改处理。
 */
@ControllerAdvice
@Slf4j
public class ResponseAdvice implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        return converterType.isAssignableFrom(FastJsonHttpMessageConverter.class);
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType,
                                  Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest req,
                                  ServerHttpResponse resp) {
        HttpServletRequest request = ((ServletServerHttpRequest) req).getServletRequest();

        log.debug(">>> beforeBodyWrite deal. Uri=[{}]", request.getRequestURI());

        long cost = System.currentTimeMillis() - TraceContext.getInstance().getStartTime();

        if (TraceContext.getInstance().getStartTime() <= 0) {
            // 内部Controller(ErrorController),不经过TraceFilter,无法获取开始时间,采用默认值
            cost = 10;
        }

        String codeStr = "";
        String responseStr = "";

        try {
            if (body instanceof Result<?>) {
                Result<?> result = (Result<?>) body;
                codeStr = String.valueOf(result.getCode());
                responseStr = JSONObject.toJSONString(body);

                // Record access log.
                LogUtil.accessLog(LogLevel.INFO, request, responseStr, codeStr, cost);

            } else {
                codeStr = "";
                responseStr = JSONObject.toJSONString(body);

                // Record access log.
                LogUtil.accessLog(LogLevel.WARN, request, responseStr, codeStr, cost);
            }

        } catch (Exception e) {
            codeStr = String.valueOf(ResultCode.SYS_ERROR.code);
            responseStr = JSONObject.toJSONString(body);

            // Record access log.
            LogUtil.accessLog(LogLevel.ERROR, request, responseStr, codeStr, cost);
        }

        return body;
    }


}
