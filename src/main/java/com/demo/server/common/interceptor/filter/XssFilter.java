/*
 * Copyright (c) 2020 demo ^-^.
 * @Author: yanbinjia@126.com
 * @LastModified: 2020-07-25T00:22:26.089+08:00
 */

package com.demo.server.common.interceptor.filter;

import com.demo.server.config.XssConfig;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.annotation.Order;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

@Configuration
@WebFilter(urlPatterns = {"/*"}, filterName = "XssFilter")
@Slf4j
@Order(2)
public class XssFilter implements Filter {
    @Autowired
    XssConfig xssConfig;

    private Map<String, Pattern> excludesMap = new HashMap<>();

    private FilterConfig filterConfig;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
        this.initConfig();
        log.info(">>> [{}] init ok.", this.filterConfig.getFilterName());
    }


    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        log.debug(">>> start filter deal, uri=[{}]", request.getRequestURI());

        if (this.excludes(request)) {
            filterChain.doFilter(servletRequest, servletResponse);
            log.debug(">>> after chain.doFilter(), uri=[{}]", request.getRequestURI());
            return;
        }

        if (xssConfig.getAction().equals("reject") && this.reject(request)) {
            log.debug(">>> xss reject, uri=[{}]", request.getRequestURI());
            return;
        }

        filterChain.doFilter(new XssHttpServletRequestWrapper(request, xssConfig.getAction()), servletResponse);
        log.debug(">>> after chain.doFilter(), uri=[{}]", request.getRequestURI());
    }

    @Override
    public void destroy() {
        log.debug(">>> destroy.");
    }

    private boolean excludes(HttpServletRequest request) {
        if (!xssConfig.isTurnOn()) {
            log.debug(">>> xss pass, filter is turn off.");
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
                log.debug(">>> xss pass, url=[{}] in excludes.", request.getRequestURI());
                return true;
            }
        }

        return false;
    }

    private void initConfig() {
        if (StringUtils.isNotBlank(xssConfig.getExcludes())) {
            String[] urls = xssConfig.getExcludes().trim().split(",");
            if (urls != null) {
                for (String url : urls) {
                    excludesMap.put(url, Pattern.compile("^" + url, Pattern.CASE_INSENSITIVE));
                }
            }
        }

        log.info(">>> XssConfig turnOn=[{}]", xssConfig.isTurnOn());
        log.info(">>> XssConfig excludes=[{}]", xssConfig.getExcludes());
        log.info(">>> XssConfig action=[{}]", xssConfig.getAction());
    }

    private boolean reject(HttpServletRequest request) {
        // TODO
        return false;
    }

}
