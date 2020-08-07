/*
 * Copyright (c) 2020 demo ^-^.
 * @Author: yanbinjia@126.com
 * @LastModified: 2020-07-29T10:34:30.857+08:00
 */

package com.demo.server.common.utils;

import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import javax.servlet.http.HttpServletRequest;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.regex.Pattern;

public class IpUtil {
    private static final Logger logger = LoggerFactory.getLogger(IpUtil.class);
    // LOCAL_IP & LOCAL_HOST Cache
    public static String LOCAL_IP = "";
    public static String LOCAL_HOST = "";
    public static long LOCAL_TIME = 0;
    public static long LOCAL_EXPIRE = 1000 * 60 * 10;// 缓存有效期10分钟
    // IP v4 Pattern
    public final static Pattern IPV4 = Pattern.compile("\\b((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\.((?!\\d\\d\\d)\\d+|1\\d\\d|2[0-4]\\d|25[0-5])\\b");
    // IP v6 Pattern
    public final static Pattern IPV6 = Pattern.compile("(([0-9a-fA-F]{1,4}:){7}[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,7}:|([0-9a-fA-F]{1,4}:){1,6}:[0-9a-fA-F]{1,4}|([0-9a-fA-F]{1,4}:){1,5}(:[0-9a-fA-F]{1,4}){1,2}|([0-9a-fA-F]{1,4}:){1,4}(:[0-9a-fA-F]{1,4}){1,3}|([0-9a-fA-F]{1,4}:){1,3}(:[0-9a-fA-F]{1,4}){1,4}|([0-9a-fA-F]{1,4}:){1,2}(:[0-9a-fA-F]{1,4}){1,5}|[0-9a-fA-F]{1,4}:((:[0-9a-fA-F]{1,4}){1,6})|:((:[0-9a-fA-F]{1,4}){1,7}|:)|fe80:(:[0-9a-fA-F]{0,4}){0,4}%[0-9a-zA-Z]+|::(ffff(:0{1,4})?:)?((25[0-5]|(2[0-4]|1?[0-9])?[0-9])\\.){3}(25[0-5]|(2[0-4]|1?[0-9])?[0-9])|([0-9a-fA-F]{1,4}:){1,4}:((25[0-5]|(2[0-4]|1?[0-9])?[0-9])\\.){3}(25[0-5]|(2[0-4]|1?[0-9])?[0-9]))");

