/*
 * Copyright (c) 2020 demo ^-^.
 * @Author: yanbinjia@126.com
 * @LastModified: 2020-08-14T11:00:57.280+08:00
 */

package com.demo.server.common.utils.file;

import com.demo.server.bean.base.ResultCode;
import com.demo.server.common.exception.AppException;

public class FileUploadException extends AppException {

    public FileUploadException(int code, String msg) {
        super(code, msg);
    }

    public FileUploadException(ResultCode resultCode) {
        super(resultCode);
    }

    public FileUploadException(ResultCode resultCode, String extMsg) {
        super(resultCode, extMsg);
    }

    public FileUploadException(String msg) {
        super(msg);
    }
}
