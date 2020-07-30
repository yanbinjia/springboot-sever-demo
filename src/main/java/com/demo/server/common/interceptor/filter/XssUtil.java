/*
 * Copyright (c) 2020 demo ^-^.
 * @Author: yanbinjia@126.com
 * @LastModified: 2020-07-25T10:28:43.376+08:00
 */

package com.demo.server.common.interceptor.filter;

import com.demo.server.common.utils.html.HtmlUtil;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.commons.lang3.StringUtils;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.springframework.web.util.HtmlUtils;

public class XssUtil {

    public static final String CHARSET_UTF8 = "UTF-8";

    public static String doFilter(String value, String action) {

        if (StringUtils.isBlank(action)) {
            return HtmlUtils.htmlEscape(value, CHARSET_UTF8);
        }

        switch (action.toLowerCase().trim()) {
            case "escape":
                return HtmlUtils.htmlEscape(value, CHARSET_UTF8);
            case "clean":
                return HtmlUtil.filter(value);
            default:
                return HtmlUtils.htmlEscape(value, CHARSET_UTF8);
        }
    }

    public static void main(String[] args) {
        String inputTest = ";;;<a>;;;A标签;;;</a>/s<ScriPt>eval('中文')\"测试\"😁😀😢😄<script></script>　＞＞　''＇＇";
        System.out.println("inputTest: " + inputTest);
        System.out.println("clean:");
        System.out.println("Whitelist.none(): " + Jsoup.clean(inputTest, Whitelist.none()));
        System.out.println("Whitelist.relaxed(): " + Jsoup.clean(inputTest, Whitelist.relaxed()));
        System.out.println("Whitelist.basic(): " + Jsoup.clean(inputTest, Whitelist.basic()));

        System.out.println("escape:");
        System.out.println("StringEscapeUtils: " + StringEscapeUtils.escapeHtml(inputTest));
        System.out.println("SpringEscape: " + org.springframework.web.util.HtmlUtils.htmlEscape(inputTest, CHARSET_UTF8));
        System.out.println("SpringUnEscape: " + HtmlUtils.htmlUnescape(HtmlUtils.htmlEscape(inputTest, CHARSET_UTF8)));
        System.out.println("HtmlUtil: " + HtmlUtil.filter(inputTest));
        System.out.println("HtmlUtilUnEscape: " + HtmlUtil.htmlUnescape(HtmlUtil.filter(inputTest)));
    }
}
