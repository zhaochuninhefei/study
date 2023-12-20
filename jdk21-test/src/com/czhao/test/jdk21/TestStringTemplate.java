package com.czhao.test.jdk21;

import java.io.File;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;

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
        var message = STR. "Welcome to use String Template, \{ name } !" ;
        System.out.println(message);

        // 直接埋入字符串
        String firstName = "Chun";
        String lastName = "Zhao";
        String fullName = STR. "\{ firstName } \{ lastName }" ;
        System.out.println(fullName);

        String sortName = STR. "\{ lastName }, \{ firstName }" ;
        System.out.println(sortName);

        // 埋入表达式,运行时会执行表达式进行计算 如这里的 x + y
        int x = 10, y = 20;
        String s = STR. "\{ x } + \{ y } = \{ x + y }" ;
        System.out.println(s);

        // 埋入方法，可访问的字段
        Poetry p = new Poetry("李白", "少年行");
        String line1 = STR. "少年负\{ getSomething() }，奋烈自有时。" ;
        String line2 = STR. "作者: \{ p.author } 《\{ p.title }》" ;
        System.out.println(line1);
        System.out.println(line2);

        // 嵌入式表达式内部可以使用双引号字符而无需对其进行转义
        String filePath = "tmp.dat";
        File file = new File(filePath);
        String msg = STR. "The file \{ filePath } \{ file.exists() ? "does" : "does not" } exist" ;
        System.out.println(msg);

        // 嵌入式表达式内部语句可以换行
        String time = STR. "The time is \{
                // The java.time.format package is very useful
                DateTimeFormatter
                        .ofPattern("HH:mm:ss")
                        .format(LocalTime.now())
                } right now" ;
        System.out.println(time);

        // 嵌入的表达式可以是后缀递增表达式
        int index = 0;
        String data = STR. "\{ index++ }, \{ index++ }, \{ index++ }, \{ index++ }" ;
        System.out.println(data);

        // 嵌入的表达式可以又是一个String Template，即可以嵌套模板表达式
        String[] fruit = {"apples", "oranges", "peaches"};
        String temp = STR. "\{ fruit[0] }, \{
                STR. "\{ fruit[1] }, \{ fruit[2] }"
                }" ;
        System.out.println(temp);
    }

    private String getSomething() {
        return "壮气";
    }

    record Poetry(String author, String title) {
    }
}
