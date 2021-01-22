/*
 * Copyright (c) 2021 demo ^-^.
 * @Author: yanbinjia@126.com
 * @LastModified: 2021-01-22T15:11:36.579+08:00
 */

package com.demo.server.common.utils.ssh;

public enum ChannelType {
    /**
     * Session
     */
    SESSION("session"),
    /**
     * shell
     */
    SHELL("shell"),
    /**
     * exec
     */
    EXEC("exec"),
    /**
     * x11
     */
    X11("x11"),
    /**
     * agent forwarding
     */
    AGENT_FORWARDING("auth-agent@openssh.com"),
    /**
     * direct tcpip
     */
    DIRECT_TCPIP("direct-tcpip"),
    /**
     * forwarded tcpip
     */
    FORWARDED_TCPIP("forwarded-tcpip"),
    /**
     * sftp
     */
    SFTP("sftp"),
    /**
     * subsystem
     */
    SUBSYSTEM("subsystem");

    /**
     * channel值
     */
    private final String value;

    /**
     * 构造
     *
     * @param value 类型值
     */
    ChannelType(String value) {
        this.value = value;
    }

    /**
     * 获取值
     *
     * @return 值
     */
    public String getValue() {
        return this.value;
    }
}