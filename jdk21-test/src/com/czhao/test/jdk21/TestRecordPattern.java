package com.czhao.test.jdk21;

/**
 * @author zhaochun
 */
public class TestRecordPattern {
    public static void main(String[] args) {
        TestRecordPattern me = new TestRecordPattern();
        me.test01();
    }

    private void test01() {
        Point p = new Point(1, 2);
        printSum(p);

        Rectangle r = new Rectangle(
                new ColoredPoint(new Point(1, 2), Color.RED),
                new ColoredPoint(new Point(3, 4), Color.BLUE)
        );
        printUpperLeftColoredPointNoNesting(r);
        printColorOfUpperLeftPointNesting(r);
    }

    static void printSum(Object obj) {
        if (obj instanceof Point(int x, int y)) {
            System.out.println(x + y);
        }
    }

    static void printUpperLeftColoredPointNoNesting(Rectangle r) {
        if (r instanceof Rectangle(ColoredPoint ul, ColoredPoint lr)) {
            System.out.println(ul.c());
        }
    }

    static void printColorOfUpperLeftPointNesting(Rectangle r) {
        if (r instanceof Rectangle(ColoredPoint(Point p, Color c),
                                   ColoredPoint lr)) {
            System.out.println(c);
        }
    }

    record Point(int x, int y) {
    }

    enum Color {RED, GREEN, BLUE}

    record ColoredPoint(Point p, Color c) {
    }

    record Rectangle(ColoredPoint upperLeft, ColoredPoint lowerRight) {
    }
}
