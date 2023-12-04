package com.czhao.test.jdk18;

import java.net.InetAddress;
import java.net.UnknownHostException;

/**
 * @author zhaochun
 */
public class TestInetAddress {
    public static void main(String[] args) {
        TestInetAddress me = new TestInetAddress();
        me.test01();
    }

    private void test01() {
        var addressBytes = new byte[] { 127, 0, 0, 1 };
        String resolveHostName;
        try {
            resolveHostName = InetAddress.getByAddress(addressBytes)
                    .getCanonicalHostName();
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
        System.out.println(resolveHostName);
    }
}
