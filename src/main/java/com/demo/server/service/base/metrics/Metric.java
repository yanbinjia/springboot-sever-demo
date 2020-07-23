package com.demo.server.service.base.metrics;

import lombok.Data;

@Data
public class Metric {
    private String count;
    private String method;
    MetricQps qps;
    MetricRt rt;
}
