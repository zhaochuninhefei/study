package com.czhao.test.jdk21;

/**
 * @author zhaochun
 */
public class TestScopedValue {
    public static void main(String[] args) {
        var me = new TestScopedValue();
        me.test01();
    }

    record MyRecord(int a, int b) {}

    final static ScopedValue<MyRecord> RECORD = ScopedValue.newInstance();

    private void test01() {
        Thread.ofVirtual().name("v1").start(() -> {
            System.out.println("v1 start");
            ScopedValue.where(RECORD, new MyRecord(1, 2))
                    .run(() -> handleOne("test1"));
        });

        Thread.ofVirtual().name("v2").start(() -> {
            System.out.println("v2 start");
            ScopedValue.where(RECORD, new MyRecord(3, 4))
                    .run(() -> handleOne("test2"));
        });

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private void handleOne(String str) {
        System.out.println("RECORD in handleOne of " + Thread.currentThread().getName() + ":" + RECORD.get());
        var result = handleTwo(str);
        System.out.println("result of handleTwo: " + result);
    }

    private int handleTwo(String str) {
        System.out.println("RECORD in handleTwo before ScopedValue.where of " + Thread.currentThread().getName() + ":" + RECORD.get());
        System.out.println(str);
        ScopedValue.where(RECORD, new MyRecord(5, 6))
                .run(() -> handleThree(str));
        System.out.println("RECORD in handleTwo after ScopedValue.where of " + Thread.currentThread().getName() + ":" + RECORD.get());
        return 1;
    }

    private void handleThree(String str) {
        System.out.println("RECORD in handleThree of " + Thread.currentThread().getName() + ":" + RECORD.get());
        System.out.println(str);
    }
}
