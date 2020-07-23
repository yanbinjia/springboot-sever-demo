package com.demo.server.service.base.metrics;

import com.codahale.metrics.Snapshot;
import com.codahale.metrics.Timer;

import java.util.*;
import java.util.concurrent.TimeUnit;

public class MetricUtil {

    private static Locale locale = Locale.getDefault();
    private static final TimeUnit rateUnit = TimeUnit.SECONDS;
    private static final TimeUnit durationUnit = TimeUnit.MILLISECONDS;

    private static final long durationFactor = durationUnit.toNanos(1);
    private static final long rateFactor = rateUnit.toSeconds(1);

    public static List<Metric> toTimerList(SortedMap<String, Timer> timers) {
        List<Metric> result = null;
        Timer timer = null;
        Metric metric = null;
        MetricQps qps;
        MetricRt rt;

        if (!timers.isEmpty()) {
            result = new ArrayList<>();
            for (Map.Entry<String, Timer> entry : timers.entrySet()) {
                metric = new Metric();
                qps = new MetricQps();
                rt = new MetricRt();

                timer = entry.getValue();

                metric.setMethod(entry.getKey());

                metric.setCount(timer.getCount());
                qps.setM1_rate(getRateStr(timer.getOneMinuteRate()));
                qps.setM5_rate(getRateStr(timer.getFiveMinuteRate()));
                qps.setM15_rate(getRateStr(timer.getFifteenMinuteRate()));
                qps.setMean_rate(getRateStr(timer.getMeanRate()));

                Snapshot snapshot = timer.getSnapshot();

                rt.setMin(getDurationStr(snapshot.getMin()));
                rt.setMax(getDurationStr(snapshot.getMax()));
                rt.setMean(getDurationStr(snapshot.getMean()));

                rt.setP75(getDurationStr(snapshot.get75thPercentile()));
                rt.setP95(getDurationStr(snapshot.get95thPercentile()));
                rt.setP99(getDurationStr(snapshot.get99thPercentile()));

                metric.setQps(qps);
                metric.setRt(rt);

                result.add(metric);
            }

            Collections.sort(result);

        }


        return result;
    }

    private static String getRateStr(double inputDouble) {
        // return String.format(locale, "%2.2f", convertRate(inputDouble));
        // return String.format(locale, "%2.2f calls/%s", convertRate(inputDouble), getRateUnit());
        return String.format(locale, "%2.2f qps", convertRate(inputDouble));
    }

    private static String getDurationStr(double inputDouble) {
        // return String.format(locale, "%2.2f", convertDuration(inputDouble));
        // return String.format(locale, "%2.2f %s", convertDuration(inputDouble), getDurationUnit());
        return String.format(locale, "%2.2f ms", convertDuration(inputDouble));
    }

    private static String getRateUnit() {
        return rateUnit.toString().toLowerCase(Locale.US);
    }

    private static String getDurationUnit() {
        return durationUnit.toString().toLowerCase(Locale.US);
    }

    private static double convertDuration(double duration) {
        return duration / durationFactor;
    }

    private static double convertRate(double rate) {
        return rate * rateFactor;
    }
}
