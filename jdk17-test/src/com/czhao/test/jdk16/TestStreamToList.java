package com.czhao.test.jdk16;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @author zhaochun
 */
public class TestStreamToList {
    public static void main(String[] args) {
        TestStreamToList me = new TestStreamToList();
        me.test01();
    }

    private void test01() {
        String[] arr = new String[]{"5", "8", "2"};

        List<String> lst1 = Arrays.asList(arr);
        System.out.println("打印lst1:" + lst1);

        List<Integer> lst2 = lst1.stream()
                .map(Integer::parseInt)
                .collect(Collectors.toList());

        List<Integer> lst3 = lst1.stream()
                .map(Integer::parseInt)
                .toList();

        System.out.println("打印lst2:" + lst2);
        System.out.println("打印lst3:" + lst3);

        extracted(lst1, lst2, lst3);
    }

    private static void extracted(List<String> lst1, List<Integer> lst2, List<Integer> lst3) {
        try {
            System.out.println("向lst2添加新元素并排序");
            lst2.add(1);
            lst2.sort(Integer::compareTo);
            System.out.println("打印lst2:" + lst2);
        } catch (Exception e) {
            System.out.println("向lst2添加新元素发生异常:" + e);
        }

        try {
            System.out.println("向lst3添加新元素");
            lst3.add(1);
            System.out.println("打印lst3:" + lst3);
        } catch (Exception e) {
            System.out.println("向lst3添加新元素发生异常:" + e);
        }

        try {
            System.out.println("为lst3排序");
            lst3.sort(Integer::compareTo);
            System.out.println("打印lst3:" + lst3);
        } catch (Exception e) {
            System.out.println("为lst3排序发生异常:" + e);
        }

        try {
            System.out.println("向lst1添加新元素");
            lst1.add("1");
            System.out.println("打印lst1:" + lst1);
        } catch (Exception e) {
            System.out.println("向lst1添加新元素发生异常:" + e);
        }
    }
}
