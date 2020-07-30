package com.demo.study.testclass.thread;


import com.demo.server.common.utils.ThreadPoolUtil;

public class Test {
    public static void main(String[] args) throws Exception {
        int threadNum = ThreadPoolUtil.poolSize(0.2);
        MultiTreadTestCase tc = new MultiTreadTestCase(TaskMethodApiTest.class, threadNum, 1000);
        tc.runCase();
    }
}
