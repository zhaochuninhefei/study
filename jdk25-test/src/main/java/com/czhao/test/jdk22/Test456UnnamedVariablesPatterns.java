package com.czhao.test.jdk22;

import java.util.ArrayList;
import java.util.List;

@SuppressWarnings({"java:S106","java:S2094"})
public class Test456UnnamedVariablesPatterns {
    public static void main(String[] args) {
        Test456UnnamedVariablesPatterns test = new Test456UnnamedVariablesPatterns();
        test.test01();
    }

    private void test01(){
        try {
            List<Order>  orders = new ArrayList<>();
            for (int i = 0; i < 10; i++) {
                Order order = new Order();
                orders.add(order);
            }
            var _ = count(orders);
            var num = count(orders);
            System.out.println(num);

        } catch (Exception _) {
            System.out.println("虽然捕获到了Exception,但就是不用这个异常实例");
        }
    }

    private int count(Iterable<Order> orders) {
        int total = 0;
        for (var _ : orders)    // Unnamed variable
            total++;
        return total;
    }

    private static class Order {
    }
}
