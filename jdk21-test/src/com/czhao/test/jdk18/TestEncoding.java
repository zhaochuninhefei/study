package com.czhao.test.jdk18;

import java.io.Console;
import java.nio.charset.Charset;

/**
 * @author zhaochun
 */
public class TestEncoding {
    public static void main(String[] args) {
        System.out.println("encodings in JDK21:");

        System.out.println("Charset.defaultCharset(): " + Charset.defaultCharset().name());
        System.out.println("System.out.charset(): " + System.out.charset().name());

        System.out.println("file.encoding: " + System.getProperty("file.encoding"));
        System.out.println("native.encoding: " + System.getProperty("native.encoding"));
    }
}
