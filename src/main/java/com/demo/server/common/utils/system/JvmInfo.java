/*
 * Copyright (c) 2020 demo ^-^.
 * @Author: yanbinjia@126.com
 * @LastModified: 2020-07-31T11:43:36.940+08:00
 */

package com.demo.server.common.utils.system;

import lombok.Data;

@Data
public class JvmInfo {
    private String userName;
    private String userHome;
    private String userDir;
    private String classPath;
    private String javaHomePath;

    private String jvmName;
    private String jvmVersion;
    private String jvmVendor;

    private String javaVersion;
    private String javaVendor;
    private String javaVendorUrl;

    private String osName;// 操作系统名称
    private String osArch;// 操作系统内核
    private String osVersion;// 操作系统版本
    private int osAvailableProcessors;//
    private double osSystemLoadAverage;//

    private String memUnit = "MB";

    private long memMax;
    private long memTotalAllocated;// 获得JVM已分配内存
    private long memFreeInAllocated;// 获得JVM已分配内存中的剩余空间
    private long memUsable;// 获得JVM最大可用内存 memMax - memTotalAllocated + memFreeInAllocated

    private long heapMemInit;// 初始化堆内存
    private long heapMemUsed;// 已使用堆内存
    private long heapMemCommitted;//可使用堆内存
    private long heapMemMax;//最大堆内存

    private long noheapMemInit;// 初始化堆内存
    private long noheapMemUsed;// 已使用堆内存
    private long noheapMemCommitted;//可使用堆内存
    private long noheapMemMax;//最大堆内存

    private long classCountLoaded;// 当前加载类数量
    private long classCountUnloaded;// 未加载类数量
    private long classCountTotalLoaded;//

    private int threadTotalCount; // 总线程数(守护+非守护)
    private int threadDaemonCount;// 守护进程线程数
    private int threadPeakCount; // 峰值线程数

    private String timeStartTime;
    private long timeUpTime;// uptime of the Java virtual machine in milliseconds.

}
