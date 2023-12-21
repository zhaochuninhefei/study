package com.czhao.test.jdk21;

import java.util.Arrays;
import java.util.HashSet;
import java.util.LinkedHashSet;

/**
 * @author zhaochun
 */
public class TestSequencedCollections {
    public static void main(String[] args) {
        TestSequencedCollections me = new TestSequencedCollections();
        // 打印 STRS
        System.out.println(STR."C_SOME_STRINGS = \{ Arrays.toString(C_SOME_STRINGS) }");
        for (int i = 0; i < 10; i++) {
            System.out.println(STR."----- i = \{ i } -----");
            me.test01(i+1);
        }
    }

    private void test01(int i) {
        LinkedHashSet<String> setSquenced = new LinkedHashSet<>();
        HashSet<String> setNotSquenced = new HashSet<>();
        // 按相同的顺序从 STR 中获取元素并加入 setSquenced 与 setNotSquenced
        for (int j = 0; j < i; j++) {
            setSquenced.add(C_SOME_STRINGS[j]);
            setNotSquenced.add(C_SOME_STRINGS[j]);
        }
        System.out.println(setSquenced);
        System.out.println(setNotSquenced);

        System.out.println("-----setSquenced 倒序:");
        System.out.println(setSquenced.reversed());
    }

    // 定义一个长度为10的String数组类型的常量
    private static final String[] C_SOME_STRINGS = new String[]{"a3", "b3", "a2", "a1", "a0", "a10", "b1", "b2", "b0", "b10"};

}
