package com.demo.study.testclass;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import sun.lwawt.macosx.CThreading;

import java.util.concurrent.TimeUnit;

public class JustTest {
    public static void main(String[] args) throws InterruptedException {
        String str = "0xxx0";
        System.out.println(str.length());
        String chars = str.substring(1, str.length());
        System.out.println(chars);

        MetricRegistry metricRegistry = new MetricRegistry();
        Meter meter1 = new Meter();
        metricRegistry.register("meter1", meter1);

        ConsoleReporter reporter = ConsoleReporter.forRegistry(metricRegistry).build();
        reporter.start(1, TimeUnit.SECONDS);

        for (int i = 0; i < 10000; i++) {
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int j = 0; j < 1000; j++) {
                        meter1.mark();
                    }
                }
            });
            t.start();
            t.join();
        }

        Thread.sleep(1000 * 60 * 60);

        System.out.println(meter1.getCount());
        System.out.println(meter1.getOneMinuteRate());
        System.out.println(meter1.getFiveMinuteRate());


    }
}
