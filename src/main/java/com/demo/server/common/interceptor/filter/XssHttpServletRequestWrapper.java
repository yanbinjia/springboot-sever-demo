/*
 * Copyright (c) 2020 demo ^-^.
 * @Author: yanbinjia@126.com
 * @LastModified: 2020-07-24T14:31:55.536+08:00
 */

package com.demo.server.common.interceptor.filter;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.util.LinkedHashMap;
import java.util.Map;

@Slf4j
public class XssHttpServletRequestWrapper extends HttpServletRequestWrapper {

    private String action = "";

    public XssHttpServletRequestWrapper(HttpServletRequest request) {
        super(request);
    }

    public XssHttpServletRequestWrapper(HttpServletRequest request, String action) {
        super(request);
        this.action = action;
    }

    @Override
    public String getHeader(String name) {
        String value = super.getHeader(name);
        if (StringUtils.isNotBlank(value)) {
            return XssUtil.doFilter(value, this.action);
        }
        return value;
    }

    @Override
    public String getParameter(String name) {
        String value = super.getParameter(name);
        if (StringUtils.isNotBlank(value)) {
            return XssUtil.doFilter(value, this.action);
        }
        return value;
    }

    @Override
    public String[] getParameterValues(String name) {
        String[] values = super.getParameterValues(name);
        if (values == null) {
            return values;
        }
        for (int i = 0; i < values.length; i++) {
            if (StringUtils.isNotBlank(values[i])) {
                values[i] = XssUtil.doFilter(values[i], this.action);
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
                if (values != null) {
                    for (int i = 0; i < values.length; i++) {
                        values[i] = XssUtil.doFilter(values[i], this.action);
                    }
                }
                filteredMap.put(key, values);
            }
        }
        return filteredMap;
    }

}
