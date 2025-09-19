package com.czhao.test.mutithread.juctools;

import java.util.Random;
import java.util.concurrent.CountDownLatch;

/**
 * @author zhaochun
 */
public class CountDownLatchTest {
    public static void main(String[] args) {
        CountDownLatchTest me = new CountDownLatchTest();
        me.testMeeting();
    }

    private void testMeeting() {
        class Member implements Runnable {
            private final String name;
            private final CountDownLatch countDownLatch;

            public Member(String name, CountDownLatch countDownLatch) {
                this.name = name;
                this.countDownLatch = countDownLatch;
            }

            @Override
            public void run() {
                System.out.println(String.format("%s 开始前往会议室...", name));
                Random random = new Random();
                try {
                    Thread.sleep(random.nextInt(5000) + 2000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(String.format("%s 到达会议室...", name));
                // 每有一个与会人员到达会议室，计数就减一
                countDownLatch.countDown();
            }
        }
        // CountDownLatch构造参数应等于需要被门闩同步的线程数量
        // 如果比需要被门闩同步的线程数量小，那么就会提前触发meeting线程继续执行
        // 如果比需要被门闩同步的线程数量大，那就永远无法开会了
        CountDownLatch countDownLatch = new CountDownLatch(3);

        Thread meeting = new Thread(() -> {
            System.out.println("发出会议通知，等待与会人员前来会议室...");
            try {
                // 发出通知的人要等待各位与会人员到来
                countDownLatch.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println("与会人员全部到达会议室，会议开始...");
            System.out.println("吧啦吧啦...");
        });
        meeting.start();

        Thread t1 = new Thread(new Member("张三", countDownLatch));
        Thread t2 = new Thread(new Member("李四", countDownLatch));
        Thread t3 = new Thread(new Member("王五", countDownLatch));

        t1.start();
        t2.start();
        t3.start();
    }
}
