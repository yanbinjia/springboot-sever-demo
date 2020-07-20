package com.demo.server.service.base.metrics;

import lombok.Data;

@Data
public class MetricBean {
    String method;
    String count = "";
    String m1_rate = "";
    String m5_rate = "";
    String m15_rate = "";
    String mean_rate = "";

    String min = "";
    String max = "";
    String mean = "";
    String stddev = "";

    String p50 = "";
    String p75 = "";
    String p95 = "";
    String p98 = "";
    String p99 = "";
    String p999 = "";
}
