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
    }

    static void printSum(Object obj) {
        if (obj instanceof Point(int x, int y)) {
            System.out.println(x + y);
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
