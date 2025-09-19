package com.czhao.test.mutithread.threadsafe;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author zhaochun
 */
public class VolatitleTest {
    public static void main(String[] args) {
        VolatitleTest me = new VolatitleTest();
//        for (int i = 0; i < 10; i++) {
//            me.testCounter();
//        }
        me.testWorkStatus();
//        while (true) {
//            me.testNumberRange();
//        }
    }

    private void testCounter() {
        class Counter implements Runnable {
            private volatile int count;

            @Override
            public void run() {
                for (int i = 0; i < 1000; i++) {
                    increase();
                }
            }

            public void printCount() {
                System.out.println(count);
            }

            private void increase() {
                count++;
            }
        }

        Counter counter = new Counter();

        ExecutorService executorService = Executors.newFixedThreadPool(50);
        List<Future> futures = new ArrayList<>();
        for (int i = 0; i < 50; i++) {
            futures.add(executorService.submit(counter));
        }
        executorService.shutdown();
        futures.forEach(future -> {
            try {
                future.get();
            } catch (InterruptedException | ExecutionException e) {
                e.printStackTrace();
            }
        });

        counter.printCount();
    }

    private void testWorkStatus() {
        class WorkStatus {
            // 可以尝试使用volatile和不使用volatile
            private volatile boolean isWorking;

            public boolean getWorking() {
                return isWorking;
            }

            // 第一个线程不断调用这个方法，试图碰到"isWorking != isWorking"的情况
            public void tryExist() {
                System.out.println("tryExist...");
                // 前面的isWorking与后面的isWorking在读取数据时，可能读到不一样的值。
                // 不管isWorking有没有被volatile修饰，两次读取isWorking之间另一个线程可能会写入新值到主存，
                // 那么只要后读取的那次重新从主存read了，这个判断的结果就可以为真
                if (getWorking() != getWorking()) {
                    System.out.println("Why?");
                    System.exit(0);
                }
            }

            // 第二个线程不断调用这个方法，不断改变isWorking的值
            public void swap() {
                isWorking = !isWorking;
            }
        }
        WorkStatus workStatus = new WorkStatus();

        Thread t1 = new Thread(() -> {
            System.out.println("t1 is started...");
            while (true) {
                workStatus.tryExist();
            }
        });
        t1.start();

        Thread t2 = new Thread(() -> {
            System.out.println("t2 is started...");
            while (true) {
                workStatus.swap();
            }
        });
        t2.start();
    }

    private void testNumberRange() {
        class NumberRange {
            private volatile int lower = 0;
            private volatile int upper = 5;

            public void check() {
                if (lower > upper) {
                    System.out.println("Why?");
                    System.exit(0);
                } else {
                    System.out.println("It's OK!");
                }
            }

            public void setLower(int value) {
                // 条件判断
                if (value <= upper) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    // 赋值
                    this.lower = value;
                }
            }

            public void setUpper(int value) {
                // 条件判断
                if (value >= lower) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    // 赋值
                    this.upper = value;
                }
            }
        }

        NumberRange numberRange = new NumberRange();

        Thread t1 = new Thread(() -> numberRange.setLower(4));
        t1.start();

        Thread t2 = new Thread(() -> numberRange.setUpper(3));
        t2.start();

        try {
            Thread.sleep(2000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        numberRange.check();
    }
}
