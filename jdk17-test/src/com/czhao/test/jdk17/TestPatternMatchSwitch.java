package com.czhao.test.jdk17;

/**
 * JEP 406:	Pattern Matching for switch (Preview)
 *
 * @author zhaochun
 */
public class TestPatternMatchSwitch {
    public static void main(String[] args) {
        TestPatternMatchSwitch me = new TestPatternMatchSwitch();
        System.out.println(me.testPatternMatchSwitch(100));
        System.out.println(me.testPatternMatchSwitch("test"));
        System.out.println(me.testPatternMatchSwitch(null));

        me.testStrSwitch(null);
        me.testStrSwitch("n");
        me.testStrSwitch("test");
    }

    private String testPatternMatchSwitch(Object o) {
        return switch (o) {
            case null -> "o is null!";
            case Integer i -> String.format("Integer i : %d", i);
            case Long l -> String.format("Long l : %d", l);
            case Double d -> String.format("Double d : %f", d);
            case String s -> String.format("String s : %s", s);
            default -> o.toString();
        };
    }

    private void testStrSwitch(String s) {
        switch (s) {
            case null -> System.out.println("s is null!");
            case "Y", "y" -> System.out.println("是");
            case "N", "n" -> System.out.println("否");
            default -> System.out.println(s);
        }
    }
}
