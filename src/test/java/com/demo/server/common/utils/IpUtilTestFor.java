package com.demo.server.common.utils;

import lombok.extern.slf4j.Slf4j;
import org.junit.Test;

import java.util.Arrays;

@Slf4j
public class IpUtilTestFor {

    @Test
    public void testForUse() throws Exception {
        String cidr = "10.255.32.0/24";
        Arrays.stream(IpUtil.cidrToIpRange(cidr)).forEach(s -> System.out.println(s));
    }

} 
