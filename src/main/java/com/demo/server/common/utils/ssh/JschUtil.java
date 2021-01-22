/*
 * Copyright (c) 2021 demo ^-^.
 * @Author: yanbinjia@126.com
 * @LastModified: 2021-01-22T14:50:05.960+08:00
 */

package com.demo.server.common.utils.ssh;

import com.demo.server.common.utils.CharsetUtil;
import com.demo.server.common.utils.IOUtil;
import com.jcraft.jsch.*;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.util.Assert;

import java.io.*;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

@Slf4j
public class JschUtil {
    /**
     * 不使用SSH的值
     */
    public final static String SSH_NONE = "none";


    /**
     * 打开一个新的SSH会话
     *
     * @param sshHost 主机
     * @param sshPort 端口
     * @param sshUser 用户名
     * @param sshPass 密码
     * @param timeout Socket连接超时时长，单位毫秒
     * @return SSH会话
     */
    public static Session openSession(String sshHost, int sshPort, String sshUser, String sshPass, int timeout) {
        long startTime = System.currentTimeMillis();
        final Session session = createSession(sshHost, sshPort, sshUser, sshPass);
        try {
            session.connect(timeout);
        } catch (JSchException e) {
            throw new JschRuntimeException(e);
        }
        long endTime = System.currentTimeMillis();
        log.info("Connect to sftp {}@{}:{} success, cost={}ms", sshUser, sshHost, sshPort, (endTime - startTime));
        return session;
    }

    /**
     * 打开一个新的SSH会话
     *
     * @param sshHost        主机
     * @param sshPort        端口
     * @param sshUser        用户名
     * @param privateKeyPath 私钥的路径
     * @param passphrase     私钥文件的密码，可以为null
     * @return SSH会话
     */
    public static Session openSession(String sshHost, int sshPort, String sshUser, String privateKeyPath, byte[] passphrase) {
        long startTime = System.currentTimeMillis();
        final Session session = createSession(sshHost, sshPort, sshUser, privateKeyPath, passphrase);
        try {
            session.connect();
        } catch (JSchException e) {
            throw new JschRuntimeException(e);
        }
        long endTime = System.currentTimeMillis();
        log.info("Connect to sftp {}@{}:{} success, cost={}ms", sshUser, sshHost, sshPort, (endTime - startTime));
        return session;
    }

    /**
     * 新建一个新的SSH会话，此方法并不打开会话（既不调用connect方法）
     *
     * @param sshHost 主机
     * @param sshPort 端口
     * @param sshUser 用户名，如果为null，默认root
     * @param sshPass 密码
     * @return SSH会话
     */
    private static Session createSession(String sshHost, int sshPort, String sshUser, String sshPass) {
        final JSch jsch = new JSch();
        final Session session = createSession(jsch, sshHost, sshPort, sshUser);

        if (StringUtils.isNotEmpty(sshPass)) {
            session.setPassword(sshPass);
        }

        return session;
    }

    /**
     * 新建一个新的SSH会话，此方法并不打开会话（既不调用connect方法）
     *
     * @param sshHost        主机
     * @param sshPort        端口
     * @param sshUser        用户名，如果为null，默认root
     * @param privateKeyPath 私钥的路径
     * @param passphrase     私钥文件的密码，可以为null
     * @return SSH会话
     * @since 5.0.0
     */
    private static Session createSession(String sshHost, int sshPort, String sshUser, String privateKeyPath, byte[] passphrase) {

        Assert.isTrue(StringUtils.isNotBlank(privateKeyPath), "PrivateKey Path must be not empty!");

        final JSch jsch = new JSch();
        try {
            jsch.addIdentity(privateKeyPath, passphrase);
        } catch (JSchException e) {
            throw new JschRuntimeException(e);
        }

        return createSession(jsch, sshHost, sshPort, sshUser);
    }

    /**
     * 创建一个SSH会话，重用已经使用的会话
     *
     * @param jsch    {@link JSch}
     * @param sshHost 主机
     * @param sshPort 端口
     * @param sshUser 用户名，如果为null，默认root
     * @return {@link Session}
     * @since 5.0.3
     */
    private static Session createSession(JSch jsch, String sshHost, int sshPort, String sshUser) {
        Assert.isTrue(StringUtils.isNotBlank(sshHost), "SSH Host must be not empty!");
        Assert.isTrue(sshPort > 0, "SSH port must be > 0");

        // 默认root用户
        if (StringUtils.isEmpty(sshUser)) {
            sshUser = "root";
        }

        if (null == jsch) {
            jsch = new JSch();
        }

        Session session;
        try {
            session = jsch.getSession(sshUser, sshHost, sshPort);
        } catch (JSchException e) {
            throw new JschRuntimeException(e);
        }

        // 设置第一次登录的时候提示，可选值：(ask | yes | no)
        session.setConfig("StrictHostKeyChecking", "no");

        return session;
    }

    /**
     * 打开SFTP连接
     *
     * @param session Session会话
     * @return {@link ChannelSftp}
     */
    public static ChannelSftp openSftp(Session session) {
        return openSftp(session, 0);
    }

