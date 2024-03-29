package com.czhao.test.jdk18;

import java.io.IOException;
import java.net.*;
import java.util.Arrays;

/**
 * @author zhaochun
 */
public class TestInetAddress {
    public static void main(String[] args) {
        TestInetAddress me = new TestInetAddress();
        me.test01();
        me.test02();
    }

    // InetAddress 功能演示
    // InetAddress 不是 java18新特性，一直就有
    private void test01() {
        // 根据 IP 查找 hostname
        var addressBytes = new byte[] { 127, 0, 0, 1 };
        String resolveHostName;
        try {
            resolveHostName = InetAddress.getByAddress(addressBytes)
                    .getCanonicalHostName();
            System.out.println(resolveHostName);
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }

        // 根据 hostname 查找 IP
        String hostname = "localhost";
        try {
            InetAddress address = InetAddress.getByName(hostname);
            System.out.println(Arrays.toString(address.getAddress()));

            InetAddress[] addresses = InetAddress.getAllByName(hostname);
            for (InetAddress ads : addresses) {
                System.out.println(Arrays.toString(ads.getAddress()));
            }
        } catch (UnknownHostException e) {
            throw new RuntimeException(e);
        }
    }

    // 比较 注册 TestInetAddressResolverProvider 前后的区别
    private void test02() {
        try (var stream = new URI("https://justtest.com:8080").toURL().openStream()) {
            stream.transferTo(System.out);
        } catch (IOException | URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
