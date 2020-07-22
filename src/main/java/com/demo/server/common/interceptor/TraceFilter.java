package com.demo.server.common.interceptor;

import java.io.IOException;
import java.util.UUID;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.FilterConfig;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import com.demo.server.common.util.RandomUtil;
import org.apache.commons.lang3.RandomUtils;
import org.springframework.context.annotation.Configuration;

import com.demo.server.common.util.RequestUtil;

import lombok.extern.slf4j.Slf4j;

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
public class TraceFilter implements Filter {

    private static final String TRACE_HEADER_NAME = "traceId";

    private FilterConfig filterConfig;

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
        this.filterConfig = filterConfig;
        log.info("[{}] init ok.", this.filterConfig.getFilterName());
    }

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {

        HttpServletRequest httpServletRequest = (HttpServletRequest) request;
        HttpServletResponse httpServletResponse = (HttpServletResponse) response;

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

        log.debug(">>> TraceFilter deal start. Uri=[{}]", httpServletRequest.getRequestURI());

        try {
            chain.doFilter(request, response);
        } finally {
            TraceContext.getInstance().remove();
        }

        log.debug(">>> TraceFilter after chain.doFilter(). Uri=[{}]", httpServletRequest.getRequestURI());

    }

    @Override
    public void destroy() {
        log.debug(">>> TraceFilter destroy.");
        TraceContext.getInstance().remove();
    }
}
