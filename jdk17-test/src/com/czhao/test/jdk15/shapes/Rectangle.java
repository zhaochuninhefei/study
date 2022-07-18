package com.czhao.test.jdk15.shapes;

import com.czhao.test.jdk15.shapes.Shape;

/**
 * @author zhaochun
 */
public sealed class Rectangle extends Shape permits FilledRectangle, TransparentRectangle {
    @Override
    public int size() {
        return 0;
    }
}
