/*
 * Copyright (c) 2020 demo ^-^.
 * @Author: yanbinjia@126.com
 * @LastModified: 2020-07-30T16:43:30.009+08:00
 */

package com.demo.server.common.utils.html;

import org.springframework.web.util.HtmlUtils;

public class HtmlUtil {
    public static final String CHARSET_UTF8 = "UTF-8";

    public static String htmlEscape(String value) {
        return HtmlUtils.htmlEscape(value, CHARSET_UTF8);
    }

    public static String htmlUnescape(String value) {
        return HtmlUtils.htmlUnescape(value);
    }

    /**
     * 过滤HTML文本，防止XSS攻击
     *
     * @param htmlContent HTML内容
     * @return 过滤后的内容
     */
    public static String filter(String htmlContent) {
        return new HtmlFilter().filter(htmlContent);
    }

}
