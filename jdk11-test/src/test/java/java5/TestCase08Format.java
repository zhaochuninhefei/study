package java5;

import java.text.MessageFormat;
import java.text.SimpleDateFormat;
import java.util.*;

/**
 * @author zhaochun
 */
public class TestCase08Format {

    public static void main(String[] args) {
        TestCase08Format me = new TestCase08Format();
        me.test01_formatter();
        me.test02_printf();
        me.test03_stringFormat();
        me.test04_messageFormat();
        me.test05_dateFormat();
    }

    private void test01_formatter() {
        StringBuilder sb = new StringBuilder();
        Formatter formatter = new Formatter(sb);
        // "  前不见古人，  后不见来者。 念天地之悠悠， 独怆然而涕下。"
        formatter.format("%4$7s，%3$7s。%2$7s，%1$7s。%n", "独怆然而涕下", "念天地之悠悠", "后不见来者", "前不见古人");
        // "祖冲之的迷之数字 ： +3.1415927 "
        formatter.format("祖冲之的迷之数字 ： %+5.7f %n", Math.PI);
        // "某款手机价格 : ￥ 5,988.00"
        formatter.format("某款手机价格 : ￥ %(,.2f", 5988.0);
        System.out.println(formatter.toString());
        formatter.close();
    }

    private void test02_printf() {
        List<String> lines = new ArrayList<>();
        lines.add("人闲桂花落，");
        lines.add("夜静春山空。");
        lines.add("月出惊山鸟，");
        lines.add("时鸣春涧中。");
        for (int i = 0; i < lines.size(); i++) {
            System.out.printf("Line %d: %s%n", i + 1, lines.get(i));
        }
    }

    private void test03_stringFormat() {
        Calendar c = new GregorianCalendar(2020, Calendar.MAY, 28);
        System.out.println(String.format("今天是个好日子: %1$tY-%1$tm-%1$te", c));
    }

    private void test04_messageFormat() {
        String msg = "您好，{0}！有您的快递哦！请到{1}号柜拿取您的快递，每超时{2}小时要收费{3}元哦～～～";
        MessageFormat mf = new MessageFormat(msg);
        String fmsg = mf.format(new Object[]{"张三", 3, 8, 2});
        System.out.println(fmsg);
    }

    private void test05_dateFormat() {
        String str = "2020-05-28 14:55:21";
        SimpleDateFormat format1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        SimpleDateFormat format2 = new SimpleDateFormat("yyyyMMddHHmmss");
        try {
            System.out.println(format2.format(format1.parse(str)));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
