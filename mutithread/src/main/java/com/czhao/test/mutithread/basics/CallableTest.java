package com.czhao.test.mutithread.basics;

import java.time.LocalDateTime;
import java.util.concurrent.*;

/**
 * @author zhaochun
 */
public class CallableTest {
    public static void main(String[] args) {
        CallableTest me = new CallableTest();
        me.testFutureTask();
        me.testFuture();
    }

    private void testFuture() {
        // Callable实现类不能直接作为Thread构造参数传入，这里使用线程池来提交一个Callable任务
        ExecutorService executor = Executors.newSingleThreadExecutor();
        // 通过submit方法向线程池提交Callable任务，submit方法返回的是Future对象
        Future<LocalDateTime> future = executor.submit(new Printer());
        // 线程池不再接收新的任务
        executor.shutdown();
        System.out.println("Print in mainThread.");
        try {
            // future.get()是获取子线程的运行结果，如果子线程此时尚未运行结束，则主线程在该步骤会等待直到子线程结束返回结果
            System.out.println(future.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    private void testFutureTask() {
        // Callable实现类不能直接作为Thread构造参数传入，而是需要包装一层FutureTask将其转为Runnable接口
        FutureTask<LocalDateTime> futureTask = new FutureTask<>(new Printer());
        Thread subThread = new Thread(futureTask);
        subThread.start();
        System.out.println("Print in mainThread.");
        try {
            // 在主线程中获取子线程执行结束后返回的结果，这里是LocalDateTime类型的时间戳。
            // 要注意的是，如果子线程此时尚未运行结束，则主线程执行futureTask.get()时会等待，一直到子线程结束返回结果。
            System.out.println(futureTask.get());
        } catch (InterruptedException | ExecutionException e) {
            e.printStackTrace();
        }
    }

    // 定义一个实现了 Callable 接口的类，并指定返回值类型
    class Printer implements Callable<LocalDateTime> {
        // 实现 call 方法，并返回指定类型的值
        @Override
        public LocalDateTime call() throws Exception {
            System.out.println("Print in Callable.");
            return LocalDateTime.now();
        }
    }
}
