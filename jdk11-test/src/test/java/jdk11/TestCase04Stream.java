package jdk11;

import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * Stream增强
 *
 * @author zhaochun
 */
public class TestCase04Stream {
    public static void main(String[] args) {
        TestCase04Stream me = new TestCase04Stream();
        me.test01_ofNullable();
        me.test02_dropWhile_takeWhile();
        me.test03_iterate();
    }

    private void test01_ofNullable() {
        // 单个参数的Stream构造方法
        long size1 = Stream.ofNullable(null).count();
        System.out.println(size1);
        long size2 = Stream.ofNullable("").count();
        System.out.println(size2);
    }

    private void test02_dropWhile_takeWhile() {
        // dropWhile 对于有序的stream，从头开始去掉满足条件的元素，一旦遇到不满足元素的就结束
        List lst1 = Stream.of(1, 2, 3, 4, 5, 4, 3, 2, 1)
                .dropWhile(e -> e < 3)
                .collect(Collectors.toList());
        System.out.println(lst1);

        // takeWhile 对于有序的stream，从头开始保留满足条件的元素，一旦遇到不满足的元素就结束
        List lst2 = Stream.of(1, 2, 3, 4, 5, 4, 3, 2, 1)
                .takeWhile(e -> e < 3)
                .collect(Collectors.toList());
        System.out.println(lst2);

        // 虽然这里最后把剩下的元素都收集到了无序的set中，但在此之前，stream对象是有序的，因此结果包含了原来stream中最后的[a2]和[a1]
        Set set1 = Stream.of("a1", "a2", "a3", "a4", "a5", "a4", "a3", "a2", "a1")
                .dropWhile(e -> "a3".compareTo(e) > 0)
                .collect(Collectors.toSet());
        System.out.println(set1);

        // 这里先创建一个无序不重复的set集合，set无序更准确的说法是不保证顺序不变，事实上是有顺序的。
        // 因此这里会发现，dropWhile还是按set当前的元素顺序判定的，一旦不满足条件就结束。
        Set<String> set = new HashSet<>();
        for (int i = 1; i <= 100 ; i++) {
            set.add("test" + i);
        }
        System.out.println(set);
        Set setNew = set.stream()
                .dropWhile(s -> "test60".compareTo(s) > 0)
                .collect(Collectors.toSet());
        System.out.println(setNew);
    }

    private void test03_iterate() {
        // java8里可以创建一个无限流，比如下面这个数列，起始值是1，后面每一项都在前一项的基础上 * 2 + 1
        Stream<Integer> streamInJava8 = Stream.iterate(1, t -> 2 * t + 1);
        // 打印出该数列的前十个: 1,3,7,15,31,63,127,255,511,1023
        System.out.println(streamInJava8.limit(10).map(Object::toString).collect(Collectors.joining(",")));

        // 从Java9开始，iterate方法可以添加一个判定器，可以用于限制数列范围不超过1000
        Stream<Integer> streamFromJava9 = Stream.iterate(1, t -> t < 1000, t -> 2 * t + 1);
        // 这里打印的结果是 1,3,7,15,31,63,127,255,511
        System.out.println(streamFromJava9.map(Objects::toString).collect(Collectors.joining(",")));
    }
}
