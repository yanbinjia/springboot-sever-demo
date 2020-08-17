/*
 * Copyright (c) 2020 demo ^-^.
 * @Author: yanbinjia@126.com
 * @LastModified: 2020-08-10T14:21:38.845+08:00
 */

package com.demo.server.common.utils.file;

import com.demo.server.bean.base.ResultCode;
import com.demo.server.common.utils.ImageUtil;
import com.demo.server.common.utils.RandomUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;
import org.apache.commons.lang3.time.DateFormatUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.Date;

@Slf4j
public class FileUploadUtil {
    // 文件名最大长度
    public static final int FILE_NAME_MAX_LENGTH = 100;

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

    private static void checkAllowedImage(String uploadBasePath, MultipartFile multipartFile, long maxUploadSize, String[] allowedExtension) {
        checkAllowed(uploadBasePath, multipartFile, maxUploadSize, allowedExtension);
        if (!ImageUtil.isImageByIO(multipartFile)) {
            throw new FileUploadException(ResultCode.PARAM_ERROR, "上传文件格式错误,请检查图片格式");
        }
    }

    private static void checkAllowed(String uploadBasePath, MultipartFile multipartFile, long maxUploadSize, String[] allowedExtension) {
        // 入参检查
        if (StringUtils.isBlank(uploadBasePath) || multipartFile == null || multipartFile.isEmpty() || maxUploadSize < 0) {
            throw new FileUploadException(ResultCode.PARAM_ERROR, "上传文件参数错误,请检查");
        }

        // 扩展名限制
        String extension = FilenameUtils.getExtension(multipartFile.getOriginalFilename());
        if (!isAllowedExtension(extension, allowedExtension)) {
            throw new FileUploadException(ResultCode.PARAM_ERROR, "上传文件格式错误,允许:" + String.join(",", allowedExtension));
        }

        // 文件大小限制
        if (multipartFile.getSize() > maxUploadSize) {
            throw new FileUploadException(ResultCode.PARAM_ERROR, "上传文件大小超过限制" + FileUtil.getReadableSizeStr(maxUploadSize));
        }

        // 文件名称长度限制
        if (multipartFile.getOriginalFilename().length() > FILE_NAME_MAX_LENGTH) {
            throw new FileUploadException(ResultCode.PARAM_ERROR, "上传文件名称超过长度限制" + FILE_NAME_MAX_LENGTH);
        }

        // 服务器上传路径检查
        File basePath = new File(uploadBasePath);
        if (!basePath.exists() || !basePath.isDirectory() || !basePath.canWrite()) {
            throw new FileUploadException(ResultCode.SYS_ERROR, "上传路径不存在或无写权限,请检查");
        }
    }

    private static final boolean isAllowedExtension(String extension, String[] allowedExtension) {
        if (allowedExtension == null) {
            return true;
        }
        return Arrays.stream(allowedExtension).anyMatch(s -> s.equalsIgnoreCase(extension));
    }

    private static String getDestAbsolutePath(String uploadBasePath, MultipartFile multipartFile) throws IOException {
        String clientOriginalFileName = multipartFile.getOriginalFilename();
        String extension = FilenameUtils.getExtension(clientOriginalFileName);
        return getPathStoreByDate(uploadBasePath, extension);
    }

    private static String getPathStoreByDate(String uploadBasePath, String extension) {
        uploadBasePath = uploadBasePath.endsWith(File.separator) ? uploadBasePath : uploadBasePath + File.separator;
        return uploadBasePath + datePath() + File.separator + System.currentTimeMillis() + "-" + RandomUtil.uuidWithoutSeparator() + "." + extension;
    }

    private static File getDestAbsoluteFile(String destAbsolutePath) throws IOException {
        File desc = new File(destAbsolutePath);

        if (!desc.getParentFile().exists()) {
            desc.getParentFile().mkdirs();
        }
        if (!desc.exists()) {
            desc.createNewFile();
        }

        return desc;
    }

    /**
     * 日期路径 即年/月/日 如2018/08/08
     */
    private static final String datePath() {
        return DateFormatUtils.format(new Date(), "yyyy/MM/dd");
    }
}
