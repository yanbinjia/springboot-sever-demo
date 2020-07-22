package com.demo.study.testclass.proxy.example.jdk;

import java.lang.reflect.Proxy;

public class Client {
    public static void main(String[] args) {
        //创建目标对象
        AccountServiceInterface target = new AccountServiceImpl();
        //创建代理对象
        AccountServiceInterface proxy = (AccountServiceInterface) Proxy.newProxyInstance(target.getClass().getClassLoader(),
                target.getClass().getInterfaces(),
                new AccountAdvice(target)
        );
        proxy.transfer();
    }
}
