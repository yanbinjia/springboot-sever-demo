package com.demo.study.testclass.spi;

public class TestServiceImpl implements TestService {
    @Override
    public void test(int testNum) {
        System.out.println("" + this.getClass().getName() + ", testNum=" + testNum);
    }
}
