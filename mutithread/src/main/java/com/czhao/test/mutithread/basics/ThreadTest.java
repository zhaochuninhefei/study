package com.czhao.test.mutithread.basics;

/**
 * @author zhaochun
 */
public class ThreadTest {
    public static void main(String[] args) throws InterruptedException {
        // 直接 new 一个Thread对象
        Thread subThread = new Printer();
        // 调用Thread对象的 start 方法，注意不能调用 run 方法
        subThread.start();
        System.out.println("Print in mainThread.");
    }

    // 定义一个继承了Thread的类
    static class Printer extends Thread {
        // 重写run方法，实现自己的业务逻辑
        @Override
        public void run() {
            System.out.println("Print in subThread.");
        }
    }
}
