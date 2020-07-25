/*
 * Copyright (c) 2020 demo ^-^.
 * @Author: yanbinjia@126.com
 * @LastModified: 2020-07-25T10:28:43.376+08:00
 */

package com.demo.server.common.interceptor.filter;

import com.google.common.escape.Escaper;
import org.apache.commons.lang.StringEscapeUtils;
import org.apache.tomcat.util.security.Escape;
import org.jsoup.Jsoup;
import org.jsoup.safety.Whitelist;
import org.springframework.web.util.HtmlUtils;

public class XssUtil {

    public static enum FlterAction {
        ESCAPE, CLEAN, REJECT;
    }

    public static final String CHARSET_UTF8 = "UTF-8";

    public static String doFilter(String value, String action) {
        switch (action) {
            case "escape":
                return HtmlUtils.htmlEscape(value, CHARSET_UTF8);
            case "clean":
                return Jsoup.clean(value, Whitelist.none());
            default:
                return HtmlUtils.htmlEscape(value, CHARSET_UTF8);
        }
    }

    public static boolean haveIllegalStr(String value) {
        // TODO
        return false;
    }

    public static void main(String[] args) {

        String htmlStr = "<a href='sdfs'>xxxx辅导费<script > alert();对对对 </script >";

        System.out.println("escape:");
        System.out.println(XssUtil.doFilter(htmlStr, "escape"));
        System.out.println(StringEscapeUtils.escapeHtml(htmlStr));

        System.out.println("clean:");
        System.out.println(Jsoup.clean(htmlStr, Whitelist.none()));
        System.out.println(Jsoup.clean(htmlStr, Whitelist.relaxed()));
        System.out.println(Jsoup.clean(htmlStr, Whitelist.basic()));

        System.out.println("unescape:");
        System.out.println(HtmlUtils.htmlUnescape("\"'&lt;a href=&#39;sdfs&#39;&gt;&lt;/a&gt; &lt; script &gt; alert(); &lt;/ script &gt;"));

    }

}
