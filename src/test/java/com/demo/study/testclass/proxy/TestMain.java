package com.demo.study.testclass.proxy;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Proxy;


public class TestMain {
    public static void main(String[] args) {
        /**
         * 使用静态代理很容易就完成了对一个类的代理操作。
         * 但是静态代理的缺点也暴露了出来：由于代理只能为一个类服务，如果需要代理的类很多，那么就需要编写大量的代理类，比较繁琐。
         */
        HelloProxy helloProxy = new HelloProxy();
        helloProxy.sayhello();


        /**
         * 动态代理具体步骤：
         * 通过实现 InvocationHandler 接口创建自己的调用处理器；
         * 通过为 Proxy 类指定 ClassLoader 对象和一组 interface 来创建动态代理类；
         * 通过反射机制获得动态代理类的构造函数，其唯一参数类型是调用处理器接口类型；
         * 通过构造函数创建动态代理类实例，构造时调用处理器对象作为参数被传入。
         *
         */
        System.getProperties().setProperty("sun.misc.ProxyGenerator.saveGeneratedFiles", "true");
        HelloInterface hello = new HelloImp();
        InvocationHandler handler = new ProxyHandler(hello);
        HelloInterface proxyHello = (HelloInterface) Proxy.newProxyInstance(Thread.currentThread().getContextClassLoader(), hello.getClass().getInterfaces(), handler);
        proxyHello.sayhello();
    }
}
