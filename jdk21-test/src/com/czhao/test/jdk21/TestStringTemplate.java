package com.czhao.test.jdk21;

import static java.lang.StringTemplate.STR;

/**
 * @author zhaochun
 */
public class TestStringTemplate {
    public static void main(String[] args) {
        TestStringTemplate me = new TestStringTemplate();
        me.test01();
    }

    private void test01() {
        var name = "zhaochun";
        // 要使用 StringTemplate, 请将工程的java编译level调整为 21 的 Preview 预览版本
        var message = STR."Welcome to use String Template, \{name} !";
        System.out.println(message);

        // Embedded expressions can be strings
        String firstName = "Chun";
        String lastName  = "Zhao";
        String fullName  = STR."\{firstName} \{lastName}";
        System.out.println(fullName);

        String sortName  = STR."\{lastName}, \{firstName}";
        System.out.println(sortName);

//        // Embedded expressions can perform arithmetic
//        int x = 10, y = 20;
//        String s = STR."\{x} + \{y} = \{x + y}";
//
//
//        // Embedded expressions can invoke methods and access fields
//        String s = STR."You have a \{getOfferType()} waiting for you!";
//
//        String t = STR."Access at \{req.date} \{req.time} from \{req.ipAddress}";

    }
}
