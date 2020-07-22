package com.demo.study.testclass.thread;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import org.jsoup.Connection;
import org.jsoup.Jsoup;

public class TaskMethodApiTest implements TaskMethod {
    boolean success = false;
    String url = "http://127.0.0.1:6673/test/rate/test";

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
