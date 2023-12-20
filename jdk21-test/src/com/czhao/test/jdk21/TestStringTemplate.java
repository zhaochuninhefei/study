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
    }
}
