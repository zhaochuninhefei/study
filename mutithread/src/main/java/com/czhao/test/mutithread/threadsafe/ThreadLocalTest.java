package com.czhao.test.mutithread.threadsafe;

import java.util.concurrent.LinkedBlockingQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

/**
 * @author zhaochun
 */
public class ThreadLocalTest {
    public static void main(String[] args) {
        ThreadLocalTest me = new ThreadLocalTest();
        me.testThreadLocalInExecutor();
    }

    static class Matrix {
        private final Long[] matrix = new Long[1024 * 1024];
    }

    static final ThreadLocal<Matrix> THREAD_LOCAL = new ThreadLocal<Matrix>();

    private void testThreadLocalInExecutor() {
        ThreadPoolExecutor poolExecutor = new ThreadPoolExecutor(5, 5, 1, TimeUnit.MINUTES,
                new LinkedBlockingQueue<>());
        for (int i = 0; i < 50; ++i) {
            poolExecutor.execute(() -> {
                THREAD_LOCAL.set(new Matrix());
                System.out.println("use local varaible" + THREAD_LOCAL.get());
                // 如果不显式调用ThreadLocal变量的remove方法的话，则有可能会发生内存泄露现象
                THREAD_LOCAL.remove();
            });
        }
        System.out.println("pool execute over");
    }
}
