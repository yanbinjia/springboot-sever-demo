/*
 * Copyright (c) 2020 demo ^-^.
 * @Author: yanbinjia@126.com
 * @LastModified: 2020-08-10T11:18:04.052+08:00
 */

package com.demo.server.common.utils.file;

import lombok.extern.slf4j.Slf4j;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;

import javax.servlet.http.HttpServletRequest;
import java.io.*;
import java.net.URLEncoder;

/**
 * 文件处理工具类
 * <p>
 * org.apache.commons.io.FileUtils 封装, FileUtils的使用实例
 * 可以直接使用org.apache.commons.io.FileUtils作为文件操作工具.
 */
@Slf4j
public class FileUtil extends org.apache.commons.io.FileUtils {
    public static String FILENAME_PATTERN = "[a-zA-Z0-9_\\-\\|\\.\\u4e00-\\u9fa5]+";

    /**
     * 输出指定文件的byte数组
     *
     * @param filePath 文件路径
     * @param os       输出流
     * @return
     */
    public static void writeBytes(String filePath, OutputStream os) throws IOException {
        FileInputStream fis = null;
        try {
            File file = new File(filePath);
            if (!file.exists()) {
                throw new FileNotFoundException(filePath);
            }
            fis = new FileInputStream(file);
            byte[] b = new byte[1024];
            int length;
            while ((length = fis.read(b)) > 0) {
                os.write(b, 0, length);
            }
        } catch (IOException e) {
            log.error("writeBytes error.", e);
        } finally {
            if (os != null) {
                try {
                    os.close();
                } catch (IOException e1) {
                    log.error("writeBytes error.", e1);
                }
            }
            if (fis != null) {
                try {
                    fis.close();
                } catch (IOException e1) {
                    log.error("writeBytes error.", e1);
                }
            }
        }
    }

    public static String getExtName(String filename) {
        return FilenameUtils.getExtension(filename);
    }

    public static boolean deleteFile(String filePath) {
        boolean flag = false;
        File file = new File(filePath);
        // 路径为文件且不为空则进行删除
        if (file.isFile() && file.exists()) {
            file.delete();
            flag = true;
        }
        return flag;
    }

    public static boolean cleanDirectory(String path) {
        File file = new File(path);
        if (!file.exists() || !file.isDirectory()) {
            log.error("cleanDirectory error. path={} is not exists or not directory", path);
            return false;
        }
        try {
            FileUtils.cleanDirectory(file);
            return true;
        } catch (IOException e) {
            log.error("cleanDirectory error. path={}", path, e);
        }
        return false;
    }

    /**
     * 文件名称验证
     *
     * @param filename 文件名称
     * @return true 正常 false 非法
     */
    public static boolean isValidFilename(String filename) {
        return filename.matches(FILENAME_PATTERN);
    }

    /**
     * 下载文件名重新编码
     *
     * @param request  请求对象
     * @param fileName 文件名
     * @return 编码后的文件名
     */
    public static String setFileDownloadHeader(HttpServletRequest request, String fileName)
            throws UnsupportedEncodingException {
        final String agent = request.getHeader("USER-AGENT");
        String filename = fileName;
        if (agent.contains("MSIE")) {
            // IE浏览器
            filename = URLEncoder.encode(filename, "utf-8");
            filename = filename.replace("+", " ");
        } else if (agent.contains("Firefox")) {
            // 火狐浏览器
            filename = new String(fileName.getBytes(), "ISO8859-1");
        } else if (agent.contains("Chrome")) {
            // google浏览器
            filename = URLEncoder.encode(filename, "utf-8");
        } else {
            // 其它浏览器
            filename = URLEncoder.encode(filename, "utf-8");
        }
        return filename;
    }
}
