package com.demo.study.testclass.jvm;

import org.checkerframework.checker.units.qual.A;
import org.openjdk.jol.info.ClassLayout;
import org.openjdk.jol.vm.VM;
import sun.jvm.hotspot.ci.ciBaseObject;

public class Test {
    int m = 8;

    public static void main(String[] args) {
        Test test = new Test();
        Object object = new Object();

        //System.out.println(VM.current().details());
        //System.out.println(ClassLayout.parseClass(Test.class).toPrintable());
    }
}
