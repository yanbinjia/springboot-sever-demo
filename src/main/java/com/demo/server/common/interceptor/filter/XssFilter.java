/*
 * Copyright (c) 2020 demo ^-^.
 * @Author: yanbinjia@126.com
 * @LastModified: 2020-07-24T14:31:55.526+08:00
 */

package com.demo.server.common.interceptor.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Configuration
@WebFilter(urlPatterns = "/*", filterName = "TraceFilter")
@Slf4j
@Order(2)
public class XssFilter implements Filter {

    private FilterConfig filterConfig;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
        log.info("[{}] init ok.", this.filterConfig.getFilterName());
    }

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        log.debug(">>> start filter deal. Uri=[{}]", request.getRequestURI());

        filterChain.doFilter(new XssHttpServletRequestWrapper(request), servletResponse);

        log.debug(">>> after chain.doFilter(). Uri=[{}]", request.getRequestURI());
    }

    @Override
    public void destroy() {
        log.debug(">>> destroy.");
    }
}
