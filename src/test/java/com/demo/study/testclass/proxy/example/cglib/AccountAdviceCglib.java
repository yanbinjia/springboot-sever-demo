package com.demo.study.testclass.proxy.example.cglib;

import org.springframework.cglib.proxy.MethodInterceptor;
import org.springframework.cglib.proxy.MethodProxy;

import java.lang.reflect.Method;

public class AccountAdviceCglib implements MethodInterceptor {
    /**
     * 代理方法, 每次调用目标方法时都会进到这里
     */
    @Override
    public Object intercept(Object obj, Method method, Object[] args, MethodProxy methodProxy) throws Throwable {
        before();
        Object result = methodProxy.invokeSuper(obj, args);
        //        return method.invoke(obj, args);  这种也行
        after();
        return result;
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