package com.czhao.test.jdk21;

/**
 * @author zhaochun
 */
public class TestSwitchPatternMatch {
    public static void main(String[] args) {
        TestSwitchPatternMatch me = new TestSwitchPatternMatch();
        me.testSwitchObject(null);
        me.testSwitchObject(new Color[]{Color.RED, Color.BLUE});
        me.testSwitchString("1234");
        me.testSwitchEnum();
    }

    private void testSwitchObject(Object o) {
        switch (o) {
            // 匹配 null
            case null -> System.out.println("o is null");

            // 匹配 String类型 + 空字符串
            case String s when s.isBlank() -> System.out.println("o is blank.");
            // 匹配 String类型 + 前缀
            case String s when s.startsWith("Prefix") -> System.out.println("o starts with Prefix.");
            // 匹配 String类型 + 正则表达式
            case String s when s.matches("^[0-9]+$") -> System.out.println("o is a digit.");
            // 匹配 String类型 (这个String类型匹配不能放到"匹配 String类型 + 空字符串"的前面，否则会导致后续所有 String类型+when的匹配走不到,会引起编译错误)
            case String s -> System.out.println("o is a string: " + s);

            // 匹配 Integer类型 + 正数
            case Integer i when i > 0 -> System.out.println("o is a positive integer.");
            // 匹配 Integer类型 + 负数
            case Integer i when i < 0 -> System.out.println("o is a negative integer.");
            // 匹配 Integer类型
            case Integer _ -> System.out.println("o is zero.");

            // 匹配long
            case Long l -> System.out.println("o is a long: " + l);

            // 匹配double
            case Double d -> System.out.println("o is a double: " + d);

            // 匹配 Point类型 + 坐标点位置
            case Point p when p.i > 0 && p.j > 0 -> System.out.println("o is a point of the first quadrant: " + p);
            // 匹配 Point类型
            case Point p -> System.out.println("o is a point: " + p);

            // 匹配 Color[]
            case Color[] colors -> {
                System.out.println("o is a color array:");
                for (Color color : colors) {
                    System.out.println(color);
                }
            }

            // default分支不能去掉，否则会导致switch不能穷尽所有可能
            default -> System.out.println("unknown o");
        }
    }

    record Point(int i, int j) {}
    enum Color { RED, GREEN, BLUE}

    private void testSwitchString(String s) {
        switch (s) {
            case null -> System.out.println("s is null");
            case "const1", "const2" -> System.out.println("s is constant: " + s);
            case String digit when digit.matches("^[0-9]+$") -> System.out.println("s is a digit.");
            default -> System.out.println("default");
        }
    }


    private void testSwitchEnum() {
        var c = Coin.HEADS;
        goodEnumSwitch1(c);
        goodEnumSwitch2(c);
    }

    sealed interface Currency permits Coin {}
    enum Coin implements Currency { HEADS, TAILS }

    static void goodEnumSwitch1(Currency c) {
        switch (c) {
            case Coin.HEADS -> System.out.println("Heads");
            case Coin.TAILS -> System.out.println("Tails");
            default -> System.out.println("Some currency");
        }
    }

    static void goodEnumSwitch2(Coin c) {
        switch (c) {
            case HEADS -> System.out.println("Heads");
            case TAILS -> System.out.println("Tails");
            default -> System.out.println("Some currency");
        }
    }
}
