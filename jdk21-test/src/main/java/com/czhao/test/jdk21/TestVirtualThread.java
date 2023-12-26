package com.czhao.test.jdk21;

import com.czhao.test.jdbc.JDBCTester;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;

/**
 * @author zhaochun
 */
public class TestVirtualThread {
    public static void main(String[] args) {
        TestVirtualThread me = new TestVirtualThread();
        me.test01();
//        me.testReadDbByPlatformThread();
//        me.testReadDbByVirtualThread();
    }

    private void test01() {
        Thread v1 = Thread.ofVirtual().name("v1").unstarted(() -> System.out.println("run v1..."));
        v1.start();
        try {
            v1.join();
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("v1 over...");
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
