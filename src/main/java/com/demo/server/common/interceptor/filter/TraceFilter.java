/*
 * Copyright (c) 2020 demo ^-^.
 * @Author: yanbinjia@126.com
 * @LastModified: 2020-07-24T14:32:33.791+08:00
 */

package com.demo.server.common.interceptor.filter;

import com.demo.server.common.util.RandomUtil;
import com.demo.server.common.util.RequestUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
 * 注意:
 *
 * @Configuration 与Application的@ServletComponentScan同时存在,会导致filter加载2遍(2.2.8).
 * <p>
 * 如果开启了@ServletComponentScan，就需要把@Configuration去掉. 否则filter会加载2遍.
 * 如果未开启@ServletComponentScan, 需要@Configuration/@Component激活@WebFilter.
 * 所以关闭@ServletComponentScan,使用@Configuration单独开启.
 */
@Configuration
@WebFilter(urlPatterns = "/*", filterName = "TraceFilter")
@Slf4j
@Order(1)
public class TraceFilter implements Filter {

    private static final String TRACE_HEADER_NAME = "traceId";

    private FilterConfig filterConfig;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
        log.info("[{}] init ok.", this.filterConfig.getFilterName());
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) servletRequest;
        HttpServletResponse httpServletResponse = (HttpServletResponse) servletResponse;

        // 设置开始时间
        TraceContext.getInstance().setStartTime(System.currentTimeMillis());

        // 生成并设置 traceId
        String traceId = RandomUtil.uuidWithoutSeparator();
        TraceContext.getInstance().setTraceId(traceId);

        // 设置客户端IP地址
        TraceContext.getInstance().setClientIp(RequestUtil.getIp(httpServletRequest));

        // 从请求中获取某个或某些参数,根据需求可以set到Context中
        // String clientId = httpServletRequest.getParameter("clientId") + "";
        // Context.getOrNewInstance().setClientId(clientId);

        // 可统一设置Response数据
        // 响应头中设置traceId
        httpServletResponse.addHeader(TRACE_HEADER_NAME, traceId);

        log.debug(">>> start filter deal. Uri=[{}]", httpServletRequest.getRequestURI());

        try {
            filterChain.doFilter(servletRequest, servletResponse);
        } finally {
            TraceContext.getInstance().remove();
        }

        log.debug(">>> after chain.doFilter(). Uri=[{}]", httpServletRequest.getRequestURI());

    }

    @Override
    public void destroy() {
        TraceContext.getInstance().remove();
        log.debug(">>> destroy.");
    }
}
