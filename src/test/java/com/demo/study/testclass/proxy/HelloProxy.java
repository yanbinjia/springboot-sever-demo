package com.demo.study.testclass.proxy;


public class HelloProxy implements HelloInterface {

    private HelloInterface hello = new HelloImp();

    @Override
    public void sayhello() {
        System.out.println("Before invoke sayHello");
        hello.sayhello();
        System.out.println("After invoke sayHello");
    }
}
