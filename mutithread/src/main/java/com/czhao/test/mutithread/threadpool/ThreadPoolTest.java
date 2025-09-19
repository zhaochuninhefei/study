package com.czhao.test.mutithread.threadpool;

import com.czhao.test.mutithread.ThreadUtil;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;

/**
 * @author zhaochun
 */
public class ThreadPoolTest {
    public static void main(String[] args) {
        ThreadPoolTest me = new ThreadPoolTest();
//        me.testThreadPoolExecutorByFuture();

        int taskCnt = 1000;
        int defaultThreadCnt = Runtime.getRuntime().availableProcessors();
        System.out.println("可用处理器数目：" + defaultThreadCnt);
        me.testCachedThreadPool(taskCnt);
        me.testFixedThreadPool(taskCnt, defaultThreadCnt * 2 + 1);
        me.testSingleThreadExecutor(taskCnt);
        me.testWorkStealingPool(taskCnt, defaultThreadCnt);
    }

    private void testThreadPoolExecutorByFuture() {
        List<Future<Integer>> futures = new ArrayList<>();
        ExecutorService executorService = new ThreadPoolExecutor(5, 10, 60, TimeUnit.SECONDS, new LinkedBlockingQueue<>());
        for (int i = 0; i < 100; i++) {
            // 一般使用submit一个一个异步提交任务而不使用invokeAll一把提交所有任务，因为invokeAll会阻塞当前线程直到所有线程都执行结束。
            Future<Integer> future = executorService.submit(new RandomIntInTen());
            futures.add(future);
        }
        executorService.shutdown();
        try {
            // 一般采用遍历Future集合一个一个get的方式等待所有线程执行结束并获得执行结果
            for (Future<Integer> future : futures) {
                Integer number = future.get();
                System.out.println(number);
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    private void testCachedThreadPool(int taskCnt) {
        testExecutorWithFutureTask(ExecutorType.CachedThreadPool, taskCnt, -1);
    }

    private void testFixedThreadPool(int taskCnt, int threadCnt) {
        testExecutorWithFutureTask(ExecutorType.FixedThreadPool, taskCnt, threadCnt);
    }

    private void testSingleThreadExecutor(int taskCnt) {
        testExecutorWithFutureTask(ExecutorType.SingleThreadExecutor, taskCnt, -1);
    }

    private void testWorkStealingPool(int taskCnt, int threadCnt) {
        testExecutorWithFutureTask(ExecutorType.WorkStealingPool, taskCnt, threadCnt);
    }

    private void testExecutorWithFutureTask(ExecutorType executorType, int taskCnt, int threadCnt) {
        LocalDateTime startTime = LocalDateTime.now();
        List<FutureTask<Integer>> futureTasks = new ArrayList<>();
        ExecutorService executorService = getExecutorService(executorType, threadCnt);
        for (int i = 0; i < taskCnt; i++) {
            FutureTask<Integer> integerFutureTask = new FutureTask<>(new RandomIntInTen());
            futureTasks.add(integerFutureTask);
            executorService.submit(integerFutureTask);
        }
        executorService.shutdown();
        try {
            for (FutureTask<Integer> integerFutureTask : futureTasks) {
                Integer number = integerFutureTask.get();
            }
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
        LocalDateTime stopTime = LocalDateTime.now();
        System.out.println(executorType.getName() + " 耗时(ms):" + Duration.between(startTime, stopTime).toMillis());
    }

    static class RandomIntInTen implements Callable<Integer> {
        @Override
        public Integer call() {
            int randomNumber = new Random().nextInt(10) + 1;
            // 模拟CPU密集
            int sum = 0;
            for (int i = 0; i < 1000000; i++) {
                int mod = i % 2;
                sum += mod;
            }
            // 模拟IO密集
            ThreadUtil.writeRandomNumberFile(randomNumber, Thread.currentThread().getName());
            // 模拟不稳定的耗时起伏
//            try {
//                TimeUnit.MILLISECONDS.sleep(new Random().nextInt(100));
//            } catch (InterruptedException e) {
//                e.printStackTrace();
//            }
            return randomNumber;
        }
    }

    private static ExecutorService getExecutorService(ExecutorType executorType, int threadCnt) {
        switch (executorType) {
            case CachedThreadPool:
                return Executors.newCachedThreadPool();
            case FixedThreadPool:
                return Executors.newFixedThreadPool(threadCnt);
            case SingleThreadExecutor:
                return Executors.newSingleThreadExecutor();
            case WorkStealingPool:
                return Executors.newWorkStealingPool(threadCnt);
            default:
                throw new RuntimeException("传的啥乱七八糟的executorType。。。");
        }
    }

    enum ExecutorType {
        CachedThreadPool("CachedThreadPool"),
        FixedThreadPool("FixedThreadPool"),
        SingleThreadExecutor("SingleThreadExecutor"),
        WorkStealingPool("WorkStealingPool");

        private final String name;

        ExecutorType(String name) {
            this.name = name;
        }

        public String getName() {
            return name;
        }
    }
}