    /**
     * 打开SFTP连接
     *
     * @param session Session会话
     * @param timeout 连接超时时长，单位毫秒
     * @return {@link ChannelSftp}
     */
    public static ChannelSftp openSftp(Session session, int timeout) {
        return (ChannelSftp) openChannel(session, ChannelType.SFTP, timeout);
    }

    /**
     * 打开Shell连接
     *
     * @param session Session会话
     * @return {@link ChannelShell}
     */
    public static ChannelShell openShell(Session session) {
        return (ChannelShell) openChannel(session, ChannelType.SHELL);
    }

    /**
     * 打开Channel连接
     *
     * @param session     Session会话
     * @param channelType 通道类型，可以是shell或sftp等，见{@link ChannelType}
     * @return {@link Channel}
     */
    public static Channel openChannel(Session session, ChannelType channelType) {
        return openChannel(session, channelType, 0);
    }

    /**
     * 打开Channel连接
     *
     * @param session     Session会话
     * @param channelType 通道类型，可以是shell或sftp等，见{@link ChannelType}
     * @param timeout     连接超时时长，单位毫秒
     * @return {@link Channel}
     */
    public static Channel openChannel(Session session, ChannelType channelType, int timeout) {
        final Channel channel = createChannel(session, channelType);
        try {
            channel.connect(Math.max(timeout, 0));
        } catch (JSchException e) {
            throw new JschRuntimeException(e);
        }
        return channel;
    }

    /**
     * 创建Channel连接
     *
     * @param session     Session会话
     * @param channelType 通道类型，可以是shell或sftp等，见{@link ChannelType}
     * @return {@link Channel}
     */
    public static Channel createChannel(Session session, ChannelType channelType) {
        Channel channel;
        try {
            if (false == session.isConnected()) {
                session.connect();
            }
            channel = session.openChannel(channelType.getValue());
        } catch (JSchException e) {
            throw new JschRuntimeException(e);
        }
        return channel;
    }

    /**
     * 执行Shell命令
     *
     * @param session Session会话
     * @param cmd     命令
     * @param charset 发送和读取内容的编码
     * @return {@link ChannelExec}
     */
    public static String exec(Session session, String cmd, Charset charset) {
        return exec(session, cmd, charset, System.err);
    }

    /**
     * 执行Shell命令（使用EXEC方式）
     * <p>
     * 此方法单次发送一个命令到服务端，不读取环境变量，执行结束后自动关闭channel，不会产生阻塞。
     * </p>
     *
     * @param session   Session会话
     * @param cmd       命令
     * @param charset   发送和读取内容的编码
     * @param errStream 错误信息输出到的位置
     * @return 执行结果内容
     */
    public static String exec(Session session, String cmd, Charset charset, OutputStream errStream) {
        if (null == charset) {
            charset = CharsetUtil.CHARSET_UTF_8;
        }
        final ChannelExec channel = (ChannelExec) createChannel(session, ChannelType.EXEC);
        channel.setCommand(cmd.getBytes(StandardCharsets.UTF_8));
        channel.setInputStream(null);
        channel.setErrStream(errStream);
        InputStream in = null;
        BufferedReader result = null;
        StringBuilder resultMsg = null;

        try {
            channel.connect();
            in = channel.getInputStream();

            result = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            String s;
            while ((s = result.readLine()) != null) {
                resultMsg.append(s);
                resultMsg.append("\n");
            }

            return resultMsg.toString();
        } catch (JSchException e) {
            throw new JschRuntimeException(e);
        } catch (IOException e) {
            throw new JschRuntimeException(e);
        } finally {
            IOUtil.close(in);
            close(channel);
        }
    }

    /**
     * 执行Shell命令
     * <p>
     * 此方法单次发送一个命令到服务端，自动读取环境变量，执行结束后自动关闭channel，不会产生阻塞。
     * </p>
     *
     * @param session Session会话
     * @param cmd     命令
     * @param charset 发送和读取内容的编码
     * @return {@link ChannelExec}
     */
    public static String execByShell(Session session, String cmd, Charset charset) {
        final ChannelShell shell = openShell(session);
        // 开始连接
        shell.setPty(true);
        OutputStream out = null;
        InputStream in = null;
        BufferedReader result = null;
        StringBuilder resultMsg = null;

        try {
            out = shell.getOutputStream();
            in = shell.getInputStream();

            out.write(cmd.getBytes(StandardCharsets.UTF_8));
            out.flush();

            result = new BufferedReader(new InputStreamReader(in, StandardCharsets.UTF_8));
            String s;
            while ((s = result.readLine()) != null) {
                resultMsg.append(s);
                resultMsg.append("\n");
            }

            return resultMsg.toString();
        } catch (IOException e) {
            throw new JschRuntimeException(e);
        } finally {
            IOUtil.close(out);
            IOUtil.close(in);
            close(shell);
        }
    }

    /**
     * 关闭SSH连接会话
     *
     * @param session SSH会话
     */
    public static void close(Session session) {
        if (session != null && session.isConnected()) {
            session.disconnect();
        }
    }

    /**
     * 关闭会话通道
     *
     * @param channel 会话通道
     */
    public static void close(Channel channel) {
        if (channel != null && channel.isConnected()) {
            channel.disconnect();
        }
    }
}
