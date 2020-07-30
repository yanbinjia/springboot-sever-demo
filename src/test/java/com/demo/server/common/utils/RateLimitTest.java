package com.demo.server.common.utils;

import com.demo.study.testclass.thread.MultiTreadTestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

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
