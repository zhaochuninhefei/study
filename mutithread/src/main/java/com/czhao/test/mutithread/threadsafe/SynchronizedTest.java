package com.czhao.test.mutithread.threadsafe;

import java.time.Duration;
import java.time.LocalDateTime;

/**
 * @author zhaochun
 */
public class SynchronizedTest {
    public static void main(String[] args) {
        SynchronizedTest me = new SynchronizedTest();
        me.testBiasedLock();
    }

    private void testBiasedLock() {
        class Counter implements Runnable {
            protected int count;
            protected int maxNumber = 10000;
            protected LocalDateTime startTime;
            protected LocalDateTime stopTime;

            public void setMaxNumber(int maxNumber) {
                this.maxNumber = maxNumber;
            }

            @Override
            public void run() {
                startTime = LocalDateTime.now();
                for (int i = 0; i < maxNumber; i++) {
                    increase();
                }
                stopTime = LocalDateTime.now();
            }

            public void printResult() {
                Duration duration = Duration.between(startTime, stopTime);
                System.out.println(String.format("最终结果 : %s , 耗时 : %s 纳秒", count, duration.getNano()));
            }

            protected void increase() {
                count++;
            }
        }

        class SynchronizedCounter extends Counter {
            @Override
            protected void increase() {
                synchronized (this) {
                    count++;
                }
            }
        }

        int maxNumber = 1000000000;

        Counter counter = new Counter();
        counter.setMaxNumber(maxNumber);
        Thread t1 = new Thread(counter);
        t1.start();

        SynchronizedCounter synchronizedCounter = new SynchronizedCounter();
        synchronizedCounter.setMaxNumber(maxNumber);
        Thread t2 = new Thread(synchronizedCounter);
        t2.start();

        try {
            t1.join();
            t2.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        counter.printResult();
        synchronizedCounter.printResult();
    }
}
