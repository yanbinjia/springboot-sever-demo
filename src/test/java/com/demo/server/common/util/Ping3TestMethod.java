package com.demo.server.common.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.demo.study.testclass.thread.TaskMethod;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

public class Ping3TestMethod implements TaskMethod {
    boolean success = false;
    String url = "http://127.0.0.1:6673/ping3";

    @Override
    public boolean run() {
        try {
            Connection.Response response = Jsoup.connect(url).ignoreContentType(true).execute();
            String jsonStr = response.body();

            JSONObject jsonObject = JSON.parseObject(jsonStr);
            int code = jsonObject.getInteger("code");

            if (code == 0) {
                success = true;
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return success;
    }
}
