/*
 * Copyright (c) 2020 demo ^-^.
 * @Author: yanbinjia@126.com
 * @LastModified: 2020-08-06T18:58:59.480+08:00
 */

package com.demo.server.common.utils.http;

import com.demo.server.common.utils.RandomUtil;
import com.demo.server.common.utils.thread.ThreadUtil;
import lombok.SneakyThrows;
import org.jsoup.Connection;
import org.jsoup.Jsoup;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.SortedMap;
import java.util.TreeMap;
import java.util.concurrent.atomic.LongAdder;

public class HttpUtil {
    private static final Logger logger = LoggerFactory.getLogger(HttpUtil.class);
    public static final int READ_TIMEOUT = 3 * 1000;// milliseconds
    public static final String[] USER_AGENT =
            {
                    "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.105 Safari/537.36"// chrome mac
                    , "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_6) AppleWebKit/605.1.15 (KHTML, like Gecko) Version/13.1.2 Safari/605.1.15"// safari mac
                    , "Mozilla/5.0 (Macintosh; Intel Mac OS X 10_15_6) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/84.0.4147.105 Safari/537.36 OPR/70.0.3728.95"// opera mac
                    , "Opera/9.80 (Windows NT 6.1; U; en) Presto/2.8.131 Version/11.11"// Opera11  windows7(Windows NT 6.1)
                    , "Mozilla/4.0 (compatible; MSIE 7.0; Windows NT 5.1; 360SE)"// 360浏览器 windowsXp(Windows NT 5.1)
                    , "Mozilla/5.0 (Windows NT 6.1; rv,2.0.1) Gecko/20100101 Firefox/4.0.1"// Firefox4.0.1 windows7(Windows NT 6.1)
                    , "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/67.0.3396.99 Safari/537.36"// win10 Chrome
                    , "Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; .NET4.0C; .NET4.0E; rv:11.0) like Gecko" // win10 ie11
                    , "Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36 Edge/16.16299"// win10 Edge
                    , "Mozilla/5.0 (Windows NT 10.0; Win64; x64; rv:61.0) Gecko/20100101 Firefox/61.0"// win10 Firefox
                    , "Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; .NET4.0C; .NET4.0E; rv:11.0) like Gecko"// win10 360极速-兼容模式
                    , "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/58.0.3029.110 Safari/537.36 SE 2.X MetaSr 1.0"// win10 搜狗高速
                    , "Mozilla/5.0 (Windows NT 10.0; WOW64; Trident/7.0; .NET4.0C; .NET4.0E; Core/1.63.5680.400 QQBrowser/10.2.1852.400; rv:11.0) like Gecko"//win10 QQ兼容
            };

    public static String sendGet(String url, Map<String, String> param, int timeoutSeconds) {
        String queryStr = queryStr(url, param);
        int timeout = timeoutSeconds <= 0 ? READ_TIMEOUT : timeoutSeconds * 1000;
        try {
            Connection.Response response = Jsoup.connect(queryStr)
                    .userAgent(getUserAgent())
                    .timeout(timeout)
                    .ignoreContentType(true)
                    .method(Connection.Method.GET)
                    .execute();
            logger.info("sendQuery url={}", queryStr);
            String responseStr = new String(response.bodyAsBytes(), response.charset());
            logger.info("sendQuery response: status={},charset()={},body={}", response.statusCode(), response.charset(), responseStr);
            return responseStr;
        } catch (IOException e) {
            logger.error("sendQuery error. url={}", queryStr, e);
        }
        return "";
    }

    public static String sendPost(String url, Map<String, String> param, int timeoutSeconds) {
        String queryStr = url;
        int timeout = timeoutSeconds <= 0 ? READ_TIMEOUT : timeoutSeconds * 1000;
        try {
            Connection.Response response = Jsoup.connect(url)
                    .userAgent(getUserAgent())
                    .timeout(timeout)
                    .ignoreContentType(true)
                    .method(Connection.Method.POST)
                    .data(param)
                    .execute();
            logger.info("sendQueryPost url={}", queryStr);
            String responseStr = new String(response.bodyAsBytes(), response.charset());
            logger.info("sendQueryPost response: status={},charset()={},body={}", response.statusCode(), response.charset(), responseStr);
            return responseStr;
        } catch (IOException e) {
            logger.error("sendQueryPost error. url={}", queryStr, e);
        }
        return "";
    }

    public static String getUserAgent() {
        String userAgent = "";
        userAgent = USER_AGENT[RandomUtil.randomInt(0, USER_AGENT.length)];
        return userAgent;
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

        int threadSize = ThreadUtil.availableProcessors();
        long sendCont = 10;
        LongAdder longAdder = new LongAdder();
        for (int i = 0; i < threadSize; i++) {
            String finalUrl = url;
            new Thread(new Runnable() {
                @SneakyThrows
                @Override
                public void run() {
                    while (true && longAdder.longValue() < sendCont) {
                        HttpUtil.sendPost(finalUrl, param, 2);
                        longAdder.increment();
                    }

                }
            }).start();
        }
    }
}