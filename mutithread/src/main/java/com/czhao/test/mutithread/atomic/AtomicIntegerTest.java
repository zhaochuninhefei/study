package com.czhao.test.mutithread.atomic;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicInteger;

/**
 * @author zhaochun
 */
public class AtomicIntegerTest {
    public static void main(String[] args) {
        AtomicIntegerTest me = new AtomicIntegerTest();
        me.testCounter();
    }

    private void testCounter() {
        AtomicInteger counter = new AtomicInteger(0);
        CountDownLatch countDownLatch = new CountDownLatch(10);
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 10; i++) {
            executorService.submit(() -> {
                for (int j = 0; j < 10000; j++) {
                    counter.incrementAndGet();
                }
                countDownLatch.countDown();
            });
        }
        executorService.shutdown();
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println(counter.get());
    }
}
