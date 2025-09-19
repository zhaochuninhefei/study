package java8;

import java.util.concurrent.CompletableFuture;

/**
 * @author zhaochun
 */
public class TestCase07CompletableFuture {
    public static void main(String[] args) {
        TestCase07CompletableFuture me = new TestCase07CompletableFuture();
        me.test01_simple();
        me.test02_thenCombine();
    }

    private void test01_simple() {
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

    private void test02_thenCombine() {
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
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
