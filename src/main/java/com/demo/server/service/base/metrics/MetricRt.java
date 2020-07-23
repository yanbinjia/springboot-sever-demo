/*
 * Copyright (c) 2020 demo ^-^.
 * @Author: yanbinjia@126.com
 * @LastModified: 2020-07-23T18:58:01.464+08:00
 */

package com.demo.server.service.base.metrics;

import lombok.Data;

@Data
public class MetricRt {
    private String min;
    private String max;
    private String mean;
    private String stddev;

    private String p50;
    private String p75;
    private String p95;
    private String p98;
    private String p99;
    private String p999;
}
