package com.czhao.test.mutithread.atomic;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.atomic.AtomicIntegerArray;
import java.util.stream.Collectors;

/**
 * @author zhaochun
 */
public class AtomicIntegerArrayTest {
    public static void main(String[] args) {
        AtomicIntegerArrayTest me = new AtomicIntegerArrayTest();
        me.testAtomicIntegerArray();
    }

    private void testAtomicIntegerArray() {
        int[] values = new int[]{1, 2, 3};
        // 作为构造方法参数的values数组不会被修改
        AtomicIntegerArray atomicIntegerArray = new AtomicIntegerArray(values);
        CountDownLatch countDownLatch = new CountDownLatch(10);
        ExecutorService executorService = Executors.newFixedThreadPool(10);
        for (int i = 0; i < 10; i++) {
            executorService.submit(() -> {
                for (int j = 0; j < 100; j++) {
                    atomicIntegerArray.getAndIncrement(0);
                    atomicIntegerArray.getAndAdd(1, 2);
                    atomicIntegerArray.compareAndSet(2, 3, 3);
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
        System.out.println(atomicIntegerArray);
        System.out.println("values不会变：");
        System.out.println(Arrays.toString(values));
    }
}
