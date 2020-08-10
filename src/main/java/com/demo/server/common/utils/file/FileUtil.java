/*
 * Copyright (c) 2020 demo ^-^.
 * @Author: yanbinjia@126.com
 * @LastModified: 2020-08-10T11:18:04.052+08:00
 */

package com.demo.server.common.utils.file;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang3.StringUtils;

import java.io.File;
import java.io.IOException;

/**
 * 文件处理工具类
 * <p>
 * org.apache.commons.io.FileUtils 封装, FileUtils的使用实例
 * 可以直接使用org.apache.commons.io.FileUtils作为文件操作工具.
 */
@Slf4j
public class FileUtil extends org.apache.commons.io.FileUtils {
    public static final String FILENAME_PATTERN = "[a-zA-Z0-9_\\-\\|\\.\\u4e00-\\u9fa5]+";

    public static String getExtName(String filename) {
        return FilenameUtils.getExtension(filename);
    }

    public static void moveFile(File srcFile, File destFile) throws IOException {
        FileUtils.moveFile(srcFile, destFile);
        log.info("moveFile ok. src={},dest={}", srcFile.getAbsolutePath(), destFile.getAbsolutePath());
    }

    public static boolean deleteFile(String path) {
        if (StringUtils.isBlank(path)) {
            log.error("deleteFile error. filePath can not blank.");
            return false;
        }
        boolean flag = false;
        File file = new File(path);
        if (file.isFile() && file.exists()) {
            file.delete();
            flag = true;
            log.info("deleteFile ok. path={}", path);
        }
        return flag;
    }

    public static boolean cleanDirectory(String path) {
        if (StringUtils.isBlank(path)) {
            log.error("deleteFile error. path can not blank.");
            return false;
        }
        File file = new File(path);
        if (!file.exists() || !file.isDirectory()) {
            log.error("cleanDirectory error. path={} is not exists or not directory", path);
            return false;
        }
        try {
            FileUtils.cleanDirectory(file);
            log.warn("cleanDirectory ok. path={} is not exists or not directory", path);
            return true;
        } catch (IOException e) {
            log.error("cleanDirectory error. path={}", path, e);
        }
        return false;
    }

    public static boolean isValidFilename(String filename) {
        return filename.matches(FILENAME_PATTERN);
    }
}
