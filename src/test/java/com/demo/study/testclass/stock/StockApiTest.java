/*
 * Copyright (c) 2020 demo ^-^.
 * @Author: yanbinjia@126.com
 * @LastModified: 2020-08-17T13:41:35.760+08:00
 */

package com.demo.study.testclass.stock;

import com.demo.server.common.utils.http.HttpUtil;
import org.apache.commons.lang.StringUtils;

import java.util.HashMap;
import java.util.Map;

public class StockApiTest {
    public static void main(String[] args) {

        Map<String, String> param = new HashMap<>();
        param.put("list", "sh600036,sh600035");
        String result = HttpUtil.sendGet("http://hq.sinajs.cn/", param, 5);
        /**
         * 股票名称、今日开盘价、昨日收盘价、当前价格、今日最高价、今日最低价、竞买价、竞卖价、成交股数、成交金额、买1手、买1报价、买2手、买2报价、…、买5报价、…、卖5报价、日期、时间
         */

        String[] resArray = StringUtils.splitByWholeSeparator(result.replace("var hq_str_sh600036=", ""), ",");
        System.out.println("股票:" + resArray[0]);
        System.out.println("当前价格:" + resArray[3]);
    }
}
