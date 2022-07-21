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
    }

    private void testNoStrictfp() {
        System.out.println(0.1 + 0.2);
    }

    private strictfp void testWithStrictfp() {
        System.out.println(0.1 + 0.2);
    }
}
