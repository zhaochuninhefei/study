package com.czhao.test.jdk21;

import com.czhao.test.jdbc.JDBCTester;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

import static java.util.concurrent.TimeUnit.MILLISECONDS;

/**
 * @author zhaochun
 */
@SuppressWarnings("CallToPrintStackTrace")
public class TestVirtualThread {
    public static void main(String[] args) {
        TestVirtualThread me = new TestVirtualThread();
        me.test01();
        me.test02();
        me.testReadDbByPlatformThread();
        me.testReadDbByVirtualThread();
    }

    private void test01() {
        Thread v1 = Thread.ofVirtual().name("v1").unstarted(() -> System.out.println("run v1, threadID:" + Thread.currentThread().threadId()));
        v1.start();

        Thread.startVirtualThread(() -> {
            System.out.println("run v2, threadID:" + Thread.currentThread().threadId());
            System.out.println("Is v2 virtual thread : " + Thread.currentThread().isVirtual());
        });

        Thread.Builder builder = Thread.ofVirtual().name("v3-", 0);
        Runnable task = () -> {
            var threadName = Thread.currentThread().getName();
            System.out.println(threadName + " run, threadID:" + Thread.currentThread().threadId());
            System.out.println("Is " + threadName + " virtual thread : " + Thread.currentThread().isVirtual());
        };
        Thread v3_0 = builder.start(task);
        Thread v3_1 = builder.start(task);

        Thread.getAllStackTraces().keySet().forEach(System.out::println);

        try {
            v1.join();
            v3_0.join();
            v3_1.join();
            MILLISECONDS.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        System.out.println("Main over...");
    }

    private void test02() {
        LocalDateTime startTime = LocalDateTime.now();
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            List<Future<Boolean>> futures = new ArrayList<>();
            // 创建100万个虚拟线程
            for (int i = 0; i < 1_000_000; i++) {
                futures.add(executor.submit(() -> {
                    // 打印当前线程信息
                    System.out.println(Thread.currentThread().threadId() + " isVirtual:" + Thread.currentThread().isVirtual());
                    Thread.sleep(1000);
                    return true;
                }));
            }
            futures.forEach(f -> {
                try {
                    f.get();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            executor.shutdown();
        }
        LocalDateTime endTime = LocalDateTime.now();
        // 计算耗时
        System.out.println("耗时:" + Duration.between(startTime, endTime).toMillis() + "毫秒");
    }

    private static final int CNT_THREADS = 300;

    private void testReadDbByPlatformThread() {
        LocalDateTime startTime = LocalDateTime.now();
        try (var executor = Executors.newFixedThreadPool(CNT_THREADS)) {
            List<Future<Boolean>> futures = new ArrayList<>();
            for (int i = 0; i < CNT_THREADS; i++) {
                futures.add(executor.submit(() -> {
                    JDBCTester test = new JDBCTester();
                    test.queryBySql();
                    return true;
                }));
            }
            futures.forEach(f -> {
                try {
                    f.get();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            executor.shutdown();
        }
        LocalDateTime endTime = LocalDateTime.now();
        // 计算耗时
        System.out.println("耗时:" + Duration.between(startTime, endTime).toMillis() + "毫秒");
    }

    private void testReadDbByVirtualThread() {
        LocalDateTime startTime = LocalDateTime.now();
        try (var executor = Executors.newVirtualThreadPerTaskExecutor()) {
            List<Future<Boolean>> futures = new ArrayList<>();
            for (int i = 0; i < CNT_THREADS; i++) {
                futures.add(executor.submit(() -> {
                    JDBCTester test = new JDBCTester();
                    test.queryBySql();
                    return true;
                }));
            }
            futures.forEach(f -> {
                try {
                    f.get();
                } catch (Exception e) {
                    e.printStackTrace();
                }
            });
            executor.shutdown();
        }
        LocalDateTime endTime = LocalDateTime.now();
        // 计算耗时
        System.out.println("耗时:" + Duration.between(startTime, endTime).toMillis() + "毫秒");
    }
}
