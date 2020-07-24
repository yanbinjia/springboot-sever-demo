package com.demo.server.common.interceptor;

import java.io.IOException;
import java.io.PrintWriter;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.handler.HandlerInterceptorAdapter;

import com.alibaba.fastjson.JSONObject;
import com.demo.server.bean.base.Result;
import com.demo.server.bean.base.ResultCode;
import com.demo.server.common.util.LogUtil;
import com.demo.server.common.util.LogUtil.LogLevel;
import com.demo.server.config.SignConfig;
import com.demo.server.service.base.security.SignService;

import lombok.extern.slf4j.Slf4j;

@Component
@Slf4j
public class SignInterceptor extends HandlerInterceptorAdapter {
    @Autowired
    SignService signService;
    @Autowired
    SignConfig signConfig;

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler)
            throws Exception {
        // -----------------------------------------------------
        // 开关
        if (!signConfig.isTurnOn()) {
            log.debug(">>> signConfig turnOff.");
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

        log.debug(">>> preHandle HandlerMethod [{}]", HandlerInterceptorUtil.handlerMethodToStr(handlerMethod));

        // 检查是否是自定义Controller
        if (!HandlerInterceptorUtil.isNeedIntercept(handlerMethod)) {
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

}
