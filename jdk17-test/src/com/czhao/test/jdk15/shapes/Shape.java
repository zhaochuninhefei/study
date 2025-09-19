package com.czhao.test.jdk15.shapes;

/**
 * JEP 360: Sealed Classes (Preview)
 *
 * @author zhaochun
 */
public abstract sealed class Shape
        permits Circle, Rectangle, Square {
    public abstract int size();
}

