package com.czhao.test.mutithread.juctools;

import java.util.Random;
import java.util.concurrent.BrokenBarrierException;
import java.util.concurrent.CyclicBarrier;
import java.util.concurrent.TimeUnit;

/**
 * @author zhaochun
 */
public class CyclicBarrierTest {
    public static void main(String[] args) {
        CyclicBarrierTest me = new CyclicBarrierTest();
        me.testWorker();
    }

    private void testWorker() {
        class BookReader implements Runnable {
            private final String name;
            private final CyclicBarrier barrier;

            public BookReader(String name, CyclicBarrier barrier) {
                this.name = name;
                this.barrier = barrier;
            }

            @Override
            public void run() {
                try {
                    // 假设书只有4页
                    for (int i = 0; i < 4; i++) {
                        System.out.println(String.format("%s 开始阅读第 %s 页。。。", name, i + 1));
                        TimeUnit.SECONDS.sleep(new Random().nextInt(5));
                        System.out.println(String.format("%s 阅读第 %s 页结束。", name, i + 1));
                        // 等所有人都阅读完这一页
                        // 返回0代表最后一个到达此处的线程
                        if (barrier.await() == 0) {
                            // 所有人都阅读完这一页后，栅栏重置
                            barrier.reset();
                        }
                    }
                } catch (InterruptedException | BrokenBarrierException e) {
                    e.printStackTrace();
                }
            }
        }

        // 假设有3个人读这本书
        int readerCount = 3;

        // CyclicBarrier构造参数传入的数量应该等于需要被栅栏同步的线程数量
        CyclicBarrier barrier = new CyclicBarrier(readerCount, () -> {
            // 每次所有人读完一页都会调用这个方法
            System.out.println("========== 所有人都读完这一页了 ==========");
        });

        new Thread(new BookReader("张三", barrier)).start();
        new Thread(new BookReader("李四", barrier)).start();
        new Thread(new BookReader("王五", barrier)).start();
    }
}
