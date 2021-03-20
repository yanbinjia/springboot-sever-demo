/*
 * Copyright (c) 2021 demo ^-^.
 * @Author: yanbinjia@126.com
 * @LastModified: 2021-01-22T16:04:39.863+08:00
 */

package com.demo.server.common.utils;

import java.io.Closeable;

public class IOUtil {

    /**
     * 关闭<br>
     * 关闭失败不会抛出异常
     *
     * @param closeable 被关闭的对象
     */
    public static void close(Closeable closeable) {
        if (null != closeable) {
            try {
                closeable.close();
            } catch (Exception e) {
                // 静默关闭
            }
        }
    }
}
