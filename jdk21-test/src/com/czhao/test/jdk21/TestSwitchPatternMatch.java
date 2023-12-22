package com.czhao.test.jdk21;

/**
 * @author zhaochun
 */
public class TestSwitchPatternMatch {
    public static void main(String[] args) {
        TestSwitchPatternMatch me = new TestSwitchPatternMatch();

        System.out.println("===== testSwitchObject =====");
        me.testSwitchObject(null);
        me.testSwitchObject(new Color[]{Color.RED, Color.BLUE});

        System.out.println("===== testSwitchString =====");
        me.testSwitchString("1234");
        me.testSwitchString(null);

        System.out.println("===== testSwitchEnum =====");
        me.testSwitchEnum();

        System.out.println("===== testTypeCoverage =====");
        me.testTypeCoverage("99", Color.GREEN);
        me.testTypeCoverage(-98, Color.BLUE);
        me.testTypeCoverage("asdf", Color.RED);

        System.out.println("===== testSealedExhaustive =====");
        me.testSealedExhaustive(new A());
        me.testSealedExhaustive(new B());
        me.testSealedExhaustive(new C(1));

        System.out.println("===== testNullDefault =====");
        me.testNullDefault("y");
        me.testNullDefault("N");
        me.testNullDefault(null);
        me.testNullDefault("");
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
            // 匹配 Integer类型 (这里使用了不具名变量与模式"_"，需要将编译level调整为 Java21 Preview 预览版本)
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
            // 这里不需要再写 只匹配String类型的case，否则会导致default分支被覆盖(入参类型已经是String了)
//            case String str -> System.out.println("s is a string: " + str);
            default -> System.out.println("s is a string: " + s);
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

    private void testTypeCoverage(Object o, Color color) {
        var result = switch (o) {
            case Integer i -> i;
            case String s when s.matches("^\\d+$") -> Integer.parseInt(s);
            // 这里适合使用default保证穷尽，因为无法列举出所有可能的类型分支
            default -> 0;
        };
        System.out.println("o: " + result);

        int numLetters = switch (color) {
            case RED -> 3;
            case GREEN -> 5;
            case BLUE -> 6;
            // 这里不推荐使用default保证穷尽，因为分支较少且明确，不加default有助于编译器检查switch分支有没有遗漏某个case。
        };
        System.out.println("numLetters:" + numLetters);
    }

    sealed interface S permits A, B, C {}
    static final class A implements S {}
    static final class B implements S {}
    record C(int i) implements S {}    // 隐式final

    private void testSealedExhaustive(S s) {
        var result = switch (s) {
            case A _ -> 1;
            case B _ -> 2;
            case C _ -> 3;
        };
        System.out.println(result);
    }

    private void testNullDefault(String s) {
        int result = switch (s) {
            case "y", "Y" -> 1;
            case "n", "N" -> -1;
            case null, default -> 0;
        };
        System.out.println(result);
    }
}
