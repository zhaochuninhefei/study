package com.czhao.test.jdk14;

import java.util.Arrays;
import java.util.Comparator;
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

    record Point(int x, int y) implements Comparator<Point> {
        // 构造函数可以声明为没有正式形参列表的形式，此时默认使用完整的状态参数列表。
        // 这允许构造函数只执行参数的验证和规范化，而省略显式的字段初始化。
        Point {
            if (x < 0 || y < 0) {
                throw new RuntimeException("坐标只能位于第一象限");
            }
        }

        // 实现接口
        @Override
        public int compare(Point o1, Point o2) {
            return Integer.compare(o1.sum(), o2.sum());
        }

        // 定义实例方法
        public int sum() {
            return x + y;
        }
    }

    record PointGrp(List<Point> points, String grpName) {
    }
}
