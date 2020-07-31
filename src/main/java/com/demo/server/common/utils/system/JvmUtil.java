/*
 * Copyright (c) 2020 demo ^-^.
 * @Author: yanbinjia@126.com
 * @LastModified: 2020-07-31T11:59:00.897+08:00
 */

package com.demo.server.common.utils.system;

import java.lang.management.ClassLoadingMXBean;
import java.lang.management.ManagementFactory;
import java.lang.management.MemoryUsage;
import java.lang.management.ThreadMXBean;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.NumberFormat;
import java.util.Locale;

public class JvmUtil {
    private static NumberFormat fmtI = new DecimalFormat("###,###", new DecimalFormatSymbols(Locale.ENGLISH));
    private static NumberFormat fmtD = new DecimalFormat("###,##0.000", new DecimalFormatSymbols(Locale.ENGLISH));

    public static JvmInfo getJvmInfo() {
        //线程使用情况
        ThreadMXBean threads = ManagementFactory.getThreadMXBean();
        //堆内存使用情况
        MemoryUsage heapMemoryUsage = ManagementFactory.getMemoryMXBean().getHeapMemoryUsage();
        //非堆内存使用情况
        MemoryUsage nonHeapMemoryUsage = ManagementFactory.getMemoryMXBean().getNonHeapMemoryUsage();
        //类加载情况
        ClassLoadingMXBean cl = ManagementFactory.getClassLoadingMXBean();

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

        return jvmInfo;
    }

    public static final long getUsableMemory() {
        return Runtime.getRuntime().maxMemory() - Runtime.getRuntime().totalMemory() + Runtime.getRuntime().freeMemory();
    }

    protected static int bytesToMB(long bytes) {
        return (int) (bytes / 1024 / 1024);
    }

    protected static String toDuration(double uptime) {
        uptime /= 1000;
        if (uptime < 60) {
            return fmtD.format(uptime) + " seconds";
        }
        uptime /= 60;
        if (uptime < 60) {
            long minutes = (long) uptime;
            String s = fmtI.format(minutes) + (minutes > 1 ? " minutes" : " minute");
            return s;
        }
        uptime /= 60;
        if (uptime < 24) {
            long hours = (long) uptime;
            long minutes = (long) ((uptime - hours) * 60);
            String s = fmtI.format(hours) + (hours > 1 ? " hours" : " hour");
            if (minutes != 0) {
                s += " " + fmtI.format(minutes) + (minutes > 1 ? " minutes" : " minute");
            }
            return s;
        }
        uptime /= 24;
        long days = (long) uptime;
        long hours = (long) ((uptime - days) * 24);
        String s = fmtI.format(days) + (days > 1 ? " days" : " day");
        if (hours != 0) {
            s += " " + fmtI.format(hours) + (hours > 1 ? " hours" : " hour");
        }
        return s;
    }

    public static void main(String[] args) {
        System.out.println(JvmUtil.getJvmInfo());
    }

}
