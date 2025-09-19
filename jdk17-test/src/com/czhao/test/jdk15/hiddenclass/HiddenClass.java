package com.czhao.test.jdk15.hiddenclass;

/**
 * JEP 371: Hidden Classes
 *
 * @author zhaochun
 */
public class HiddenClass {
    public String sayHello(String name) {
        return "Hello, " + name;
    }

    public static void printHello(String name) {
        System.out.printf("""
                Hello, %s !
                Hello, HiddenClass !
                %n""", name);
    }
}
