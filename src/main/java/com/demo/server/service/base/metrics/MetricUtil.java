package com.demo.server.service.base.metrics;

import com.codahale.metrics.MetricAttribute;
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

    public static List<MetricBean> toTimerList(SortedMap<String, Timer> timers) {
        List<MetricBean> result = null;
        Timer timer = null;
        MetricBean metricBean = null;
        if (!timers.isEmpty()) {
            result = new ArrayList<>();
            for (Map.Entry<String, Timer> entry : timers.entrySet()) {
                metricBean = new MetricBean();
                timer = entry.getValue();

                metricBean.setMethod(entry.getKey());

                metricBean.setCount(String.valueOf(timer.getCount()));
                metricBean.setM1_rate(getRateStr(timer.getOneMinuteRate()));
                metricBean.setM5_rate(getRateStr(timer.getFiveMinuteRate()));
                metricBean.setM15_rate(getRateStr(timer.getFifteenMinuteRate()));
                metricBean.setMean_rate(getRateStr(timer.getMeanRate()));

                Snapshot snapshot = timer.getSnapshot();

                metricBean.setMin(getDurationStr(snapshot.getMin()));
                metricBean.setMax(getDurationStr(snapshot.getMax()));
                metricBean.setMean(getDurationStr(snapshot.getMean()));
                metricBean.setStddev(getDurationStr(snapshot.getStdDev()));

                metricBean.setP50(getDurationStr(snapshot.getMedian()));
                metricBean.setP75(getDurationStr(snapshot.get75thPercentile()));
                metricBean.setP95(getDurationStr(snapshot.get95thPercentile()));
                metricBean.setP98(getDurationStr(snapshot.get98thPercentile()));
                metricBean.setP99(getDurationStr(snapshot.get99thPercentile()));
                metricBean.setP999(getDurationStr(snapshot.get999thPercentile()));

                result.add(metricBean);
            }
        }

        return result;
    }

    private static String getRateStr(double inputDouble) {
        // return String.format(locale, "%2.2f", convertRate(inputDouble));
        return String.format(locale, "%2.2f calls/%s", convertRate(inputDouble), getRateUnit());
    }

    private static String getDurationStr(double inputDouble) {
        // String.format(locale, "%2.2f %s", convertDuration(inputDouble), getDurationUnit())
        // return String.format(locale, "%2.2f", convertDuration(inputDouble));
        return String.format(locale, "%2.2f %s", convertDuration(inputDouble), getDurationUnit());

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
