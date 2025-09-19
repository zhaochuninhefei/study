package com.czhao.test.jdk14;

/**
 * JEP 305: Pattern Matching for instanceof (Preview)
 *
 * @author zhaochun
 */
public class TestInstanceof {
    public static void main(String[] args) {
        TestInstanceof me = new TestInstanceof();
        me.test01("test");
        me.test02(100);
    }

    private void test01(Object o) {
        if (o instanceof Integer) {
            Integer i = (Integer) o;
            System.out.println(i.intValue());
        } else if (o instanceof String) {
            String s = (String) o;
            System.out.println(s.charAt(0));
        }
    }

    private void test02(Object o) {
        if (o instanceof Integer i && i > 0) {
            System.out.println(i.intValue());
        } else if (o instanceof String s && s.startsWith("t")) {
            System.out.println(s.charAt(0));
        }
    }
}
