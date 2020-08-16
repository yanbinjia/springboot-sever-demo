/*
 * Copyright (c) 2020 demo ^-^.
 * @Author: yanbinjia@126.com
 * @LastModified: 2020-08-10T14:21:38.845+08:00
 */

package com.demo.server.common.utils.file;

import com.demo.server.bean.base.ResultCode;
import com.demo.server.common.utils.DateUtil;
import com.demo.server.common.utils.ImageUtil;
import com.demo.server.common.utils.RandomUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

@Slf4j
public class FileUploadUtil {
    // 默认的文件名最大长度 100
    public static final int DEFAULT_FILE_NAME_LENGTH = 100;

    public static final String upload(String uploadBasePath, MultipartFile multipartFile, long maxUploadSize, String[] allowedExtension)
            throws Exception {

        checkAllowed(uploadBasePath, multipartFile, maxUploadSize, allowedExtension);

        String destAbsolutePath = getDestAbsolutePath(uploadBasePath, multipartFile);
        File destFile = getDestAbsoluteFile(destAbsolutePath);

        multipartFile.transferTo(destFile);

        log.info("upload success. src={},save to dest={}", multipartFile.getOriginalFilename(), destAbsolutePath);

        return destAbsolutePath;
    }

    public static final String uploadImage(String uploadBasePath, MultipartFile multipartFile, long maxUploadSize)
            throws Exception {
        String[] allowedExtension = ImageUtil.IMG_EXT_LIST.toArray(new String[0]);
        return uploadImage(uploadBasePath, multipartFile, maxUploadSize, allowedExtension);
    }

    public static final String uploadImage(String uploadBasePath, MultipartFile multipartFile, long maxUploadSize, String[] allowedExtension)
            throws Exception {

        checkAllowedImage(uploadBasePath, multipartFile, maxUploadSize, allowedExtension);

        String destAbsolutePath = getDestAbsolutePath(uploadBasePath, multipartFile);
        File destFile = getDestAbsoluteFile(destAbsolutePath);

        multipartFile.transferTo(destFile);

        log.info("upload success. src={},save to dest={}", multipartFile.getOriginalFilename(), destAbsolutePath);

        return destAbsolutePath;
    }

    public static void checkAllowedImage(String uploadBasePath, MultipartFile multipartFile, long maxUploadSize, String[] allowedExtension) {
        checkAllowed(uploadBasePath, multipartFile, maxUploadSize, allowedExtension);
        if (!ImageUtil.isImageByIO(multipartFile)) {
            throw new FileUploadException(ResultCode.PARAM_ERROR, "上传文件格式错误,请检查图片格式");
        }
    }

    public static void checkAllowed(String uploadBasePath, MultipartFile multipartFile, long maxUploadSize, String[] allowedExtension) {

        if (StringUtils.isBlank(uploadBasePath) || multipartFile == null || multipartFile.isEmpty() || maxUploadSize < 0) {
            throw new FileUploadException(ResultCode.PARAM_ERROR, "上传文件参数错误,请检查");
        }

        String extension = FilenameUtils.getExtension(multipartFile.getOriginalFilename());

        if (!isAllowedExtension(extension, allowedExtension)) {
            throw new FileUploadException(ResultCode.PARAM_ERROR, "上传文件格式错误,允许:" + String.join(",", allowedExtension));
        }

        if (multipartFile.getSize() > maxUploadSize) {
            // 超过限制大小
            throw new FileUploadException(ResultCode.PARAM_ERROR, "上传文件大小超过限制" + FileUtil.getReadableSizeStr(maxUploadSize));
        }

        if (multipartFile.getOriginalFilename().length() > DEFAULT_FILE_NAME_LENGTH) {
            // 文件名称超长
            throw new FileUploadException(ResultCode.PARAM_ERROR, "上传文件名称超过长度限制" + DEFAULT_FILE_NAME_LENGTH);
        }
    }

    public static final boolean isAllowedExtension(String extension, String[] allowedExtension) {
        if (allowedExtension == null) {
            return true;
        }
        return Arrays.stream(allowedExtension).anyMatch(s -> s.equalsIgnoreCase(extension));
    }

    public static String getDestAbsolutePath(String uploadBasePath, MultipartFile multipartFile) throws IOException {
        if (StringUtils.isBlank(uploadBasePath) || multipartFile == null || multipartFile.isEmpty()) {
            throw new IOException("getDestAbsolutePath error: uploadBasePath or multipartFile is blank.");
        }
        String clientOriginalFileName = multipartFile.getOriginalFilename();
        String extension = FilenameUtils.getExtension(clientOriginalFileName);

        return getPathStoreByDate(uploadBasePath, extension);
    }

    public static String getPathStoreByDate(String uploadBasePath, String extension) {
        uploadBasePath = uploadBasePath.endsWith(File.separator) ? uploadBasePath : uploadBasePath + File.separator;
        return uploadBasePath + DateUtil.datePathNow() + File.separator + System.currentTimeMillis() + "-" + RandomUtil.uuidWithoutSeparator() + "." + extension;
    }

    public static File getDestAbsoluteFile(String destAbsolutePath) throws IOException {
        if (StringUtils.isBlank(destAbsolutePath)) {
            throw new IOException("getDestAbsoluteFile error: destAbsolutePath is blank.");
        }
        File desc = new File(destAbsolutePath);

        if (!desc.getParentFile().exists()) {
            desc.getParentFile().mkdirs();
        }
        if (!desc.exists()) {
            desc.createNewFile();
        }
        return desc;

    }

}
