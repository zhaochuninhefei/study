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

        // 直接埋入字符串
        String firstName = "Chun";
        String lastName  = "Zhao";
        String fullName  = STR."\{firstName} \{lastName}";
        System.out.println(fullName);

        String sortName  = STR."\{lastName}, \{firstName}";
        System.out.println(sortName);

        // 埋入表达式,运行时会执行表达式进行计算 如这里的 x + y
        int x = 10, y = 20;
        String s = STR."\{x} + \{y} = \{x + y}";
        System.out.println(s);


//        // Embedded expressions can invoke methods and access fields
//        String s = STR."You have a \{getOfferType()} waiting for you!";
//
//        String t = STR."Access at \{req.date} \{req.time} from \{req.ipAddress}";

    }
}
