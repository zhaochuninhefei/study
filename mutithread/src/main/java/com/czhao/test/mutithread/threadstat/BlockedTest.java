package com.czhao.test.mutithread.threadstat;

import java.util.Scanner;

/**
 * @author zhaochun
 */
public class BlockedTest {
    public static void main(String[] args) {
        BlockedTest me = new BlockedTest();
//        me.testEnterBlocked();
        me.testReenterBlocked();
    }

    private void testEnterBlocked() {
        class EchoPrinter {
            // synchronized修饰实例方法，则锁为 this，即EchoPrinter的一个实例
            public synchronized void echoPrint1() {
                Scanner scanner = new Scanner(System.in);
                System.out.println("我是胡汉三，我要准备跑路了，随便说点啥吧：");
                String content = scanner.nextLine();
                System.out.println(content);
            }

            public void echoPrint2() {
                System.out.println("我是潘冬子，准备抢锁，按导演的计划，我会失败...");
                // 使用 this 作为锁，即EchoPrinter的一个实例
                synchronized (this) {
                    System.out.println("我是潘冬子，我抢到锁了...");
                }
            }
        }
        // 因为锁是EchoPrinter的一个实例，这里需要先生成实例
        EchoPrinter echoPrinter = new EchoPrinter();

        // 创建线程1并启动，线程1将运行echoPrint方法
        Thread t1 = new Thread(echoPrinter::echoPrint1);
//        t1.setDaemon(true);
        t1.start();

        // 主线程等待1秒钟，以确保线程1启动
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 创建线程2并启动，线程2将运行print方法
        Thread t2 = new Thread(echoPrinter::echoPrint2);
//        t2.setDaemon(true);
        t2.start();

        // 主线程等待1秒钟，以确保线程2启动
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 在主线程输出线程1的状态，由于echoPrint方法里有从System.in读取控制台输入的语句，会在这里陷入操作系统层面的IO阻塞，但JVM中线程状态仍然是RUNNABLE
        System.out.println(String.format("线程 %s 的状态 %s", "胡汉三", t1.getState().toString()));
        // 在主线程输出线程2的状态，由于print方法内部也使用this作为锁，而该锁目前仍然被线程1占用着，所以线程2获取锁失败而进入BLOCKED状态。
        // 注意线程2的BLOCKED状态是JVM线程状态，与线程1在操作系统层面的IO阻塞状态并不相同。
        System.out.println(String.format("线程 %s 的状态 %s", "潘冬子", t2.getState().toString()));

    }

    private void testReenterBlocked() {
        class EchoPrinterAgain {
            // synchronized修饰实例方法，则锁为 this，即EchoPrinter的一个实例
            public synchronized void echoPrint1() {
                Scanner scanner = new Scanner(System.in);
                System.out.println("我是胡汉三，我要准备跑路了，随便说点啥吧：");
                String content = scanner.nextLine();
                System.out.println(content);
                try {
                    // 让出锁，当前JAVA线程进入WAITING状态
                    this.wait();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("我胡汉三又回来了。。。over。。。");
            }

            public void echoPrint2() {
                System.out.println("我是潘冬子，准备抢锁，按导演的计划，我会失败...");
                // 使用 this 作为锁，即EchoPrinter的一个实例
                synchronized (this) {
                    System.out.println("我是潘冬子，我抢到锁了...");
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("我是潘冬子，导演让我喊胡汉三回来...");
                    this.notifyAll();
                    System.out.println("我是潘冬子，我唤醒了胡汉三，等我休息三秒...");
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    System.out.println("我是潘冬子，我即将退出舞台。。。");
                }
            }
        }

        // 因为锁是EchoPrinter的一个实例，这里需要先生成实例
        EchoPrinterAgain echoPrinterAgain = new EchoPrinterAgain();

        // 创建线程1并启动，线程1将运行echoPrint方法
        Thread t1 = new Thread(echoPrinterAgain::echoPrint1);
        t1.start();

        // 主线程等待1秒钟，以确保线程1启动
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // 创建线程2并启动，线程2将运行print方法
        Thread t2 = new Thread(echoPrinterAgain::echoPrint2);
        t2.start();

        // 主线程每隔一秒打印一次两个子线程的状态
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(String.format("线程 %s 的状态 %s", "胡汉三", t1.getState().toString()));
            System.out.println(String.format("线程 %s 的状态 %s", "潘冬子", t2.getState().toString()));

            if (Thread.State.TERMINATED.equals(t1.getState())
                    && Thread.State.TERMINATED.equals(t2.getState())) {
                break;
            }
        }
    }
}
