package com.czhao.test.jdk21;

import com.czhao.test.jdbc.JDBCTester;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

/**
 * @author zhaochun
 */
@SuppressWarnings({"CallToPrintStackTrace", "StringTemplateMigration"})
public class TestVirtualThread {
    public static void main(String[] args) {
        TestVirtualThread me = new TestVirtualThread();
        me.test01();
        me.test02();
        me.test03();
        me.test04();
        me.test05();
        me.test06();
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
            TimeUnit.MILLISECONDS.sleep(1000);
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

    private void test03() {
        // 不要同时使用 try-with-resource 和 Executors.newSingleThreadScheduledExecutor()
        // 比如下面的例子，如果不在 scheduleWithFixedDelay 之后，try-with-resource 花括弧`}`之前 sleep一秒的话，
        // 那么在计划器延迟启动任务之前，就会执行 try-with-resource 对 executor 的 close，该方法会尝试关闭 executor，
        // 而无论关闭的时间点是在计划器首次执行任务之前，还是在两次执行任务的中间，executor都会被关闭而导致计划任务不再执行。
        try (var executor = Executors.newSingleThreadScheduledExecutor(Thread.ofVirtual().factory())) {
            executor.scheduleWithFixedDelay(() -> {
                System.out.println(STR."\{Thread.currentThread().threadId()} isVirtual:\{Thread.currentThread().isVirtual()}");
                System.out.println("test03 executor.scheduleWithFixedDelay...");
            }, 1, 1, TimeUnit.SECONDS);
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("test03 继续...");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("test03 结束...");
    }

    private void test04() {
        try (var executor = Executors.newSingleThreadScheduledExecutor()) {
            executor.scheduleWithFixedDelay(() -> {
                System.out.println(STR."\{Thread.currentThread().threadId()} isVirtual:\{Thread.currentThread().isVirtual()}");
                System.out.println("test04 executor.scheduleWithFixedDelay...");
            }, 1, 1, TimeUnit.SECONDS);
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("test04 继续...");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("test04 结束...");
    }

    private void test05() {
        // schedule的话，try-with-resource 会在花括弧结束后等待计划任务执行
        try (var executor = Executors.newSingleThreadScheduledExecutor()) {
            executor.schedule(() -> {
                System.out.println(STR."\{Thread.currentThread().threadId()} isVirtual:\{Thread.currentThread().isVirtual()}");
                System.out.println("test05 executor.scheduleWithFixedDelay...");
            }, 3, TimeUnit.SECONDS);
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("test05 继续...");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("test05 结束...");
    }

    private void test06() {
        @SuppressWarnings("resource")
        var executor = Executors.newSingleThreadScheduledExecutor(Thread.ofVirtual().factory());
        executor.scheduleWithFixedDelay(() -> {
            System.out.println(STR."\{Thread.currentThread().threadId()} isVirtual:\{Thread.currentThread().isVirtual()}");
            System.out.println("test06 executor.scheduleWithFixedDelay...");
        }, 1, 1, TimeUnit.SECONDS);
        System.out.println("test06 继续...");
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            throw new RuntimeException(e);
        }
        System.out.println("test06 结束...");
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
