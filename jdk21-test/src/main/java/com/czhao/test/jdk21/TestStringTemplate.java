package com.czhao.test.jdk21;

import java.io.File;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.stream.Stream;

import static java.lang.StringTemplate.STR;
import static java.util.FormatProcessor.FMT;

/**
 * 测试StringTemplate(需要将编译level调整为 Java21 Preview 预览版本)
 *
 * @author zhaochun
 */
public class TestStringTemplate {
    public static void main(String[] args) {
        TestStringTemplate me = new TestStringTemplate();
        me.test01();
        me.testHtmlTemplate();
        me.testJsonTemplate();
        me.testMdTemplate();
        me.testFMT();
        me.testLoopByNested();
    }

    private void test01() {
        var name = "zhaochun";
        // 要使用 StringTemplate, 请将工程的java编译level调整为 21 的 Preview 预览版本
        var message = STR. "Welcome to use String Template, \{ name } !" ;
        System.out.println(message);

        // 直接埋入变量
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

    private void testHtmlTemplate() {
        String title = "My Web Page";
        String text = "Hello, world";
        String html = STR. """
            <html>
              <head>
                <title>\{ title }</title>
              </head>
              <body>
                <p>\{ text }</p>
              </body>
            </html>
            """ ;
        System.out.println(html);
    }

    private void testJsonTemplate() {
        String name = "Joan Smith";
        String phone = "555-123-4567";
        String address = "1 Maple Drive, Anytown";
        String json = STR. """
            {
                "name":    "\{ name }",
                "phone":   "\{ phone }",
                "address": "\{ address }"
            }
            """ ;
        System.out.println(json);
    }


    record Rectangle(String name, double width, double height) {
        double area() {
            return width * height;
        }
    }

    private void testMdTemplate() {
        Rectangle[] zone = new Rectangle[] {
                new Rectangle("Alfa", 17.8, 31.4),
                new Rectangle("Bravo", 9.6, 12.4),
                new Rectangle("Charlie", 7.1, 11.23),
        };
        String table = STR."""
            | Description | Width | Height | Area |
            | --- | --- | --- | --- |
            | \{zone[0].name} | \{zone[0].width} | \{zone[0].height} | \{zone[0].area()} |
            | \{zone[1].name} | \{zone[1].width} | \{zone[1].height} | \{zone[1].area()} |
            | \{zone[2].name} | \{zone[2].width} | \{zone[2].height} | \{zone[2].area()} |

            Total: \{zone[0].area() + zone[1].area() + zone[2].area()}
            """;
        System.out.println(table);
    }

    private void testFMT() {
        Rectangle[] zone = new Rectangle[] {
                new Rectangle("Alfa", 17.8, 31.4),
                new Rectangle("Bravo", 9.6, 12.4),
                new Rectangle("Charlie", 7.1, 11.23),
        };
        String table = FMT."""
            Description     Width    Height     Area
            %-12s\{zone[0].name}  %7.2f\{zone[0].width}  %7.2f\{zone[0].height}     %7.2f\{zone[0].area()}
            %-12s\{zone[1].name}  %7.2f\{zone[1].width}  %7.2f\{zone[1].height}     %7.2f\{zone[1].area()}
            %-12s\{zone[2].name}  %7.2f\{zone[2].width}  %7.2f\{zone[2].height}     %7.2f\{zone[2].area()}
            \{" ".repeat(28)} Total %7.2f\{zone[0].area() + zone[1].area() + zone[2].area()}
            """;
        System.out.println(table);
    }

    private void testLoopByNested() {
        Rectangle[] zone = new Rectangle[] {
                new Rectangle("Alfa", 17.8, 31.4),
                new Rectangle("Bravo", 9.6, 12.4),
                new Rectangle("Charlie", 7.1, 11.23),
        };
        String table = STR."""
            | Description | Width | Height | Area |
            | --- | --- | --- | --- |
            \{ createLines(zone) }
            Total: \{ Stream.of(zone).mapToDouble(Rectangle::area).sum() }
            """;
        System.out.println(table);
    }

    private String createLines(Rectangle[] zone) {
        StringBuilder lines = new StringBuilder();
        for (Rectangle z : zone) {
            var line = STR."| \{z.name} | \{z.width} | \{z.height} | \{z.area()} |";
            lines.append(line).append("\n");
        }
        return lines.toString();
    }
}
