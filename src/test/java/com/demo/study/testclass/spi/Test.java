package com.demo.study.testclass.spi;

import java.util.ServiceLoader;

public class Test {
    public static void main(String[] args) {
        ServiceLoader<TestService> loadedParsers = ServiceLoader.load(TestService.class);
        for (TestService ser : loadedParsers) {
            ser.test(100);
        }
    }
}
