package com.demo.server.common.utils;

import com.demo.server.common.utils.thread.ThreadUtil;
import com.demo.server.common.utils.thread.runner.MultiTreadRunner;
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
        MultiTreadRunner runner = new MultiTreadRunner(RateLimitTaskMethod.class, ThreadUtil.availableProcessors(), 10);
        runner.run();
    }

}
