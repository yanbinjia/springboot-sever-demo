package com.demo.study.testclass.metricslib;

import com.codahale.metrics.ConsoleReporter;
import com.codahale.metrics.Meter;
import com.codahale.metrics.MetricRegistry;
import com.codahale.metrics.Timer;
import com.demo.server.common.utils.RandomUtil;

import java.util.concurrent.TimeUnit;

public class MetricsLib {
    public static void main(String[] args) throws InterruptedException {

        MetricRegistry metricRegistry = new MetricRegistry();
        Meter meter1 = new Meter();
        metricRegistry.register("meter1", meter1);

        Timer timer = new Timer();
        metricRegistry.register("timer", timer);

        ConsoleReporter reporter = ConsoleReporter.forRegistry(metricRegistry).build();
        reporter.start(5, TimeUnit.SECONDS);

        int threadNum = 1000;
        int loopSize = 1000;
        for (int i = 0; i < threadNum; i++) {
            Thread t = new Thread(new Runnable() {
                @Override
                public void run() {
                    for (int j = 0; j < loopSize; j++) {
                        meter1.mark();
                        timer.update(RandomUtil.randomLong(150, 800), TimeUnit.MILLISECONDS);
                    }
                }
            });
            t.start();
            t.join();
        }

        Thread.sleep(1000 * 10);

        System.out.println(meter1.getCount());
        System.out.println(meter1.getOneMinuteRate());
        System.out.println(meter1.getFiveMinuteRate());

        /**
         * 20-7-20 11:42:42 ===============================================================
         *
         * -- Meters ----------------------------------------------------------------------
         * meter1
         *              count = 10000000
         *          mean rate = 1838859.82 events/second
         *      1-minute rate = 2000000.00 events/second
         *      5-minute rate = 2000000.00 events/second
         *     15-minute rate = 2000000.00 events/second
         * -- Timers ----------------------------------------------------------------------
         * timer
         *              count = 10000000
         *          mean rate = 1838731.92 calls/second
         *      1-minute rate = 2000000.00 calls/second
         *      5-minute rate = 2000000.00 calls/second
         *     15-minute rate = 2000000.00 calls/second
         *                min = 150.00 milliseconds
         *                max = 797.00 milliseconds
         *               mean = 478.68 milliseconds
         *             stddev = 186.16 milliseconds
         *             median = 479.00 milliseconds
         *               75% <= 640.00 milliseconds
         *               95% <= 764.00 milliseconds
         *               98% <= 787.00 milliseconds
         *               99% <= 793.00 milliseconds
         *             99.9% <= 797.00 milliseconds
         */
    }
}
