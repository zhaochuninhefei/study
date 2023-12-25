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
        printXCoordOfUpperLeftPointWithPatterns(r);
    }

    static void printSum(Object obj) {
        if (obj instanceof Point(int x, int y)) {
            System.out.println(x + y);
        }
    }

    static void printUpperLeftColoredPointNoNesting(Rectangle r) {
        if (r instanceof Rectangle(ColoredPoint ul, ColoredPoint _)) {
            System.out.println(ul.c());
        }
    }

    static void printColorOfUpperLeftPointNesting(Rectangle r) {
        if (r instanceof Rectangle(ColoredPoint(Point _, Color c),
                                   ColoredPoint _)) {
            System.out.println(c);
        }
    }

    static void printXCoordOfUpperLeftPointWithPatterns(Rectangle r) {
        if (r instanceof Rectangle(ColoredPoint(Point(var x, var _), var _),
                                   var _)) {
            System.out.println("左上角横坐标：" + x);
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
