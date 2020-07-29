/*
 * Copyright (c) 2020 demo ^-^.
 * @Author: yanbinjia@126.com
 * @LastModified: 2020-07-29T10:34:30.857+08:00
 */

package com.demo.server.common.util;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class IpUtil {
    public static String getLocalIp() throws UnknownHostException {
        return InetAddress.getLocalHost().getHostAddress();
    }

    public static void main(String[] args) throws UnknownHostException {
        System.out.println(IpUtil.getLocalIp());
    }
}
