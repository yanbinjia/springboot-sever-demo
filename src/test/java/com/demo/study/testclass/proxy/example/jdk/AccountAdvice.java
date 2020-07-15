package com.demo.study.testclass.proxy.example.jdk;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;

public class AccountAdvice implements InvocationHandler {
    //目标对象
    private AccountServiceInterface target;

    public AccountAdvice(AccountServiceInterface target) {
        this.target = target;
    }

    /**
     * 代理方法, 每次调用目标方法时都会进到这里
     */
    @Override
    public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
        before();
        Object invokeResult = method.invoke(target, args);
        after();
        return invokeResult;
    }

    /**
     * 前置增强
     */
    private void before() {
        System.out.println("Before invoke. do something before.");
    }

    private void after() {
        System.out.println("After invoke. do something after.");
    }
}
