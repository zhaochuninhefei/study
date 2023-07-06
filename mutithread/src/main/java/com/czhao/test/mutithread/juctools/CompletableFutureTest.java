package com.czhao.test.mutithread.juctools;

import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutionException;

/**
 * @author zhaochun
 */
public class CompletableFutureTest {

    public static void main(String[] args) {
        CompletableFutureTest me = new CompletableFutureTest();
//        me.testSimple();
        me.testCombine();
    }

    private void testSimple(){
        CompletableFuture<String> completableFuture = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return "明月出天山，苍茫云海间。";
        });
        completableFuture.thenApply(s -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return s.concat("\n").concat("长风几万里，吹度玉门关。");
        }).thenApply(s -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return s.concat("\n").concat("汉下白登道，胡窥青海湾。");
        }).thenApply(s -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return s.concat("\n").concat("由来征战地，不见有人还。");
        }).thenApply(s -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return s.concat("\n").concat("戍客望边邑，思归多苦颜。");
        }).thenApply(s -> {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            return s.concat("\n").concat("高楼当此夜，叹息未应闲。");
        }).thenAccept(System.out::println);

        System.out.println("关山月 唐 李白");
        try {
            Thread.sleep(8000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("==================");
    }

    private void testCombine() {
        CompletableFuture<Double> futurePrice = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep((long) (Math.random() * 1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            double price = Math.random() * 100;
            System.out.println("Price is " + price);
            return price;
        });
        CompletableFuture<Integer> futureCount = CompletableFuture.supplyAsync(() -> {
            try {
                Thread.sleep((long) (Math.random() * 1000));
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            int count = (int) (Math.random() * 100);
            System.out.println("Count is " + count);
            return count;
        });
        CompletableFuture<Double> futureTotal = futurePrice.thenCombine(futureCount, (price, count) -> price * count);
        futureTotal.thenAccept(total -> System.out.println("Total is " + total));


        System.out.println("鬼知道要多久。。。该干嘛干嘛去。。。");
//        try {
//            Thread.sleep(3000);
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
        try {
            System.out.println("主线程等待futureTotal执行结果, Total: " + futureTotal.get());
        } catch (InterruptedException | ExecutionException e) {
            throw new RuntimeException(e);
        }
    }
}
