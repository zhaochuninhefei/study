package com.czhao.test.mutithread.atomic;

import java.util.concurrent.atomic.AtomicIntegerFieldUpdater;
import java.util.concurrent.atomic.AtomicReferenceFieldUpdater;

/**
 * @author zhaochun
 */
public class AtomicFieldUpdaterTest {
    public static void main(String[] args) {
        AtomicFieldUpdaterTest me = new AtomicFieldUpdaterTest();
        me.testAtomicIntegerFieldUpdater();
        me.testAtomicReferenceFieldUpdater();
    }

    private void testAtomicIntegerFieldUpdater() {
        class Custom {
            private String name;
            volatile int cash;

            public Custom(String name, int cash) {
                this.name = name;
                this.cash = cash;
            }

            @Override
            public String toString() {
                return "Custom{" +
                        "name='" + name + '\'' +
                        ", cash=" + cash +
                        '}';
            }
        }

        Custom zhangsan = new Custom("张三", 1000);
        System.out.println(zhangsan.toString());
        AtomicIntegerFieldUpdater<Custom> updater = AtomicIntegerFieldUpdater.newUpdater(Custom.class, "cash");
        updater.getAndAdd(zhangsan, 300);
        System.out.println(zhangsan.toString());
    }

    private void testAtomicReferenceFieldUpdater() {
        class Member {
            volatile String name = "张三";
        }
        AtomicReferenceFieldUpdater<Member, String> updater = AtomicReferenceFieldUpdater.newUpdater(Member.class, String.class, "name");
        Member member = new Member();
        System.out.println(member.name);
        updater.compareAndSet(member, "张三", "李四");
        System.out.println(member.name);
    }
}
