package com.czhao.test.jdk17;

/**
 * JEP 306:	Restore Always-Strict Floating-Point Semantics
 *
 * @author zhaochun
 */
public class TestStrictFloat {

    public static void main(String[] args) {
        TestStrictFloat me = new TestStrictFloat();
        me.testNoStrictfp();
        me.testWithStrictfp();
        me.testFloatAndDouble();
    }

    private void testNoStrictfp() {
        System.out.println(0.1 + 0.2);
    }

    private strictfp void testWithStrictfp() {
        System.out.println(0.1 + 0.2);
    }

    private void testFloatAndDouble() {
        // 12.345678 最后一位没有进位
        float f1 = 12.3456787f;
        System.out.println("f1: " + f1);
        // 123.45679 最后一位进位了
        float f2 = 123.456787f;
        System.out.println("f2: " + f2);
        // 0.12345678 最后一位没有进位
        float f3 = 0.123456787f;
        System.out.println("f3: " + f3);

        // 12.345678901234567 最后一位没有进位
        double d1 = 12.34567890123456789;
        System.out.println("d1: " + d1);
        // 123.45678901234568 最后一位进位了
        double d2 = 123.4567890123456789;
        System.out.println("d2: " + d2);
        // 0.12345678901234568 最后一位进位了
        double d3 = 0.1234567890123456789;
        System.out.println("d3: " + d3);
    }
}
