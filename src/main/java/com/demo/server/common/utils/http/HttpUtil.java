/*
 * Copyright (c) 2020 demo ^-^.
 * @Author: yanbinjia@126.com
 * @LastModified: 2020-08-06T18:58:59.480+08:00
 */

package com.demo.server.common.utils.http;

import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;

public class HttpUtil {
    private static final Logger logger = LoggerFactory.getLogger(HttpUtil.class);
    public static final int READ_TIMEOUT = 3 * 1000;// milliseconds
    /**
     * Chrome@2020 版本号 84.0.4147.105
     * <p>
     * Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.105 Safari/537.36
     */
    public static final String USER_AGENT = "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.105 Safari/537.36";

    public static String sendGet(String url, Map<String, String> param, int timeoutSec) {
        String queryStr = queryStr(url, param);
        int timeout = timeoutSec <= 0 ? READ_TIMEOUT : timeoutSec * 1000;
        try {
            Connection.Response response = Jsoup.connect(queryStr)
                    .userAgent(USER_AGENT).timeout(timeout)
                    .ignoreContentType(true)
                    .method(Connection.Method.GET)
                    .execute();
            logger.info("sendQuery url={}", queryStr);
            String responseStr = new String(response.bodyAsBytes(), response.charset());
            logger.info("sendQuery response.charset()={}, response.body={}", response.charset(), responseStr);
            return responseStr;
        } catch (IOException e) {
            logger.error("sendQuery error. url={}", queryStr, e);
        }
        return "";
    }

    public static String sendPost(String url, Map<String, String> param, int timeoutSec) {
        String queryStr = url;
        int timeout = timeoutSec <= 0 ? READ_TIMEOUT : timeoutSec * 1000;
        try {
            Connection.Response response = Jsoup.connect(url)
                    .userAgent(USER_AGENT).timeout(timeout)
                    .ignoreContentType(true)
                    .method(Connection.Method.POST)
                    .data(param)
                    .execute();
            logger.info("sendQueryPost url={}", queryStr);
            String responseStr = new String(response.bodyAsBytes(), response.charset());
            logger.info("sendQueryPost response.charset()={}, response.body={}", response.charset(), responseStr);
            return responseStr;
        } catch (IOException e) {
            logger.error("sendQueryPost error. url={}", queryStr, e);
        }
        return "";
    }

    public static String queryStr(String url, Map<String, String> param) {
        return (param != null && param.size() > 0) ? (url + "?" + paramStr(param)) : url;
    }

    public static String paramStr(Map<String, String> param) {
        StringBuffer paramStrSb = new StringBuffer();
        if (param != null && param.size() > 0) {
            // 处理请求参数,对参数按照 key1=value1&key2=value2 的格式，并按照参数名ASCII字典序排序
            SortedMap<String, String> paramsSortedMap = new TreeMap<>(param);
            paramsSortedMap.forEach((k, v) -> {
                if (paramStrSb.length() == 0) {
                    paramStrSb.append(k + "=" + v);
                } else {
                    paramStrSb.append("&" + k + "=" + v);
                }
            });
        }
        return paramStrSb.toString();
    }


    public static void main(String[] args) throws IOException {
        Map<String, String> param = new HashMap<>();
        param.put("tel", "13466799355");
        String url = "https://tcc.taobao.com/cc/json/mobile_tel_segment.htm";

        HttpUtil.sendGet(url, param, 2);

        url = "https://wis.qq.com/weather/common?source=xw&weather_type=forecast_24h";
        param.clear();
        param.put("province", "北京");
        param.put("city", "北京");

        HttpUtil.sendPost(url, param, 2);

        url = "https://api.apiopen.top/getWangYiNews";
        param.clear();
        param.put("page", "1");
        param.put("count", "10");

        HttpUtil.sendPost(url, param, 2);
    }
}