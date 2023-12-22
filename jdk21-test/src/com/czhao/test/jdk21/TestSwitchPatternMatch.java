package com.czhao.test.jdk21;

/**
 * @author zhaochun
 */
public class TestSwitchPatternMatch {
    public static void main(String[] args) {
        TestSwitchPatternMatch me = new TestSwitchPatternMatch();
        me.test01();
    }

    private void test01() {
        var c = Coin.HEADS;
        goodEnumSwitch1(c);
        goodEnumSwitch2(c);
    }

    sealed interface Currency permits Coin {}
    enum Coin implements Currency { HEADS, TAILS }

    static void goodEnumSwitch1(Currency c) {
        switch (c) {
            case Coin.HEADS -> {    // Qualified name of enum constant as a label
                System.out.println("Heads");
            }
            case Coin.TAILS -> {
                System.out.println("Tails");
            }
        }
    }

    static void goodEnumSwitch2(Coin c) {
        switch (c) {
            case HEADS -> {
                System.out.println("Heads");
            }
            case Coin.TAILS -> {    // Unnecessary qualification but allowed
                System.out.println("Tails");
            }
        }
    }

//    static void badEnumSwitch(Currency c) {
//        switch (c) {
//            case Coin.HEADS -> {
//                System.out.println("Heads");
//            }
//            case TAILS -> {         // Error - TAILS must be qualified
//                System.out.println("Tails");
//            }
//            default -> {
//                System.out.println("Some currency");
//            }
//        }
//    }
}
