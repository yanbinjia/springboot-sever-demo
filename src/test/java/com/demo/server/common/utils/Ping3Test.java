package com.demo.server.common.utils;

import com.demo.study.testclass.thread.MultiTreadTestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;

public class Ping3Test {


    @Before
    public void setUp() throws Exception {

    }

    @After
    public void tearDown() throws Exception {

    }

    @Test
    public void rateLimitTest() throws Exception {
        MultiTreadTestCase tc = new MultiTreadTestCase(Ping3TestMethod.class, 10, 10);
        tc.runCase();
    }

}
