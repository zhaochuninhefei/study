package com.czhao.test.jdk14;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * JEP 359: Records (Preview)
 *
 * @author zhaochun
 */
public class TestRecord {
    public static void main(String[] args) {
        Point p1 = new Point(10, 20);
        Point p2 = new Point(10, 20);
        System.out.println(p1.x());
        System.out.println(p1.y());
        System.out.println(p1.hashCode());
        System.out.println(p1.equals(p2));
        System.out.println(p1.toString());

        List<Point> points = Arrays.stream(new Point[]{p1, p2}).collect(Collectors.toList());
        PointGrp pointGrp = new PointGrp(points, "pg");
        System.out.println(pointGrp.toString());

        pointGrp.points().add(new Point(0, 0));
        System.out.println(pointGrp.toString());
    }

    record Point(int x, int y) {
        Point {
            if (x < 0 || y < 0) {
                throw new RuntimeException("坐标只能位于第一象限");
            }
        }
    }

    record PointGrp(List<Point> points, String grpName) {
    }
}
