/*
 * Copyright (c) 2020 demo ^-^.
 * @Author: yanbinjia@126.com
 * @LastModified: 2020-07-24T14:31:55.536+08:00
 */

package com.demo.server.common.interceptor.filter;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.util.HtmlUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.Enumeration;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {

    public static final String ENCODING = "UTF-8";

    public XssHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    @Override
    public String getHeader(String name) {
        String header = super.getHeader(name);
        if (StringUtils.isNotBlank(header)) {
            return this.doFilter(header);
        }
        return header;
    }

    @Override
    public String getParameter(String name) {
        String parameter = super.getParameter(name);
        if (StringUtils.isNotBlank(parameter)) {
            return this.doFilter(parameter);
        }
        return parameter;
    }

    @Override
    public String[] getParameterValues(String name) {
        String[] values = super.getParameterValues(name);
        if (values == null) {
            return values;
        }
        for (int i = 0; i < values.length; i++) {
            if (StringUtils.isNotBlank(values[i])) {
                values[i] = this.doFilter(values[i]);
            }
        }
        return values;
    }

    @Override
    public Map<String, String[]> getParameterMap() {
        Map<String, String[]> parameters = super.getParameterMap();
        Map<String, String[]> filteredMap = null;
        if (parameters != null) {
            filteredMap = new LinkedHashMap<>();
            for (String key : parameters.keySet()) {
                String[] values = parameters.get(key);
                for (int i = 0; i < values.length; i++) {
                    values[i] = this.doFilter(values[i]);
                }
                filteredMap.put(key, values);
            }
        }
        return filteredMap;
    }

    private String doFilter(String value) {
        return HtmlUtils.htmlEscape(value, ENCODING);
    }
}
