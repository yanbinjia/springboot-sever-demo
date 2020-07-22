package com.demo.study.testclass.singleton;

import java.util.Date;

public class SingletonStaticInnerClass {

    private static class SingletonClassInstance {
        private static final SingletonStaticInnerClass instance = new SingletonStaticInnerClass();
    }


    private SingletonStaticInnerClass() {
        System.out.printf("New SingletonStaticInnerClass @ %s", new Date());
    }

    public static SingletonStaticInnerClass getInstance() {
        return SingletonClassInstance.instance;
    }

    public static void main(String[] args) {
        SingletonStaticInnerClass.getInstance();
    }
}
