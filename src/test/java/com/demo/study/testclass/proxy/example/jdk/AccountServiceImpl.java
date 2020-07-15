package com.demo.study.testclass.proxy.example.jdk;

public class AccountServiceImpl implements AccountServiceInterface {
    @Override
    public void transfer() {
        System.out.println("业务操作: 例如调用dao层,完成转账主业务.");
    }
}
