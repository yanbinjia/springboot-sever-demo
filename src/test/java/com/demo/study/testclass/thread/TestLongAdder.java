package com.demo.study.testclass.thread;

import java.util.concurrent.atomic.LongAccumulator;
import java.util.concurrent.atomic.LongAdder;
import java.util.function.LongBinaryOperator;

public class TestLongAdder {

    /**
     * JDK8新增 LongAdder,LongAccumulator
     * 解决高并发下的对一个变量CAS竞争激烈，自旋导致CPU资源浪费
     */

    public static void main(String[] args) {
        // LongAdder,无法初始值,从0开始
        LongAdder longAdder = new LongAdder();
        longAdder.sum();
        System.out.println("longAdder=" + longAdder.longValue());
        longAdder.add(100);
        longAdder.increment();
        System.out.println("longAdder=" + longAdder.longValue());


        // LongAccumulator,可指定accumulate操作方法和初始值,LongAdder的加强版本
        LongAccumulator longAccumulator = new LongAccumulator(new LongBinaryOperator() {
            @Override
            public long applyAsLong(long left, long right) {
                return left + right;
            }
        }, 100);
        System.out.println("longAccumulator=" + longAccumulator.longValue());
        longAccumulator.accumulate(1);
        System.out.println("longAccumulator=" + longAccumulator.longValue());
        longAccumulator.accumulate(100);
        System.out.println("longAccumulator=" + longAccumulator.longValue());


    }
}
