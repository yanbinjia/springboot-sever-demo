package com.demo.study.testclass.singleton;

import java.util.Date;

public class Singleton1 {
    private static final Singleton1 SINGLETON = new Singleton1();

    private Singleton1() {
        System.out.printf("New Singleton @ %s", new Date());
    }

    public static Singleton1 getInstance() {
        return SINGLETON;
    }

    public static void main(String[] args) {
        Singleton1.getInstance();
    }
}
