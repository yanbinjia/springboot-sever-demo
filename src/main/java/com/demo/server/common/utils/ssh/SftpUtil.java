/*
 * Copyright (c) 2021 demo ^-^.
 * @Author: yanbinjia@126.com
 * @LastModified: 2021-01-22T16:19:23.287+08:00
 */

package com.demo.server.common.utils.ssh;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpATTRS;
import com.jcraft.jsch.SftpException;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.util.ArrayList;
import java.util.List;

public class SftpUtil {
    private static final Logger log = LoggerFactory.getLogger(SftpUtil.class);
    private Session session;
    private ChannelSftp channel;

    public SftpUtil(String host, int port, String user, String password, int timeout) {
        init(host, port, user, password, timeout);
    }

    private void init(String host, int port, String user, String password, int timeout) {
        this.session = JschUtil.openSession(host, port, user, password, timeout);
        this.channel = JschUtil.openSftp(this.session);
    }

    public String pwd() {
        try {
            log.info("exec cmd: pwd");
            return channel.pwd();
        } catch (SftpException e) {
            throw new JschRuntimeException(e);
        }
    }

    public String home() {
        try {
            log.info("exec cmd: home");
            return channel.getHome();
        } catch (SftpException e) {
            throw new JschRuntimeException(e);
        }
    }

    public boolean cd(String path) {
        log.info("exec cmd: cd {}", path);
        if (StringUtils.isBlank(path)) {
            return true;// 当前目录
        }
        try {
            channel.cd(path);
            return true;
        } catch (SftpException e) {
            return false;
        }
    }

    public boolean mkdir(String dir) {
        log.info("exec cmd: mkdir {}", dir);
        try {
            this.channel.mkdir(dir);
            return true;
        } catch (SftpException e) {
            throw new JschRuntimeException(e);
        }
    }

    public boolean delFile(String filePath) {
        log.info("exec cmd: rm {}", filePath);
        try {
            channel.rm(filePath);
        } catch (SftpException e) {
            throw new JschRuntimeException(e);
        }
        return true;
    }

    public List<String> ls(String path) {
        log.info("exec cmd: ls {}", path);
        final List<ChannelSftp.LsEntry> entryList = lsEntries(path);
        final List<String> list = new ArrayList<>();
        entryList.stream().forEach(s -> list.add(s.getFilename()));
        return list;
    }

    public void upload(String ftpPath, String localFilePath) {
        try {
            long startTime = System.currentTimeMillis();
            log.info("exec upload, localFilePath={} to ftpPath={}", localFilePath, ftpPath);
            channel.cd(ftpPath);
            File file = new File(localFilePath);
            channel.put(new FileInputStream(file), file.getName());
            log.info("exec upload success ,cost={}", System.currentTimeMillis() - startTime);
        } catch (Exception e) {
            log.error("exec upload error, localFilePath={} to ftpPath={}", localFilePath, ftpPath, e);
            throw new JschRuntimeException(e);
        }
    }

    public void download(String ftpPath, String ftpFile, String localFilePath) {
        try {
            long startTime = System.currentTimeMillis();
            log.info("exec download, ftpPath={}, ftpFile={} to localFilePath={}", ftpPath, ftpFile, localFilePath);
            channel.cd(ftpPath);
            File file = new File(localFilePath);
            channel.get(ftpFile, new FileOutputStream(file));
            log.info("exec download success ,cost={}", System.currentTimeMillis() - startTime);
        } catch (Exception e) {
            log.error("exec download error, ftpPath={}, ftpFile={} to localFilePath={}", ftpPath, ftpFile, localFilePath, e);
            throw new JschRuntimeException(e);
        }
    }

    public boolean isDirExist(String ftpPath) {
        boolean isDirExistFlag = false;
        try {
            SftpATTRS sftpATTRS = channel.lstat(ftpPath);
            isDirExistFlag = true;
            return sftpATTRS.isDir();
        } catch (Exception e) {
            if (e.getMessage().toLowerCase().equals("no such file")) {
                isDirExistFlag = false;
            }
        }
        return isDirExistFlag;
    }

    /**
     * 遍历某个目录下所有文件或目录，生成LsEntry列表，不会递归遍历<br>
     * 此方法自动过滤"."和".."两种目录
     *
     * @param path 遍历某个目录下所有文件或目录
     * @return 目录或文件名列表
     */
    public List<ChannelSftp.LsEntry> lsEntries(String path) {
        final List<ChannelSftp.LsEntry> entryList = new ArrayList<>();
        try {
            channel.ls(path, entry -> {
                final String fileName = entry.getFilename();
                if (false == StringUtils.equals(".", fileName) && false == StringUtils.equals("..", fileName)) {
                    entryList.add(entry);
                }
                return ChannelSftp.LsEntrySelector.CONTINUE;
            });
        } catch (SftpException e) {
            if (false == e.getMessage().startsWith("No such file")) {
                throw new JschRuntimeException(e);
            }
        }
        return entryList;
    }

    public void close() {
        JschUtil.close(this.channel);
        JschUtil.close(this.session);
    }

}
