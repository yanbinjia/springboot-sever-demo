/*
 * Copyright (c) 2020 demo ^-^.
 * @Author: yanbinjia@126.com
 * @LastModified: 2020-07-31T11:59:00.897+08:00
 */

package com.demo.server.common.utils.system;

import com.demo.server.common.utils.NumberUtil;

import java.lang.management.*;

public class JvmUtil {

    public static JvmInfo getJvmInfo() {
        ThreadMXBean threads = ManagementFactory.getThreadMXBean();
        MemoryUsage heapMemoryUsage = ManagementFactory.getMemoryMXBean().getHeapMemoryUsage();
        MemoryUsage nonHeapMemoryUsage = ManagementFactory.getMemoryMXBean().getNonHeapMemoryUsage();
        ClassLoadingMXBean cl = ManagementFactory.getClassLoadingMXBean();
        OperatingSystemMXBean osMBean = ManagementFactory.getOperatingSystemMXBean();

        JvmInfo jvmInfo = new JvmInfo();
        jvmInfo.setUserName(System.getProperty("user.name"));
        jvmInfo.setUserHome(System.getProperty("user.home"));
        jvmInfo.setUserDir(System.getProperty("user.dir"));

        jvmInfo.setJvmName(System.getProperty("java.vm.name"));
        jvmInfo.setJvmVersion(System.getProperty("java.vm.version"));
        jvmInfo.setJvmVendor(System.getProperty("java.vm.vendor"));

        jvmInfo.setJavaVersion(System.getProperty("java.version"));
        jvmInfo.setJavaVendor(System.getProperty("java.vendor"));
        jvmInfo.setJavaVendorUrl(System.getProperty("java.vendor.url"));

        jvmInfo.setOsName(System.getProperty("os.name"));
        jvmInfo.setOsVersion(System.getProperty("os.version"));
        jvmInfo.setOsArch(System.getProperty("os.arch"));
        jvmInfo.setOsAvailableProcessors(osMBean.getAvailableProcessors());
        jvmInfo.setOsSystemLoadAverage(NumberUtil.round(osMBean.getSystemLoadAverage(), 2).doubleValue());

        jvmInfo.setHeapMemInit(bytesToMB(heapMemoryUsage.getInit()));
        jvmInfo.setHeapMemCommitted(bytesToMB(heapMemoryUsage.getCommitted()));
        jvmInfo.setHeapMemUsed(bytesToMB(heapMemoryUsage.getUsed()));
        jvmInfo.setHeapMemMax(bytesToMB(heapMemoryUsage.getMax()));

        jvmInfo.setNoheapMemInit(bytesToMB(nonHeapMemoryUsage.getInit()));
        jvmInfo.setNoheapMemCommitted(bytesToMB(nonHeapMemoryUsage.getCommitted()));
        jvmInfo.setNoheapMemUsed(bytesToMB(nonHeapMemoryUsage.getUsed()));
        jvmInfo.setNoheapMemMax(bytesToMB(nonHeapMemoryUsage.getMax()));

        jvmInfo.setClassCountLoaded(cl.getLoadedClassCount());
        jvmInfo.setClassCountUnloaded(cl.getUnloadedClassCount());
        jvmInfo.setClassCountTotalLoaded(cl.getTotalLoadedClassCount());

        jvmInfo.setMemMax(bytesToMB(Runtime.getRuntime().maxMemory()));
        jvmInfo.setMemTotalAllocated(bytesToMB(Runtime.getRuntime().totalMemory()));
        jvmInfo.setMemFreeInAllocated(bytesToMB(Runtime.getRuntime().freeMemory()));
        jvmInfo.setMemUsable(bytesToMB(getUsableMemory()));

        jvmInfo.setThreadDaemonCount(threads.getDaemonThreadCount());
        jvmInfo.setThreadPeakCount(threads.getPeakThreadCount());
        jvmInfo.setThreadTotalCount(threads.getThreadCount());

        return jvmInfo;
    }

    public static final long getUsableMemory() {
        return Runtime.getRuntime().maxMemory() - Runtime.getRuntime().totalMemory() + Runtime.getRuntime().freeMemory();
    }

    protected static int bytesToMB(long bytes) {
        return (int) (bytes / 1024 / 1024);
    }

    public static void main(String[] args) {
        System.out.println(JvmUtil.getJvmInfo());
        System.out.println(OshiUtil.getCpuInfo().toString());
    }

}
