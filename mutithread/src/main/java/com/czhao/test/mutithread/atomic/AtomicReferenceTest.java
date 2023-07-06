package com.czhao.test.mutithread.atomic;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicMarkableReference;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.atomic.AtomicStampedReference;

/**
 * @author zhaochun
 */
public class AtomicReferenceTest {
    public static void main(String[] args) {
        AtomicReferenceTest me = new AtomicReferenceTest();
//        me.testAtomicReference();
//        me.testAtomicMarkableReference();
        me.testAtomicStampedReference();
    }

    private void testAtomicReference() {
        class MyStatus {
            private String status1;
            private int status2;

            public MyStatus(String status1, int status2) {
                this.status1 = status1;
                this.status2 = status2;
            }

            @Override
            public String toString() {
                return "MyStatus{" +
                        "status1='" + status1 + '\'' +
                        ", status2=" + status2 +
                        '}';
            }
        }

        MyStatus statusA = new MyStatus("甲", 1);
        MyStatus statusB = new MyStatus("乙", 2);
        MyStatus statusC = new MyStatus("丙", 3);

        AtomicReference<MyStatus> curStatus = new AtomicReference<>();
        curStatus.set(statusC);

        System.out.println(curStatus.get().toString());

        Thread t1 = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                curStatus.compareAndSet(statusA, statusB);
            }
        });
        Thread t2 = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                curStatus.compareAndSet(statusB, statusC);
            }
        });
        Thread t3 = new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                curStatus.compareAndSet(statusC, statusA);
            }
        });

        t1.start();
        t2.start();
        t3.start();

        try {
            t1.join();
            t2.join();
            t3.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(curStatus.get().toString());
    }

    private void testAtomicMarkableReference() {
        class Employee {
            private String name;

            public Employee(String name) {
                this.name = name;
            }

            @Override
            public String toString() {
                return "Employee{" +
                        "name='" + name + '\'' +
                        '}';
            }
        }

        Employee zhangsan = new Employee("张三");
        Employee lisi = new Employee("李四");
        Employee wangwu = new Employee("王五");

        AtomicMarkableReference<Employee> employee = new AtomicMarkableReference<>(zhangsan, true);
        new Thread(() -> System.out.println(employee.compareAndSet(zhangsan, lisi, true, false)))
                .start();
        new Thread(() -> System.out.println(employee.compareAndSet(lisi, wangwu, false, true)))
                .start();
        new Thread(() -> System.out.println(employee.compareAndSet(wangwu, zhangsan, true, false)))
                .start();

        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(employee.getReference().toString());
        System.out.println(employee.isMarked());
    }

    private void testAtomicStampedReference() {
        String weekDayOne = "星期一";
        AtomicStampedReference<String> stampedReference = new AtomicStampedReference<>(weekDayOne, 1);

        int threadCnt = 100;
        ExecutorService executorService = Executors.newFixedThreadPool(threadCnt);
        for (int i = 0; i < threadCnt; i++) {
            String threadName = "" + i;
            executorService.submit(() -> {
                for (int j = 0; j < 100; j++) {
                    String weekDay = stampedReference.getReference();
                    int curStamp = stampedReference.getStamp();
                    // do something ...
                    boolean success = stampedReference.compareAndSet(weekDay, weekDayOne, curStamp, curStamp + 1);
                    if (!success) {
                        System.out.println(String.format("线程 %s compareAndSet fail.", threadName));
                    }
                }
            });
        }
        executorService.shutdown();
    }
}
