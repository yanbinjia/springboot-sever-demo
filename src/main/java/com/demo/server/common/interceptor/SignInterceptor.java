/*
 * Copyright (c) 2020 demo ^-^.
 * @Author: yanbinjia@126.com
 * @LastModified: 2020-07-28T19:01:46.984+08:00
 */

package com.demo.server.common.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.demo.server.bean.base.Result;
import com.demo.server.bean.base.ResultCode;
import com.demo.server.common.interceptor.filter.TraceContext;
import com.demo.server.common.utils.LogUtil;
import com.demo.server.common.utils.LogUtil.LogLevel;
import com.demo.server.config.SignConfig;
import com.demo.server.service.base.security.SignService;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.annotation.PostConstruct;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.io.PrintWriter;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Component
@Slf4j
public class SignInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    SignService signService;
    @Autowired
    SignConfig signConfig;

    private Map<String, Pattern> excludesMap = new HashMap<>();

    @PostConstruct
    public void initConfig() {
        if (StringUtils.isNotBlank(signConfig.getExcludes())) {
            String[] urls = signConfig.getExcludes().trim().split(",");
            if (urls != null) {
                for (String url : urls) {
                    excludesMap.put(url, Pattern.compile("^" + url, Pattern.CASE_INSENSITIVE));
                }
            }
        }

        log.info(">>> SignConfig turnOn=[{}]", signConfig.isTurnOn());
        log.info(">>> SignConfig algorithm=[{}]", signConfig.getAlgorithm());
        log.info(">>> SignConfig excludes=[{}]", signConfig.getExcludes());
        log.info(">>> SignConfig timestampExpireSecs=[{}]", signConfig.getTimestampExpireSecs());
        log.info(">>> SignConfig printCheckInfo=[{}]", signConfig.isPrintCheckInfo());
    }

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        // -----------------------------------------------------
        if (this.excludes(request)) {
            return true;
        }

        log.debug(">>> preHandle HandlerType [{}]", handler.getClass().getName());

        // -----------------------------------------------------
        // 检查HandlerMethod
        if (!(handler instanceof HandlerMethod)) {
            // 如果不是映射到方法直接通过
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;

        log.debug(">>> preHandle HandlerMethod [{}]", InterceptorUtil.handlerMethodToStr(handlerMethod));

        // 检查是否是自定义Controller
        if (!InterceptorUtil.isNeedIntercept(handlerMethod)) {
            return true;
        }

        // 检查是否有@SignPass注解，有则跳过校验
        if (handlerMethod.getMethod().isAnnotationPresent(SignPass.class)) {
            return true;
        }

        // -----------------------------------------------------
        // 验证签名
        Result<String> result = signService.checkSign(request);

        if (result.getCode() != ResultCode.SUCCESS.code) {
            // 校验失败
            try {
                // 向客户端写响应信息
                response.setContentType("application/json; charset=utf-8");
                PrintWriter out = response.getWriter();
                String responseStr = JSONObject.toJSONString(result);
                out.print(responseStr);

                // Record access log.
                long cost = System.currentTimeMillis() - TraceContext.getInstance().getStartTime();
                LogUtil.accessLog(LogLevel.WARN, request, responseStr, String.valueOf(result.getCode()), cost);

            } catch (IOException e) {
                log.error("", e);
            }

            // return false 拦截，中断后续处理链，返回客户端
            return false;
        }

        return true;
    }

    private boolean excludes(HttpServletRequest request) {
        if (!signConfig.isTurnOn()) {
            log.debug(">>> sign pass, sign is turn off.");
            return true;
        }

        if (excludesMap == null || excludesMap.isEmpty()) {
            return false;
        }

        Pattern pattern;
        Matcher m;
        for (Map.Entry<String, Pattern> entry : excludesMap.entrySet()) {
            pattern = entry.getValue();
            m = pattern.matcher(request.getRequestURI());
            if (m.find()) {
                log.debug(">>> sign pass, uri=[{}] in excludes. ", request.getRequestURI());
                return true;
            }
        }

        return false;
    }

}
