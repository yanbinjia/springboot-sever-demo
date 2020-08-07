/*
 * Copyright (c) 2020 demo ^-^.
 * @Author: yanbinjia@126.com
 * @LastModified: 2020-07-31T11:59:00.897+08:00
 */

package com.demo.server.common.utils.system;

import com.demo.server.common.utils.DateUtil;
import com.demo.server.common.utils.NumberUtil;
import org.apache.commons.lang.StringUtils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.OutputStream;
import java.lang.management.*;
import java.util.Date;

public class JvmUtil {

    private JvmUtil() {
    }

    public static String getUserDir() {
        return System.getProperty("user.dir");
    }

    public static String getClassPathRoot() {
        return JvmUtil.class.getResource("/").getPath();
    }

    public static String getJavaHomePath() {
        return System.getProperty("java.home");
    }


    public static JvmInfo getJvmInfo() {
        ThreadMXBean threads = ManagementFactory.getThreadMXBean();
        MemoryUsage heapMemoryUsage = ManagementFactory.getMemoryMXBean().getHeapMemoryUsage();
        MemoryUsage nonHeapMemoryUsage = ManagementFactory.getMemoryMXBean().getNonHeapMemoryUsage();
        ClassLoadingMXBean cl = ManagementFactory.getClassLoadingMXBean();
        OperatingSystemMXBean osMBean = ManagementFactory.getOperatingSystemMXBean();
        RuntimeMXBean rb = ManagementFactory.getRuntimeMXBean();

        JvmInfo jvmInfo = new JvmInfo();
        jvmInfo.setUserName(System.getProperty("user.name"));
        jvmInfo.setUserHome(System.getProperty("user.home"));
        jvmInfo.setUserDir(System.getProperty("user.dir"));
        jvmInfo.setClassPath(getClassPathRoot());
        jvmInfo.setJavaHomePath(getJavaHomePath());

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

        jvmInfo.setMemMax(bytesToMB(Runtime.getRuntime().maxMemory()));
        jvmInfo.setMemTotalAllocated(bytesToMB(Runtime.getRuntime().totalMemory()));
        jvmInfo.setMemFreeInAllocated(bytesToMB(Runtime.getRuntime().freeMemory()));
        jvmInfo.setMemUsable(bytesToMB(getUsableMemory()));

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

        jvmInfo.setThreadDaemonCount(threads.getDaemonThreadCount());
        jvmInfo.setThreadPeakCount(threads.getPeakThreadCount());
        jvmInfo.setThreadTotalCount(threads.getThreadCount());

        jvmInfo.setTimeStartTime(DateUtil.getDateTimeStr(new Date(rb.getStartTime())));
        jvmInfo.setTimeUpTime(rb.getUptime());

        return jvmInfo;
    }

    public static final long getUsableMemory() {
        return Runtime.getRuntime().maxMemory() - Runtime.getRuntime().totalMemory() + Runtime.getRuntime().freeMemory();
    }

    protected static int bytesToMB(long bytes) {
        return (int) (bytes / 1024 / 1024);
    }

    public static boolean jstack(String path) throws Exception {
        if (StringUtils.isBlank(path)) {
            return false;
        }
        File jstackFile = new File(path);
        if (!jstackFile.exists()) {
            jstackFile.createNewFile();
        }
        try (OutputStream outputStream = new FileOutputStream(jstackFile);) {
            JvmUtil.jstack(outputStream);
            outputStream.flush();
        }
        return true;
    }

    public static void jstack(OutputStream stream) throws Exception {
        ThreadMXBean threadMxBean = ManagementFactory.getThreadMXBean();
        for (ThreadInfo threadInfo : threadMxBean.dumpAllThreads(true, true)) {
            stream.write(getThreadDumpString(threadInfo).getBytes());
        }
    }

    private static String getThreadDumpString(ThreadInfo threadInfo) {
        StringBuilder sb = new StringBuilder("\"" + threadInfo.getThreadName() + "\"" +
                " Id=" + threadInfo.getThreadId() + " " +
                threadInfo.getThreadState());
        if (threadInfo.getLockName() != null) {
            sb.append(" on " + threadInfo.getLockName());
        }
        if (threadInfo.getLockOwnerName() != null) {
            sb.append(" owned by \"" + threadInfo.getLockOwnerName() +
                    "\" Id=" + threadInfo.getLockOwnerId());
        }
        if (threadInfo.isSuspended()) {
            sb.append(" (suspended)");
        }
        if (threadInfo.isInNative()) {
            sb.append(" (in native)");
        }
        sb.append('\n');
        int i = 0;

        StackTraceElement[] stackTrace = threadInfo.getStackTrace();
        MonitorInfo[] lockedMonitors = threadInfo.getLockedMonitors();
        for (; i < stackTrace.length && i < 32; i++) {
            StackTraceElement ste = stackTrace[i];
            sb.append("\tat " + ste.toString());
            sb.append('\n');
            if (i == 0 && threadInfo.getLockInfo() != null) {
                Thread.State ts = threadInfo.getThreadState();
                switch (ts) {
                    case BLOCKED:
                        sb.append("\t-  blocked on " + threadInfo.getLockInfo());
                        sb.append('\n');
                        break;
                    case WAITING:
                        sb.append("\t-  waiting on " + threadInfo.getLockInfo());
                        sb.append('\n');
                        break;
                    case TIMED_WAITING:
                        sb.append("\t-  waiting on " + threadInfo.getLockInfo());
                        sb.append('\n');
                        break;
                    default:
                }
            }

            for (MonitorInfo mi : lockedMonitors) {
                if (mi.getLockedStackDepth() == i) {
                    sb.append("\t-  locked " + mi);
                    sb.append('\n');
                }
            }
        }
        if (i < stackTrace.length) {
            sb.append("\t...");
            sb.append('\n');
        }

        LockInfo[] locks = threadInfo.getLockedSynchronizers();
        if (locks.length > 0) {
            sb.append("\n\tNumber of locked synchronizers = " + locks.length);
            sb.append('\n');
            for (LockInfo li : locks) {
                sb.append("\t- " + li);
                sb.append('\n');
            }
        }
        sb.append('\n');
        return sb.toString();
    }

    public static void main(String[] args) throws Exception {
        System.out.println("getJvmInfo=");
        System.out.println(JvmUtil.getJvmInfo());
        System.out.println();
        System.out.println("getClassPathRoot=" + JvmUtil.getClassPathRoot());
        System.out.println("getUserDir=" + JvmUtil.getUserDir());
        System.out.println("getJavaHomePath=" + JvmUtil.getJavaHomePath());

        File jstackFile = new File("./tmp/jstack-info.txt");
        if (!jstackFile.exists()) jstackFile.createNewFile();
        try (OutputStream outputStream = new FileOutputStream(jstackFile);) {
            JvmUtil.jstack(outputStream);
            outputStream.flush();
        }
    }

}
