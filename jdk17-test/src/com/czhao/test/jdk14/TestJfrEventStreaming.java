package com.czhao.test.jdk14;

import jdk.jfr.consumer.RecordingStream;

import java.time.Duration;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.*;

/**
 * JEP 349: JFR Event Streaming
 *
 * @author zhaochun
 */
public class TestJfrEventStreaming {
    public static void main(String[] args) {
        TestJfrEventStreaming me = new TestJfrEventStreaming();
        // 启动JFR监听线程
        me.runJfrMonitor();
        // 启动争锁任务线程池
        me.runLock();
    }

    /**
     * 启动JFR监听线程
     */
    private void runJfrMonitor() {
        // 定义JFR监听线程
        Thread monitorThread = new Thread(() -> {
            try (var rs = new RecordingStream()) {
                // 设定每秒收集一次cpu使用率, 即每秒触发一次 jdk.CPULoad 事件
                rs.enable("jdk.CPULoad").withPeriod(Duration.ofSeconds(1));
                // 设定收集锁争用时间超过10毫秒的锁信息, 即每当有一个线程执行synchronized锁的monitorenter指令超过10ms时，就触发一次 jdk.JavaMonitorEnter 事件。
                rs.enable("jdk.JavaMonitorEnter").withThreshold(Duration.ofMillis(10));
                // 实现 jdk.CPULoad 的事件接收处理
                rs.onEvent("jdk.CPULoad", event -> System.out.printf("jdk.CPULoad: %f%n", event.getFloat("machineTotal")));
                // 实现 jdk.JavaMonitorEnter 的事件接收处理
                rs.onEvent("jdk.JavaMonitorEnter", event -> System.out.printf("jdk.JavaMonitorEnter: %s%n", event.getClass("monitorClass")));
                rs.start();
            }
        });
        // 设置JFR监听线程为守护线程
        monitorThread.setDaemon(true);
        // 启动JFR监听线程
        monitorThread.start();
    }

    /**
     * 启动争锁任务线程池
     */
    private void runLock() {
        // 目标锁对象
        MyLock lock = new MyLock();
        // 定义一个20线程的线程池
        ExecutorService executor = Executors.newFixedThreadPool(20);
        List<Future<Boolean>> futures = new ArrayList<>();
        // 提交100个争锁任务
        for (int i = 0; i < 100; i++) {
            String taskName = String.format("task%03d", i + 1);
            futures.add(executor.submit(() -> {
                // 实现争锁任务
                synchronized (lock) {
                    System.out.println(taskName + " 开始执行...");
                    try {
                        Thread.sleep(500);
                    } catch (InterruptedException e) {
                        Thread.currentThread().interrupt();
                    }
                    System.out.println(taskName + " 执行结束...");
                }
                return true;
            }));
        }
        // 关闭线程池，不再接收新任务
        executor.shutdown();
        System.out.println("不再接收新的任务。");
        try {
            // 主线程等待线程池中的任务全部执行结束
            for (Future<Boolean> future : futures) {
                future.get();
            }
            System.out.println("所有任务执行结束。");
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        // 结束主线程
        System.exit(0);
    }

    static class MyLock {
    }
}
