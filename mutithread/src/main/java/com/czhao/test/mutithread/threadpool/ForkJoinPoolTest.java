package com.czhao.test.mutithread.threadpool;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.ForkJoinPool;
import java.util.concurrent.Future;
import java.util.concurrent.RecursiveTask;

/**
 * @author zhaochun
 */
public class ForkJoinPoolTest {
    public static void main(String[] args) {
        ForkJoinPoolTest me = new ForkJoinPoolTest();
        me.testForkJoinPool();
    }

    private void testForkJoinPool() {
        // 计算斐波那契数列第n项，从第三项开始，每一项的值都等于前两项之和
        // 1, 1, 2, 3, 5, 8, 13, 21, 34, 55, 89。。。
        class Fibonacci extends RecursiveTask<Integer> {
            // 斐波那契数列下标，下标从1开始
            int index;

            public Fibonacci(int index) {
                this.index = index;
            }

            @Override
            protected Integer compute() {
                // 前两项直接返回1
                if (index <= 2) {
                    return 1;
                } else {
                    // 准备计算 index - 1 项
                    Fibonacci f1 = new Fibonacci(index - 1);
                    // 将f1任务推入当前工作线程的工作队列，如果有空闲线程，这个任务会被偷走执行
                    f1.fork();
                    // 准备计算 index - 2 项
                    Fibonacci f2 = new Fibonacci(index - 2);
                    // 将f2任务推入当前工作线程的工作队列，如果有空闲线程，这个任务会被偷走执行
                    f2.fork();
                    // 等待f1与f2计算结束，获取返回值在计算它们之和
                    return f1.join() + f2.join();
                }
            }
        }

        ForkJoinPool forkJoinPool = new ForkJoinPool();
        Fibonacci fibonacci = new Fibonacci(30);
        Future<Integer> future = forkJoinPool.submit(fibonacci);
        try {
            System.out.println(future.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }
}
