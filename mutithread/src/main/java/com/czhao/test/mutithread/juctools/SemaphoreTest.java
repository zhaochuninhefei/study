package com.czhao.test.mutithread.juctools;

import java.time.LocalDateTime;
import java.util.Random;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Semaphore;
import java.util.concurrent.TimeUnit;

/**
 * @author zhaochun
 */
public class SemaphoreTest {
    public static void main(String[] args) {
        SemaphoreTest me = new SemaphoreTest();
        me.testCanteen();
    }

    private void testCanteen() {
        class EmployeeInCanteen implements Runnable {
            private final String name;
            private final Semaphore canteen;

            public EmployeeInCanteen(String name, Semaphore canteen) {
                this.name = name;
                this.canteen = canteen;
            }

            @Override
            public void run() {
                try {
                    System.out.println(LocalDateTime.now() + " " + name + " 来到了食堂门口...");
                    canteen.acquire();
                    System.out.println(LocalDateTime.now() + " " + name + " 进入食堂就餐...");
                    TimeUnit.SECONDS.sleep(3 + new Random().nextInt(3));
                    System.out.println(LocalDateTime.now() + " " + name + " 进餐结束，离开食堂.");
                } catch (InterruptedException e) {
                    e.printStackTrace();
                } finally {
                    canteen.release();
                }
            }
        }
        // 假设食堂最多容纳5人同时进餐
        int maxPermits = 5;
        // 假设有10个员工
        int employeeCnt = 10;

        Semaphore canteen = new Semaphore(maxPermits);

        ExecutorService executorService = Executors.newFixedThreadPool(employeeCnt);
        for (int i = 0; i < employeeCnt; i++) {
            executorService.submit(new EmployeeInCanteen("员工" + i, canteen));
        }
        executorService.shutdown();
    }
}
