/*
 * Copyright (c) 2020 demo ^-^.
 * @Author: yanbinjia@126.com
 * @LastModified: 2020-07-25T00:22:26.089+08:00
 */

package com.demo.server.common.interceptor.filter;

import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.PropertySource;
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
@PropertySource("classpath:security.properties")
@ConfigurationProperties(prefix = "xss")
@Data
@Slf4j
@Order(2)
public class XssFilter implements Filter {

    private boolean turnOn = true;
    private String excludes = "";// excludes config use regex
    private String action = "escape";// action support:escape,clean,reject

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

        log.debug(">>> start filter deal. Uri=[{}]", request.getRequestURI());

        if (this.excludes(request)) {
            filterChain.doFilter(servletRequest, servletResponse);
            log.debug(">>> after chain.doFilter(). Uri=[{}]", request.getRequestURI());
            return;
        }

        if (this.getAction().equals("reject") && this.reject(request)) {
            log.debug(">>> xss reject. Uri=[{}]", request.getRequestURI());
            return;
        }

        filterChain.doFilter(new XssHttpServletRequestWrapper(request, this.getAction()), servletResponse);
        log.debug(">>> after chain.doFilter(). Uri=[{}]", request.getRequestURI());
    }

    @Override
    public void destroy() {
        log.debug(">>> destroy.");
    }

    private boolean excludes(HttpServletRequest request) {
        if (!this.isTurnOn()) {
            log.debug(">>> xss pass, filter is turn off.");
            return true;
        }

        if (excludesMap == null) {
            return false;
        }

        Pattern pattern;
        for (Map.Entry<String, Pattern> entry : excludesMap.entrySet()) {
            pattern = entry.getValue();
            Matcher m = pattern.matcher(request.getRequestURI());
            if (m.find()) {
                log.debug(">>> xss pass, url in excludes. Uri=[{}]", request.getRequestURI());
                return true;
            }
        }

        return false;
    }

    private void initConfig() {
        this.action = this.getAction() == null ? "escape" : this.getAction().toLowerCase().trim();

        if (StringUtils.isNotBlank(excludes)) {
            String[] urls = excludes.split(",");
            if (urls != null) {
                for (String url : urls) {
                    excludesMap.put(url, Pattern.compile("^" + url));
                }
            }
        }

        log.info(">>> XssConfig turnOn=[{}]", this.isTurnOn());
        log.info(">>> XssConfig excludes=[{}]", this.getExcludes());
        log.info(">>> XssConfig action=[{}]", this.getAction());
    }

    private boolean reject(HttpServletRequest request) {
        Map<String, String[]> parameters = request.getParameterMap();
        if (parameters != null) {
            for (String key : parameters.keySet()) {
                String[] values = parameters.get(key);
                if (values != null) {
                    for (int i = 0; i < values.length; i++) {
                        if (XssUtil.haveIllegalStr(values[i])) {
                            return true;
                        }
                    }
                }
            }
        }

        return false;
    }

}
