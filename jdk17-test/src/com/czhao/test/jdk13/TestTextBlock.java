package com.czhao.test.jdk13;

import javax.script.ScriptEngine;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

/**
 * @author zhaochun
 */
public class TestTextBlock {
    public static void main(String[] args) throws ScriptException {
        TestTextBlock me = new TestTextBlock();
        me.test01();
    }

    private void test01() throws ScriptException {
        String htmlNoBlock =
                "<html>\n" +
                "    <body>\n" +
                "        <p>Hello, world</p>\n" +
                "    </body>\n" +
                "</html>";
        System.out.println("===== htmlNoBlock start =====");
        System.out.println(htmlNoBlock);
        System.out.println("===== htmlNoBlock stop =====\n");

        String htmlWithBlock =
                """
                <html>
                    <body>
                        <p>Hello, world!</p>
                    </body>
                </html>""";
        System.out.println("===== htmlWithBlock start =====");
        System.out.println(htmlWithBlock);
        System.out.println("===== htmlWithBlock stop =====\n");

        String query =
               """
               SELECT `EMP_ID`, `LAST_NAME` FROM `EMPLOYEE_TB`
                WHERE `CITY` = '%s'
                ORDER BY `EMP_ID`, `LAST_NAME`;
               """;
        System.out.println("===== query start =====");
        System.out.println(String.format(query, "合肥"));
        System.out.println("===== query stop =====\n");
    }
}
