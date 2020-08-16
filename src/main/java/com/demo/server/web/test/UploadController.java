/*
 * Copyright (c) 2020 demo ^-^.
 * @Author: yanbinjia@126.com
 * @LastModified: 2020-08-13T17:39:49.233+08:00
 */

package com.demo.server.web.test;

import com.demo.server.bean.base.Result;
import com.demo.server.bean.base.ResultCode;
import com.demo.server.common.interceptor.SignPass;
import com.demo.server.common.interceptor.TokenPass;
import com.demo.server.common.utils.file.FileUploadUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotEmpty;

@RestController
@RequestMapping("/test/upload")
@Slf4j
public class UploadController {
    @PostMapping("/test")
    @ResponseBody
    @TokenPass
    @SignPass
    public Result<String> upload(@RequestParam("file") @NotEmpty(message = "不能为空") MultipartFile file) throws Exception {
        String uploadBasePath = "/Users/ucredit/upload_data";
        long maxUploadSize = 1 * 1024 * 1024;
        String path = FileUploadUtil.upload(uploadBasePath, file, maxUploadSize, new String[]{"png", "jpg", "jpeg", "icon"});
        Result<String> result = new Result<String>(ResultCode.SUCCESS.code, "上传成功");
        result.setData(path);
        return result;
    }

    @PostMapping("/image")
    @ResponseBody
    @TokenPass
    @SignPass
    public Result<String> uploadImage(@RequestParam("file") @NotEmpty(message = "不能为空") MultipartFile file) throws Exception {
        String uploadBasePath = "/Users/ucredit/upload_data";
        long maxUploadSize = 1 * 1024 * 1024;
        String path = FileUploadUtil.uploadImage(uploadBasePath, file, maxUploadSize, new String[]{"png", "jpg", "jpeg"});
        Result<String> result = new Result<String>(ResultCode.SUCCESS.code, "上传成功");
        result.setData(path);
        return result;
    }

}
