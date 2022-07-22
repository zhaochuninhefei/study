package com.czhao.test.jdk17;

/**
 * JEP 406:	Pattern Matching for switch (Preview)
 *
 * @author zhaochun
 */
public class TestPatternMatchSwitch {
    public static void main(String[] args) {
        TestPatternMatchSwitch me = new TestPatternMatchSwitch();
        System.out.println(me.test01(100));;
        System.out.println(me.test01("asdf"));;
        System.out.println(me.test01(null));;
    }

    private String test01(Object o) {
        return switch (o) {
            case null -> "null";
            case Integer i -> String.format("Integer i : %s", i);
            case Long l -> String.format("Long l : %s", l);
            case Double d -> String.format("Double d : %s", d);
            case String s -> String.format("String s : %s", s);
            default -> o.toString();
        };
    }
}
