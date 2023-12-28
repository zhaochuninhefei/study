package com.czhao.test.jdk21;

import java.util.concurrent.ExecutionException;
import java.util.concurrent.StructuredTaskScope;
import java.util.function.Supplier;

/**
 * @author zhaochun
 */
public class TestStructuredTaskScope {
    public static void main(String[] args) {
        var me = new TestStructuredTaskScope();
        me.test01();
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
}
