package com.czhao.test.mutithread.atomic;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Random;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicLong;
import java.util.concurrent.atomic.LongAccumulator;
import java.util.concurrent.atomic.LongAdder;

/**
 * @author zhaochun
 */
public class LongAdderAndLongAccumulatorTest {
    public static void main(String[] args) {
        LongAdderAndLongAccumulatorTest me = new LongAdderAndLongAccumulatorTest();
        me.testLongAdder();
        me.testLongAccumulator();
        me.testAtomicLongPerformance();
        me.testLongAdderPerformance();
    }

    private void testLongAdder() {
        // LongAdder默认初始值为0
        LongAdder counter = new LongAdder();
        CountDownLatch countDownLatch = new CountDownLatch(10);
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 10; i++) {
            executorService.submit(() -> {
                for (int j = 0; j < 10000; j++) {
                    // 加一操作
                    counter.increment();
                    // 累加操作
                    counter.add(2);
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
        System.out.println(counter.sum());
    }

    private void testLongAccumulator() {
        // 第一个参数是LongBinaryOperator，一个2入参1返回的函数式接口，指定了执行什么样的二目运算；第二个参数是初始值；
        // 初始值会作为LongBinaryOperator的第一个参数left参与运算
        LongAccumulator longAccumulator = new LongAccumulator((left, right) -> Math.max(left, right) - 1, 1);
        CountDownLatch countDownLatch = new CountDownLatch(10);
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 10; i++) {
            executorService.submit(() -> {
                for (int j = 0; j < 1000; j++) {
                    // accumulate对应的逻辑就是构造方法处传入的lambda函数 LongBinaryOperator
                    // 这里传入的参数会作为 LongBinaryOperator 的第二个入参right参与计算
                    longAccumulator.accumulate(new Random().nextInt());
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
        System.out.println(longAccumulator.longValue());
    }

    private void testAtomicLongPerformance() {
        int threadCnt = 500;

        LocalDateTime atomicLongStartTime = LocalDateTime.now();
        AtomicLong atomicLong = new AtomicLong(0);
        CountDownLatch countDownLatch = new CountDownLatch(threadCnt);
        ExecutorService executorService = Executors.newFixedThreadPool(threadCnt);
        for (int i = 0; i < threadCnt; i++) {
            executorService.submit(() -> {
                for (int j = 0; j < 100000; j++) {
                    // 加一操作
                    atomicLong.incrementAndGet();
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
        LocalDateTime atomicLongStopTime = LocalDateTime.now();
        System.out.println("atomicLong耗时(ms):" + Duration.between(atomicLongStartTime, atomicLongStopTime).toMillis());
    }

    private void testLongAdderPerformance() {
        int threadCnt = 500;

        LocalDateTime longAdderStartTime = LocalDateTime.now();
        LongAdder longAdder = new LongAdder();
        CountDownLatch countDownLatch2 = new CountDownLatch(threadCnt);
        ExecutorService executorService2 = Executors.newFixedThreadPool(threadCnt);
        for (int i = 0; i < threadCnt; i++) {
            executorService2.submit(() -> {
                for (int j = 0; j < 100000; j++) {
                    // 加一操作
                    longAdder.increment();
                }
                countDownLatch2.countDown();
            });
        }
        executorService2.shutdown();
        try {
            countDownLatch2.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        LocalDateTime longAdderStopTime = LocalDateTime.now();
        System.out.println("longAdder耗时(ms):" + Duration.between(longAdderStartTime, longAdderStopTime).toMillis());
    }
}
