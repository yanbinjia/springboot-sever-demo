package com.demo.server.service.base.metrics;

import lombok.Data;

@Data
public class Metric implements Comparable<Metric> {
    private long count;
    private String method;
    MetricQps qps;
    MetricRt rt;

    @Override
    public int compareTo(Metric o) {
        return (this.count <= o.count) ? 0 : -1;
    }
}
