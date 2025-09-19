package com.czhao.test.jdk21;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.LinkedHashSet;

/**
 * @author zhaochun
 */
public class TestSequencedCollections {

    // 定义一个长度为10的String数组类型的常量
    private static final String[] C_SOME_STRINGS = new String[]{"a3", "b3", "a2", "a1", "a0", "a10", "b1", "b2", "b0", "b10", "c3", "c2", "c1", "c0", "c10", "d3", "d2", "d1", "d0", "d10"};

    public static void main(String[] args) {
        TestSequencedCollections me = new TestSequencedCollections();
        // 打印 C_SOME_STRINGS
        System.out.println(STR. "C_SOME_STRINGS = \{ Arrays.toString(C_SOME_STRINGS) }" );
        for (int i = 0; i < 20; i++) {
            System.out.println(STR. "----- i = \{ i } -----" );
            me.test01(i + 1);
        }
        me.test02();
        me.test03();
    }

    private void test01(int i) {
        LinkedHashSet<String> setSquenced = new LinkedHashSet<>();
        HashSet<String> setNotSquenced = new HashSet<>();
        // 按相同的顺序从 STR 中获取元素并加入 setSquenced 与 setNotSquenced
        for (int j = 0; j < i; j++) {
            setSquenced.add(C_SOME_STRINGS[j]);
            setNotSquenced.add(C_SOME_STRINGS[j]);
        }
        System.out.println("setSquenced 正序:" + setSquenced);
        System.out.println("setSquenced 倒序:" + setSquenced.reversed());
        System.out.println("setNotSquenced:" + setNotSquenced);
    }

    private void test02() {
        LinkedHashSet<String> setSquenced = new LinkedHashSet<>();
        setSquenced.add("a");
        setSquenced.add("b");
        setSquenced.add("c");
        System.out.println(setSquenced);
        setSquenced.addFirst("d");
        System.out.println(setSquenced);
        setSquenced.addLast("e");
        System.out.println(setSquenced);
        // 已经存在的元素，会被移动到尾部
        setSquenced.addLast("b");
        System.out.println(setSquenced);
        // 已经存在的元素被移动到头部
        setSquenced.addFirst("b");
        System.out.println(setSquenced);
    }

    private void test03() {
        LinkedHashMap<String, String> mapSequenced = new LinkedHashMap<>();
        mapSequenced.put("a", "a");
        mapSequenced.put("b", "b");
        mapSequenced.put("c", "c");
        System.out.println(mapSequenced);
        mapSequenced.putFirst("d", "d");
        System.out.println(mapSequenced);
        mapSequenced.putLast("e", "e");
        System.out.println(mapSequenced);
        mapSequenced.putLast("b", "b");
        System.out.println(mapSequenced);
        mapSequenced.putFirst("b", "b");
        System.out.println(mapSequenced);
    }
}
