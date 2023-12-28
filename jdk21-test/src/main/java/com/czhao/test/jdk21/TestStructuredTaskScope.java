package com.czhao.test.jdk21;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.Random;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.StructuredTaskScope;
import java.util.concurrent.TimeoutException;
import java.util.function.Supplier;

/**
 * @author zhaochun
 */
public class TestStructuredTaskScope {
    public static void main(String[] args) {
        var me = new TestStructuredTaskScope();
        me.test01();
        me.test02();
        me.test03();
        // 打印主线程是否是虚拟线程
        System.out.println("main thread isVirtual: " + Thread.currentThread().isVirtual());
    }

    private void test01() {
        try {
            var res = queryUserOrder();
            System.out.println("queryUserOrder result: " + res);
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private record Response(String user, Integer order) {
    }

    private Response queryUserOrder() throws ExecutionException, InterruptedException {
        // 定义一个 结构化任务的作用域 StructuredTaskScope, 并指定关闭策略为 ShutdownOnFailure
        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
            // 在作用域上创建一个分支, 传入子任务定义(lambda)，即分派一个子任务
            Supplier<String> user = scope.fork(this::findUser);
            // 在作用域上创建另一个分支, 传入子任务定义(lambda)，即分派一另个子任务
            Supplier<Integer> order = scope.fork(this::fetchOrder);

            // join 将已经分派的子任务加入作用域
            scope.join()
                    // 传播错误
                    .throwIfFailed();

            // 在这里，两个子任务都成功，因此组合它们的结果
            return new Response(user.get(), order.get());
        }
    }

    private String findUser() {
        // 打印当前线程信息
        System.out.println("findUser current thread: " + Thread.currentThread().threadId() + " isVirtual: " + Thread.currentThread().isVirtual());
        // 假设这里从表1读取用户名
        return "tester";
    }

    private Integer fetchOrder() {
        // 打印当前线程信息
        System.out.println("fetchOrder current thread: " + Thread.currentThread().threadId() + " isVirtual: " + Thread.currentThread().isVirtual());
        // 假设这里从表2读取订单ID
        return 1;
    }

    private void test02() {
        var res = sum();
        System.out.println("sum result: " + res);
    }

    private int sum() {
        // 创建结构化任务的作用域，并指定关闭策略为 ShutdownOnSuccess, 即只要有任何一个执行成功即尝试关闭所有其他子任务。
        // 但如果某个子任务一直占用cpu，不会陷入阻塞，那么shutdown依然无法强制让该子任务线程中止，无论这个子任务的线程是不是虚拟线程。
        // 因此对于没有IO阻塞的子任务，一定要在实现该子任务时添加 interrupt 处理，例如下面的 sumFour
        try (var scope = new StructuredTaskScope.ShutdownOnSuccess<Integer>()) {
            // 分派不同的sum子任务
            scope.fork(this::sumOne);
            scope.fork(this::sumTwo);
            scope.fork(this::sumThree);
            // 分派一个不会阻塞的子任务
            scope.fork(this::sumFour);
            // 加入所有子任务, 指定超时时间
            return scope.joinUntil(Instant.now().plus(1000, ChronoUnit.MILLIS)).result();
        } catch (InterruptedException | ExecutionException | TimeoutException e) {
            throw new RuntimeException(e);
        }
    }

    private int sumOne() {
        var threadID = Thread.currentThread().threadId();
        var sleepMillis = new Random().nextInt(1000);
        System.out.println("sumOne sleepMillis:" + sleepMillis + " ThreadId:" + threadID);
        try {
            Thread.sleep(sleepMillis);
        } catch (InterruptedException e) {
            System.out.println(threadID + " interrupted");
            throw new RuntimeException(e);
        }
        System.out.println("sumOne end ThreadId:" + threadID);
        return 1;
    }

    private int sumTwo() {
        var threadID = Thread.currentThread().threadId();
        var sleepMillis = new Random().nextInt(1000);
        System.out.println("sumTwo sleepMillis:" + sleepMillis + " ThreadId:" + threadID);
        try {
            Thread.sleep(sleepMillis);
        } catch (InterruptedException e) {
            System.out.println(threadID + " interrupted");
            throw new RuntimeException(e);
        }
        System.out.println("sumTwo end ThreadId:" + threadID);
        return 2;
    }

    private int sumThree() {
        var threadID = Thread.currentThread().threadId();
        var sleepMillis = new Random().nextInt(1000);
        System.out.println("sumThree sleepMillis:" + sleepMillis + " ThreadId:" + threadID);
        try {
            Thread.sleep(sleepMillis);
        } catch (InterruptedException e) {
            System.out.println(threadID + " interrupted");
            throw new RuntimeException(e);
        }
        System.out.println("sumThree end ThreadId:" + threadID);
        return 3;
    }

    private int sumFour() {
        var threadID = Thread.currentThread().threadId();
        System.out.println("sumFour ThreadId:" + threadID);
        var sum = 0;
        for (; ; ) {
            // 判断当前线程是否接收到 interrupt 信号
            if (Thread.currentThread().isInterrupted()) {
                System.out.println("sumFour interrupted ThreadId:" + threadID);
                break;
            }
            System.out.println("sumFour still running...");
            sum++;
        }
        System.out.println("sumFour end ThreadId:" + threadID);
        return sum;
    }

    final static ScopedValue<TestScopedValue.MyRecord> RECORD = ScopedValue.newInstance();

    private void test03() {
        ScopedValue.where(RECORD, new TestScopedValue.MyRecord(1, 2))
                .run(() -> {
                    try {
                        var res = handle();
                        System.out.println(res);
                    } catch (ExecutionException | InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                });
    }

    private Response handle() throws ExecutionException, InterruptedException {
        // 定义一个 结构化任务的作用域 StructuredTaskScope, 并指定关闭策略为 ShutdownOnFailure
        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
            // 在作用域上创建一个分支, 传入子任务定义(lambda)，即分派一个子任务
            Supplier<String> user = scope.fork(this::findUserWithScope);
            // 在作用域上创建另一个分支, 传入子任务定义(lambda)，即分派一另个子任务
            Supplier<Integer> order = scope.fork(this::fetchOrderWithScope);

            // join 将已经分派的子任务加入作用域
            scope.join()
                    // 传播错误
                    .throwIfFailed();

            // 在这里，两个子任务都成功，因此组合它们的结果
            return new Response(user.get(), order.get());
        }
    }

    private String findUserWithScope() {
        // 打印当前线程信息
        System.out.println("findUserWithScope current thread: " + Thread.currentThread().threadId() + " isVirtual: " + Thread.currentThread().isVirtual());
        // 从 RECORD 获取信息并打印
        System.out.println("findUserWithScope RECORD: " + RECORD.get());
        // 假设这里从表1读取用户名
        return "tester";
    }

    private Integer fetchOrderWithScope() {
        // 打印当前线程信息
        System.out.println("fetchOrderWithScope current thread: " + Thread.currentThread().threadId() + " isVirtual: " + Thread.currentThread().isVirtual());
        // 从 RECORD 获取信息并打印
        System.out.println("fetchOrderWithScope RECORD: " + RECORD.get());
        // 假设这里从表2读取订单ID
        return 1;
    }
}
