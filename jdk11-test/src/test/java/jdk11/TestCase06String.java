package jdk11;

/**
 * String增强
 *
 * @author zhaochun
 */
public class TestCase06String {
    public static void main(String[] args) {
        TestCase06String me = new TestCase06String();
        me.test01_blank();
        me.test02_strip();
        me.test03_repeat();
        me.test04_lines();
    }

    private void test01_blank() {
        // 半角空格
        System.out.println(" ".isBlank());
        // 全角空格
        System.out.println("　".isBlank());
        // 半角空格的unicode字符值
        System.out.println("\u0020".isBlank());
        // 全角空格的unicode字符值
        System.out.println("\u3000".isBlank());
        // 制表符
        System.out.println("\t".isBlank());
        // 回车
        System.out.println("\r".isBlank());
        // 换行
        System.out.println("\n".isBlank());
        // 各种空白字符拼接
        System.out.println(" \t\r\n　".isBlank());
    }

    private void test02_strip() {
        // 全角空格 + 制表符 + 回车 + 换行 + 半角空格 + <内容> + 全角空格 + 制表符 + 回车 + 换行 + 半角空格
        var strTest = "　\t\r\n 你好 jdk11　\t\r\n ";

        // strip 去除两边空白字符
        System.out.println("[" + strTest.strip() + "]");
        // stripLeading 去除开头的空白字符
        System.out.println("[" + strTest.stripLeading() + "]");
        // stripTrailing 去除结尾的空白字符
        System.out.println("[" + strTest.stripTrailing() + "]");
    }

    private void test03_repeat() {
        var strOri = "jdk11";
        var str1 = strOri.repeat(1);
        var str2 = strOri.repeat(3);
        System.out.println(str1);
        System.out.println(str2);
        // repeat传入参数为1时，不会创建一个新的String对象，而是直接返回原来的String对象。
        System.out.println(str1 == strOri);
    }

    private void test04_lines() {
        var strContent = "hello java\rhello jdk11\nhello world\r\nhello everyone";
        // lines方法用 \r 或 \n 或 \r\n 对字符串切割并返回stream对象
        strContent.lines().forEach(System.out::println);
        System.out.println(strContent.lines().count());
    }
}
