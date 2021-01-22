/*
 * Copyright (c) 2021 demo ^-^.
 * @Author: yanbinjia@126.com
 * @LastModified: 2021-01-22T14:53:01.025+08:00
 */

package com.demo.server.common.utils.ssh;

public class JschRuntimeException extends RuntimeException {
    private static final long serialVersionUID = 8247610319171014183L;

    public JschRuntimeException() {
        super();
    }

    public JschRuntimeException(String message) {
        super(message);
    }

    public JschRuntimeException(String message, Throwable cause) {
        super(message, cause);
    }

    public JschRuntimeException(Throwable cause) {
        super(cause);
    }
}