    public static String getClientIp(HttpServletRequest request) {
        if (request == null) {
            return "unknown";
        }
        String ip = request.getHeader("x-forwarded-for");
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Forwarded-For");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("WL-Proxy-Client-IP");
        }
        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getHeader("X-Real-IP");
        }

        if (ip == null || ip.length() == 0 || "unknown".equalsIgnoreCase(ip)) {
            ip = request.getRemoteAddr();
        }

        return "0:0:0:0:0:0:0:1".equals(ip) ? "127.0.0.1" : ip;
    }

    public static String getLocalIp() {
        if (isLocalCacheValid()) {
            logger.debug("getLocalIp by cache LOCAL_IP={} ", LOCAL_IP);
            return LOCAL_IP;
        }
        try {
            setLocalCache(InetAddress.getLocalHost());
            return LOCAL_IP;
        } catch (UnknownHostException e) {
            logger.error("getLocalIp error.", e);
        }
        return "127.0.0.1";
    }

    public static String getLocalHost() {
        if (isLocalCacheValid()) {
            logger.debug("getLocalIp by cache LOCAL_HOST={} ", LOCAL_HOST);
            return LOCAL_HOST;
        }
        try {
            setLocalCache(InetAddress.getLocalHost());
            return LOCAL_HOST;
        } catch (UnknownHostException e) {
            logger.error("getLocalHostName error.", e);
        }
        return "unknown";
    }

    public static boolean isLocalCacheValid() {
        return StringUtils.isNoneBlank(LOCAL_IP, LOCAL_HOST) && (System.currentTimeMillis() - LOCAL_TIME) < LOCAL_EXPIRE;
    }

    public static void setLocalCache(InetAddress inetAddress) {
        LOCAL_IP = inetAddress.getHostAddress();
        LOCAL_HOST = inetAddress.getHostName();
        LOCAL_TIME = System.currentTimeMillis();
        logger.debug("setLocalCache LOCAL_IP={},LOCAL_HOST={},EXPIRE={}min", LOCAL_IP, LOCAL_HOST, LOCAL_EXPIRE / 1000 / 60);
    }

    /**
     * 根据long值获取ip v4地址
     *
     * @param longIP IP的long表示形式
     * @return IP V4 地址
     */
    public static String longToIpv4(long longIP) {
        final StringBuilder sb = new StringBuilder();
        // 直接右移24位
        sb.append((longIP >>> 24));
        sb.append(".");
        // 将高8位置0，然后右移16位
        sb.append(((longIP & 0x00FFFFFF) >>> 16));
        sb.append(".");
        sb.append(((longIP & 0x0000FFFF) >>> 8));
        sb.append(".");
        sb.append((longIP & 0x000000FF));
        return sb.toString();
    }

    /**
     * 根据ip地址计算出long型的数据
     *
     * @param strIP IP V4 地址
     * @return long值
     */
    public static long ipv4ToLong(String strIP) {
        if (isIpv4(strIP)) {
            long[] ip = new long[4];
            // 先找到IP地址字符串中.的位置
            int position1 = strIP.indexOf(".");
            int position2 = strIP.indexOf(".", position1 + 1);
            int position3 = strIP.indexOf(".", position2 + 1);
            // 将每个.之间的字符串转换成整型
            ip[0] = Long.parseLong(strIP.substring(0, position1));
            ip[1] = Long.parseLong(strIP.substring(position1 + 1, position2));
            ip[2] = Long.parseLong(strIP.substring(position2 + 1, position3));
            ip[3] = Long.parseLong(strIP.substring(position3 + 1));
            return (ip[0] << 24) + (ip[1] << 16) + (ip[2] << 8) + ip[3];
        }
        return 0;
    }

    /*
     * ipv4共32位,点分十进制字符串长15.
     */
    public static final int IPV4_LEN = 15;

    public static boolean isIpv4(String ip) {
        if (StringUtils.isBlank(ip) || ip.length() < 7 || ip.length() > IPV4_LEN) {
            return false;
        }
        return IPV4.matcher(ip).matches();
    }

    /*
     * ipv6共128位,冒号分16进制长度为39,IPv6 地址的大小和格式使得寻址功能大为增强. support ipv4 on ipv6 模式下长度为46.
     * 地址表示法为 x:x:x:x:x:x:x:x, 其中每个 x 是地址的 8 个 16 位部分的十六进制值.
     * ipv6地址范围从 0000:0000:0000:0000:0000:0000:0000:0000 至 ffff:ffff:ffff:ffff:ffff:ffff:ffff:ffff
     */
    public static final int IPV6_LEN = 46;

    public static boolean isIpv6(String ip) {
        if (StringUtils.isBlank(ip) || ip.length() < 15 || ip.length() > IPV6_LEN) {
            return false;
        }
        return IPV6.matcher(ip).matches();
    }


    public static boolean ping(String ip) {
        return ping(ip, 200);
    }

    public static boolean ping(String ip, int timeout) {
        try {
            // timeout 检测超时（毫秒）
            return InetAddress.getByName(ip).isReachable(timeout); // 当返回值是true时，说明host是可用的，false则不可。
        } catch (Exception ex) {
            return false;
        }
    }

    public static void main(String[] args) {
        String ipv6 = "2001:0db8:3c4d:0015:0000:0000:1a2f:1a2b";
        System.out.println(ipv6 + ":" + IpUtil.isIpv6(ipv6));
        ipv6 = "0:0:0:0:0:0:0:1";
        System.out.println(ipv6 + ":" + IpUtil.isIpv6(ipv6));

        String localIpStr = IpUtil.getLocalIp();
        long localIpLong = IpUtil.ipv4ToLong(localIpStr);
        System.out.println("localIpStr:" + localIpStr);
        System.out.println("localIpLong:" + localIpLong);
        System.out.println("localIpLong from long:" + IpUtil.longToIpv4(localIpLong));

        String ipv4 = "192.168.31.111";
        System.out.println(ipv4 + " pingok=" + IpUtil.ping(ipv4));
        ipv4 = "192.168.31.1";
        System.out.println(ipv4 + " pingok=" + IpUtil.ping(ipv4));
        System.out.println(IpUtil.getLocalIp() + " pingok=" + IpUtil.ping(IpUtil.getLocalIp()));

        System.out.println(IpUtil.getLocalHost());
    }
}
