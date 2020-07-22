package com.demo.study.testclass.thread;


import com.demo.server.common.util.ThreadPoolUtil;
import com.mchange.v2.lang.ThreadUtils;

import java.util.concurrent.atomic.LongAdder;

public class Test {
    public static void main(String[] args) throws Exception {
        int threadNum = ThreadPoolUtil.poolSize(0.2);
        MultiTreadTestCase tc = new MultiTreadTestCase(TaskMethodApiTest.class, threadNum, 1000);
        tc.runCase();
    }
}
