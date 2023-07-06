package com.czhao.test.mutithread.juctools;

import java.util.concurrent.*;

/**
 * @author zhaochun
 */
public class FlowTest {
    public static void main(String[] args) throws InterruptedException {
        FlowTest me = new FlowTest();
        me.testSimple();
    }

    private void testSimple() throws InterruptedException {
        // 创建一个固定大小的线程池，用于为订阅者分配线程，一个订阅者只会分配一个线程
        ExecutorService executor = Executors.newFixedThreadPool(5);

        // 创建一个发布者，第一个参数是用于给订阅者分配线程的线程池对象，第二个参数是消息缓冲池的大小，也是一个订阅者一个消息缓冲池。
        SubmissionPublisher<String> publisher = new SubmissionPublisher<>(executor, 256);
        System.out.println("maxBufferCapacity:" + publisher.getMaxBufferCapacity());

        // 创建一个订阅者，用于接收和累加数据, 一个订阅者只会分配到一个线程执行
        Flow.Subscriber<String> subscriber = new Flow.Subscriber<>() {
            // 对订阅者自己的引用
            private Flow.Subscription subscription;
            // 统计接收了多少条消息
            private int sum = 0;

            @Override
            public void onSubscribe(Flow.Subscription subscription) {
                // 打印当前Thread ID
                System.out.println("【订阅者】注册订阅者, 当前线程ID:" + Thread.currentThread().getId());
                this.subscription = subscription;
                // 请求2个数据,即该订阅者线程只会从对应该订阅者的消息缓冲池中拉取两条消息。
                // 如果publisher提交了更多的消息，那么这些消息会在该订阅者对应的缓冲池中等待，不会被订阅者线程拉取出来。
                this.subscription.request(2);
            }

            @Override
            public void onNext(String item) {
                // 订阅者线程从消息缓冲池中拉取到2条消息后，就会按顺序串行调用onNext。
                System.out.println("【订阅者】接收数据:" + item + ", 当前线程ID:" + Thread.currentThread().getId());
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    throw new RuntimeException(e);
                }
                // 统计已接收到消息件数
                sum += 1;
                System.out.println("【订阅者】已接收到消息件数：" + sum);
                if (sum % 2 == 0) {
                    // 每消费两条消息，就再次发起消费请求,参数传2表示再接收2条消息,
                    // 这样堆积在缓冲池中的消息就会按顺序和这里的件数被订阅者线程拉取到。
                    // 这是一种背压机制，用于控制上游消息生产速度，不要超过下游消费消息的速度。
                    System.out.println("【订阅者】再次请求消费消息数: 2");
                    this.subscription.request(2);
                }
            }

            @Override
            public void onError(Throwable throwable) {
                // 出现异常，取消订阅
                System.out.println("【订阅者】数据接收出现异常，" + throwable + ", 当前线程ID:" + Thread.currentThread().getId());
                this.subscription.cancel();
            }

            @Override
            public void onComplete() {
                // 数据接收完毕，打印最终的合计值
                System.out.println("【订阅者】数据接收完毕，最终接收到消息件数：" + sum + ", 当前线程ID:" + Thread.currentThread().getId());
            }
        };

        // 添加订阅者
        System.out.println("【发布者】注册订阅者");
        publisher.subscribe(subscriber);

        new Thread(() -> {
            for (int i = 0; i < 10; i++) {
                int num = i + 1;
                System.out.println("【发布者】发送消息, num:" + num);
                int estimated = publisher.submit(Integer.toString(num));
                System.out.println("【发布者】发送消息 已堆积消息数量:" + estimated);
                // 中间等3秒再继续发送消息
                // 这样是为了验证: 订阅者线程在拉取不到消息时,会被线程池回收，之后再有消息发送到缓冲池时，订阅者会被分配另一个线程。
                // 因此才会出现以下现象: 即使不调用`publisher.close()`, 直接调用`executor.shutdown()`，一旦订阅者线程拉取不到消息,也会停止订阅。
                if (i == 4) {
                    try {
                        Thread.sleep(3000);
                    } catch (InterruptedException e) {
                        throw new RuntimeException(e);
                    }
                }
            }
        }).start();


        Thread.sleep(6000);
        System.out.println("【发布者】关闭发布者");
        // 关闭发布者
        publisher.close();
        System.out.println("关闭订阅者线程池");
        // 使用shutdown会等待所有堆积在缓冲池中的消息全部消费结束,一旦缓冲池中没有消息了，订阅者线程将被线程池回收，不会一直在拉取消息的地方阻塞。
        executor.shutdown();
        // 使用shutdownNow,如果订阅者线程进入WAITING状态则会直接退出，否则依然会将所有积压消息都消费掉后才能退出。
//        executor.shutdownNow();
        executor.awaitTermination(1, TimeUnit.HOURS);

        System.out.println("程序结束");
    }
}
