package com.czhao.test.jdk16;

/**
 * JEP 390: Warnings for Value-Based Classes
 *
 * @author zhaochun
 */
public class TestValueBased {
    public static void main(String[] args) {
        TestValueBased me = new TestValueBased();
        me.test01();
        me.test02();
    }

    private void test01() {
        Integer lock = Integer.valueOf("100");
        // Integer已经是`value-based`类型，其构造方法已经被废弃
        // lock = new Integer(100);
        // 这里会警告`Attempt to synchronize on an instance of a value-based class `
        synchronized (lock) {
            System.out.println("test Integer for synchronized");
        }
    }

    private void test02() {
        Integer i1 = Integer.valueOf("127");
        Integer i2 = Integer.valueOf("127");
        System.out.println(i1 == i2);

        Integer i3 = Integer.valueOf("128");
        Integer i4 = Integer.valueOf("128");
        System.out.println(i3 == i4);
    }
}
