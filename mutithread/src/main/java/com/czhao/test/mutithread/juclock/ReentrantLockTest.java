package com.czhao.test.mutithread.juclock;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author zhaochun
 */
public class ReentrantLockTest {
    public static void main(String[] args) {
        ReentrantLockTest me = new ReentrantLockTest();

//        for (int i = 0; i < 10; i++) {
//            me.testCounter();
//        }

//        me.testWorkFlow();
        me.testThreadStatus();
    }

    private void testCounter() {
        class Counter implements Runnable {
            private final Lock lock = new ReentrantLock();
            private int count;

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
                lock.lock();
                try {
                    count++;
                } finally {
                    lock.unlock();
                }
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

    private void testWorkFlow() {
        class WorkFlow {
            private int step = 1;
            private final ReentrantLock lock = new ReentrantLock();

            // Condition用于控制线程是否释放锁以及被唤醒
            // condition1用于控制刘备线程
            private final Condition condition1 = lock.newCondition();
            // condition2用于控制关羽线程
            private final Condition condition2 = lock.newCondition();
            // condition3用于控制张飞线程
            private final Condition condition3 = lock.newCondition();

            // 刘备线程遍历执行doStep1
            public void doStep1() {
                lock.lock();
                try {
                    while (step != 1) {
                        // 当前线程在 condition1 上等待
                        condition1.await();
                    }
                    System.out.println(String.format("%s 把军师的信递给了二弟 ...", Thread.currentThread().getName()));
                    step = 2;
                    // 唤醒所有在 condition2 上等待的线程
                    condition2.signalAll();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }

            // 关羽线程遍历执行doStep2
            public void doStep2() {
                lock.lock();
                try {
                    while (step != 2) {
                        // 当前线程在 condition2 上等待
                        condition2.await();
                    }
                    System.out.println(String.format("%s 把军师的信递给了三弟 ...", Thread.currentThread().getName()));
                    step = 3;
                    // 唤醒所有在 condition3 上等待的线程
                    condition3.signalAll();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }

            // 张飞线程遍历执行doStep3
            public void doStep3() {
                lock.lock();
                try {
                    while (step != 3) {
                        // 当前线程在 condition3 上等待
                        condition3.await();
                    }
                    System.out.println(String.format("%s 把军师的信递给了大哥 ...", Thread.currentThread().getName()));
                    step = 1;
                    // 唤醒所有在 condition1 上等待的线程
                    condition1.signalAll();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
        }
        WorkFlow workFlow = new WorkFlow();

        // 启动刘备线程
        new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                workFlow.doStep1();
            }
        }, "刘备").start();
        // 启动关羽线程
        new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                workFlow.doStep2();
            }
        }, "关羽").start();
        // 启动张飞线程
        new Thread(() -> {
            for (int i = 0; i < 5; i++) {
                workFlow.doStep3();
            }
        }, "张飞").start();
    }

    private void testThreadStatus() {
        class Anyway {
            private final ReentrantLock lock = new ReentrantLock();

            public void doSomething() {
                lock.lock();
                try {
                    System.out.println(Thread.currentThread().getName() + "doSomething...");
                    Thread.sleep(3000);
                    System.out.println(Thread.currentThread().getName() + "doSomething end...");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    lock.unlock();
                }
            }
        }
        Anyway anyway = new Anyway();
        Thread t1 = new Thread(anyway::doSomething, "线程1");
        Thread t2 = new Thread(anyway::doSomething, "线程2");
        t1.start();
        t2.start();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(t1.getName() + "的状态:" + t1.getState());
        System.out.println(t2.getName() + "的状态:" + t2.getState());
    }
}