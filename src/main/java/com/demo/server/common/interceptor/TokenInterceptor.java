/*
 * Copyright (c) 2020 demo ^-^.
 * @Author: yanbinjia@126.com
 * @LastModified: 2020-08-06T18:00:26.938+08:00
 */

package com.demo.server.common.interceptor;

import com.alibaba.fastjson.JSONObject;
import com.demo.server.bean.base.Result;
import com.demo.server.bean.base.ResultCode;
import com.demo.server.common.constant.AppConstant;
import com.demo.server.common.interceptor.filter.TraceContext;
import com.demo.server.common.utils.LogUtil;
import com.demo.server.common.utils.LogUtil.LogLevel;
import com.demo.server.service.base.security.TokenService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.PrintWriter;

@Slf4j
@Component
public class TokenInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    TokenService tokenService;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        // -----------------------------------------------------
        // 获取token参数
        // 相对规范的做法，token放在HTTP请求的头信息Authorization字段: 即 Authorization: Bearer <token>
        // 也可采用简洁的做法是，token放在HTTP请求的头信息token字段：即 token：<token>
        // 更粗暴一点放在body参数中，因为token比较长，不建议这么干
        // String token = request.getHeader("Authorization");
        // String token = request.getHeader("token");
        // String token = request.getParameter("token");

        // token
        String token = request.getHeader(AppConstant.AUTH_HEADER_NAME);
        // 需要验证token的请求参数中必须有userId，用于校验token和userId的关系
        String userId = request.getHeader(AppConstant.AUTH_UID_PARAM_NAME);

        log.debug(">>> token=[{}],userId=[{}]", token, userId);
        log.debug(">>> HandlerType [{}]", handler.getClass().getName());

        // -----------------------------------------------------
        // 检查HandlerMethod
        if (!(handler instanceof HandlerMethod)) {
            return true;
        }

        HandlerMethod handlerMethod = (HandlerMethod) handler;

        log.debug(">>> HandlerMethod [{}]", InterceptorUtil.handlerMethodToStr(handlerMethod));

        // 检查是否是自定义Controller
        if (!InterceptorUtil.isNeedIntercept(handlerMethod)) {
            return true;
        }

        // 检查是否有@TokenPass注解，有则跳过校验
        if (handlerMethod.getMethod().isAnnotationPresent(TokenPass.class)) {
            return true;
        }

        // -----------------------------------------------------
        // token 验证
        Result<String> result = tokenService.checkToken(userId, token);

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

            } catch (Exception e) {
                log.error("", e);
            }

            // return false 拦截，中断后续处理链，返回客户端
            return false;
        } else {
            // 校验成功
            TraceContext.getInstance().setUserId(userId);
        }

        return true;
    }

}
