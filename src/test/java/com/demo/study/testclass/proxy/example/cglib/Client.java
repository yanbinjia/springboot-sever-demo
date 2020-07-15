package com.demo.study.testclass.proxy.example.cglib;

import org.springframework.cglib.proxy.Enhancer;

public class Client {
    public static void main(String[] args) {
        //创建目标对象
        AccountService target = new AccountService();
        //创建代理对象
        AccountService proxy = (AccountService) Enhancer.create(target.getClass(),
                new AccountAdviceCglib());
        proxy.transfer();

    }
}
