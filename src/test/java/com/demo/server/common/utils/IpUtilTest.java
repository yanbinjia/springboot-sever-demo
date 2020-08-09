package com.demo.server.common.utils;

import org.junit.After;
import org.junit.Assert;
import org.junit.Before;
import org.junit.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mock.web.MockHttpServletRequest;

import java.net.InetAddress;

/**
 * IpUtil Tester.
 *
 * @author yanbinjia@126.com
 * @version 1.0
 * @since <pre>8月 9, 2020</pre>
 */
public class IpUtilTest {
    private static final Logger logger = LoggerFactory.getLogger(IpUtilTest.class);

    @Before
    public void before() throws Exception {
    }

    @After
    public void after() throws Exception {
    }

    /**
     * Method: getClientIp(HttpServletRequest request)
     */
    @Test
    public void testGetClientIp() throws Exception {
        MockHttpServletRequest request = new MockHttpServletRequest();
        String clientIp = "10.34.23.12";
        String proxyIp = "10.34.23.22,10.34.23.19,10.34.23.100";
        request.addHeader("X-Forwarded-For", clientIp + "," + proxyIp);
        Assert.assertEquals(clientIp, IpUtil.getClientIp(request));

        request = new MockHttpServletRequest();
        clientIp = "10.34.23.12";
        request.addHeader("NS-Client-IP", clientIp);
        Assert.assertEquals(clientIp, IpUtil.getClientIp(request));
    }

    /**
     * Method: getLocalIp()
     */
    @Test
    public void testGetLocalIp() throws Exception {
        logger.info(IpUtil.getLocalIp());
        Assert.assertNotNull(IpUtil.getLocalIp());
    }

    /**
     * Method: getLocalHostName()
     */
    @Test
    public void testGetLocalHostName() throws Exception {
        logger.info(IpUtil.getLocalIp());
        Assert.assertNotNull(IpUtil.getLocalIp());
    }

    /**
     * Method: isLocalCacheValid()
     */
    @Test
    public void testIsLocalCacheValid() throws Exception {
        logger.info("isLocalCacheValid=" + IpUtil.isLocalCacheValid());
    }

    /**
     * Method: setLocalCache(InetAddress inetAddress)
     */
    @Test
    public void testSetLocalCache() throws Exception {
        IpUtil.setLocalCache(InetAddress.getLocalHost());
    }

    /**
     * Method: longToIpv4(long longIp)
     */
    @Test
    public void testLongToIpv4() throws Exception {
        String localIpStr = IpUtil.getLocalIp();
        long localIpLong = IpUtil.ipv4ToLong(localIpStr);
        logger.info("localIpStr:" + localIpStr);
        logger.info("localIpLong:" + localIpLong);
        logger.info("localIpBinary:" + Long.toBinaryString(localIpLong));
        logger.info("localIpLong from long:" + IpUtil.longToIpv4(localIpLong));
        Assert.assertEquals(localIpStr, IpUtil.longToIpv4(localIpLong));
    }

    /**
     * Method: ipv4ToLong(String strIp)
     */
    @Test
    public void testIpv4ToLong() throws Exception {
        String localIpStr = IpUtil.getLocalIp();
        long localIpLong = IpUtil.ipv4ToLong(localIpStr);
        logger.info("localIpStr:" + localIpStr);
        logger.info("localIpLong:" + localIpLong);
        logger.info("localIpBinary:" + Long.toBinaryString(localIpLong));
        logger.info("localIpLong from long:" + IpUtil.longToIpv4(localIpLong));
        Assert.assertEquals(localIpStr, IpUtil.longToIpv4(localIpLong));
    }


    /**
     * Method: isIpv4(String ip)
     */
    @Test
    public void testIsIpv4() throws Exception {
        Assert.assertFalse(IpUtil.isIpv4("99.xy.ss"));
        Assert.assertFalse(IpUtil.isIpv4("88899990000.xy.ss"));
        Assert.assertTrue(IpUtil.isIpv4(IpUtil.getLocalIp()));
    }

    /**
     * Method: isIpv6(String ip)
     */
    @Test
    public void testIsIpv6() throws Exception {
        String ipv6 = "2001:0db8:3c4d:0015:0000:0000:1a2f:1a2b";
        Assert.assertFalse(IpUtil.isIpv6("99.xy.ss"));
        Assert.assertFalse(IpUtil.isIpv6("88899990000.xy.ss"));
        Assert.assertTrue(IpUtil.isIpv6(ipv6));

        ipv6 = "0:0:0:0:0:0:0:1";
        Assert.assertTrue(IpUtil.isIpv6(ipv6));
    }

    /**
     * Method: isInRange(String ip, String cidr)
     */
    @Test
    public void testIsInRange() {
        String cidr = "128.14.35.7/20";
        Assert.assertTrue(IpUtil.isInRange("128.14.33.10", cidr));
        Assert.assertTrue(IpUtil.isInRange("128.14.32.10", cidr));
        Assert.assertFalse(IpUtil.isInRange("128.14.31.11", cidr));
        Assert.assertFalse(IpUtil.isInRange("128.14.48.11", cidr));

    }

    /**
     * Method: cidrToIpRange(String cidr)
     */
    @Test
    public void testCidrToIpRange() throws Exception {
        String cidr = "128.14.35.7/20";
        String[] ipv4range = {"128.14.32.0", "128.14.47.255"};
        Assert.assertArrayEquals(ipv4range, IpUtil.cidrToIpRange(cidr));
    }

    /**
     * Method: isInnerIp(String ip)
     */
    @Test
    public void testIsInnerIp() throws Exception {
        Assert.assertTrue(IpUtil.isInnerIp(IpUtil.LOCALHOST));
        Assert.assertTrue(IpUtil.isInnerIp("192.168.12.12"));
        Assert.assertTrue(IpUtil.isInnerIp("172.16.12.12"));
        Assert.assertTrue(IpUtil.isInnerIp("10.255.23.23"));
        Assert.assertFalse(IpUtil.isInnerIp("11.12.1.2"));
        Assert.assertFalse(IpUtil.isInnerIp("100.12.223.123"));
    }

    /**
     * Method: ping(String ip)
     */
    @Test
    public void testPingIp() throws Exception {
        Assert.assertTrue(IpUtil.ping(IpUtil.getLocalIp()));
    }

    /**
     * Method: ping(String ip, int timeout)
     */
    @Test
    public void testPingForIpTimeout() throws Exception {
        Assert.assertTrue(IpUtil.ping(IpUtil.getLocalIp(), 200));
    }

} 
