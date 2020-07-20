package com.demo.server.common.util;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.atomic.AtomicInteger;

import com.demo.study.testclass.thread.MultiTreadTestCase;
import com.demo.study.testclass.thread.TaskMethodApiTest;
import org.jsoup.Connection.Response;
import org.jsoup.Jsoup;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;

public class RateLimitTest {


    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void rateLimitTest() throws Exception {
        MultiTreadTestCase tc = new MultiTreadTestCase(RateLimitTaskMethod.class, 10, 100);
        tc.runCase();
    }

}
