package com.demo.study.testclass.singleton;

import java.util.Date;

public class Singleton {
    // volatile语义 1.线程可见; 2.禁止指令重排
    private static volatile Singleton SINGLETON;

    private Singleton() {
        System.out.printf("New Singleton @ %s", new Date());
    }

    public static Singleton getInstance() {
        if (SINGLETON == null) {
            synchronized (Singleton.class) {
                if (SINGLETON == null) {// DCL Double Check Lock
                    SINGLETON = new Singleton();
                }
            }
        }
        return SINGLETON;
    }

    public static void main(String[] args) {
        Singleton.getInstance();
    }
}
