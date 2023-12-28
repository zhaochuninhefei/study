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
            var res = handle();
            System.out.println("handle result: " + res);
        } catch (ExecutionException | InterruptedException e) {
            throw new RuntimeException(e);
        }
    }

    private record Response(String user, Integer order) {
    }

    private Response handle() throws ExecutionException, InterruptedException {
        // 定义一个 结构化任务区域 StructuredTaskScope, 并指定关闭策略为 ShutdownOnFailure
        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
            // 向区域添加一个子任务分支
            Supplier<String> user = scope.fork(this::findUser);
            // 向区域添加另一个子任务分支
            Supplier<Integer> order = scope.fork(this::fetchOrder);

            // join 加入两个子任务
            scope.join()
                    // 传播错误
                    .throwIfFailed();

            // 在这里，两个子任务都成功，因此组合它们的结果
            return new Response(user.get(), order.get());
        }
    }

    private String findUser() {
        return "tester";
    }

    private Integer fetchOrder() {
        return 1;
    }
}
