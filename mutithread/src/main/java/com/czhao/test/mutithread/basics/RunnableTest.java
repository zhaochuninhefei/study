package com.czhao.test.mutithread.basics;

/**
 * @author zhaochun
 */
public class RunnableTest {
    public static void main(String[] args) {
        // 将一个 Runnable 对象作为Thread的构造参数
        Thread subThread = new Thread(new Printer());
        // 调用Thread对象的 start 方法，注意不能调用 run 方法
        subThread.start();
        System.out.println("Print in mainThread.");
    }

    // 定义一个实现了 Runnable 接口的类
    static class Printer implements Runnable {
        // 实现 run 方法
        @Override
        public void run() {
            System.out.println("Print in Runnable.");
        }
    }
}
