/*
 * Copyright (c) 2021 demo ^-^.
 * @Author: yanbinjia@126.com
 * @LastModified: 2021-01-22T16:19:23.287+08:00
 */

package com.demo.server.common.utils.ssh;

import com.jcraft.jsch.ChannelSftp;
import com.jcraft.jsch.Session;
import com.jcraft.jsch.SftpException;
import org.apache.commons.lang3.StringUtils;

import java.util.ArrayList;
import java.util.List;

public class Sftp {
    private Session session;
    private ChannelSftp channel;

    public Sftp(String sshHost, int sshPort, String sshUser, String sshPass, int timeout) {
        init(sshHost, sshPort, sshUser, sshPass, timeout);
    }

    public void init(String sshHost, int sshPort, String sshUser, String sshPass, int timeout) {
        this.session = JschUtil.openSession(sshHost, sshPort, sshUser, sshPass, timeout);
        this.channel = JschUtil.openSftp(this.session);
    }

    public String pwd() {
        try {
            return channel.pwd();
        } catch (SftpException e) {
            throw new JschRuntimeException(e);
        }
    }

    public String home() {
        try {
            return channel.getHome();
        } catch (SftpException e) {
            throw new JschRuntimeException(e);
        }
    }

    public boolean cd(String path) {
        if (StringUtils.isBlank(path)) {
            return true;// 当前目录
        }
        try {
            channel.cd(path.replaceAll("\\\\", "/"));
            return true;
        } catch (SftpException e) {
            return false;
        }
    }

    public boolean mkdir(String dir) {
        try {
            this.channel.mkdir(dir);
            return true;
        } catch (SftpException e) {
            throw new JschRuntimeException(e);
        }
    }

    public boolean delFile(String filePath) {
        try {
            channel.rm(filePath);
        } catch (SftpException e) {
            throw new JschRuntimeException(e);
        }
        return true;
    }

    public List<String> ls(String path) {
        final List<ChannelSftp.LsEntry> entryList = lsEntries(path);
        final List<String> list = new ArrayList<>();
        entryList.stream().forEach(s -> list.add(s.getFilename()));
        return list;
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
