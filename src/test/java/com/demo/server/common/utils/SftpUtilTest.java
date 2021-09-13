package com.demo.server.common.utils;

import com.demo.server.common.utils.ssh.SftpUtil;
import org.junit.Test;

import java.util.ArrayList;
import java.util.List;

public class SftpUtilTest {
    public String host = "10.255.33.18";
    public int port = 3333;
    public String user = "pppp";
    public String password = "magic_sharp_868@pppp";
    public int timeout = 1000 * 3;

    @Test
    public void testSftp() throws Exception {
        SftpUtil sftpUtil = new SftpUtil(host, port, user, password, timeout);
        System.out.println("home:" + sftpUtil.home());
        System.out.println("pwd:" + sftpUtil.pwd());
        List s = new ArrayList();
    }
}
