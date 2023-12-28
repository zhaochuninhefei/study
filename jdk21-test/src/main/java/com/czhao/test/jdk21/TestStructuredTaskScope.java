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
        try (var scope = new StructuredTaskScope.ShutdownOnFailure()) {
            Supplier<String> user  = scope.fork(this::findUser);
            Supplier<Integer> order = scope.fork(this::fetchOrder);

            scope.join()            // 加入两个子任务
                    .throwIfFailed();  // ... 并传播错误

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
