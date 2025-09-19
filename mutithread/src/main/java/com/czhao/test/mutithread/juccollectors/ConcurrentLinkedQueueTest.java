package com.czhao.test.mutithread.juccollectors;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author zhaochun
 */
public class ConcurrentLinkedQueueTest {
    public static void main(String[] args) {
        ConcurrentLinkedQueueTest me = new ConcurrentLinkedQueueTest();
        me.testMsgQueue(100, 1000);

//        int dataSize = 100000;
//        int threadCnt = 1000;
//        me.testBlockingConsumer(dataSize, threadCnt);
//        me.testNoBlockingConsumer(dataSize, threadCnt);
    }

    private static volatile boolean stop = false;

    private void testMsgQueue(int producerSize, int consumerSize) {
        class MsgConsumer {
            private ConcurrentLinkedQueue<String> queue;

            private String name;
            private int consumedMsgCnt;

            public MsgConsumer(ConcurrentLinkedQueue<String> queue, String name) {
                this.queue = queue;
                this.name = name;
            }

            public void consume() {
                while (true) {
                    if (stop) break;
                    String msg = queue.poll();
                    if (msg == null) {
                        if (stop) break;
                        continue;
                    }
                    consumedMsgCnt++;
                    System.out.println(String.format("消费者 %s 消费一个消息:[%s]，截至目前已经消费 %s 个消息。", name, msg, consumedMsgCnt));
                    if (stop) break;
                    try {
                        TimeUnit.MILLISECONDS.sleep(20);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            public int getConsumedMsgCnt() {
                return consumedMsgCnt;
            }
        }

        class MsgProducer {
            private ConcurrentLinkedQueue<String> queue;

            private String name;
            private int producedMsgCnt;

            public MsgProducer(ConcurrentLinkedQueue<String> queue, String name) {
                this.queue = queue;
                this.name = name;
            }

            public void produce() {
                while (true) {
                    if (stop) break;
                    String msg = "你好啊";
                    queue.offer(msg);
                    producedMsgCnt++;
                    System.out.println(String.format("生产者 %s 生产了一个消息:[%s]，截至目前已生产 %s 个消息", name, msg, producedMsgCnt));
                    if (stop) break;
                    try {
                        TimeUnit.MILLISECONDS.sleep(20);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }

            public int getProducedMsgCnt() {
                return producedMsgCnt;
            }
        }

        ConcurrentLinkedQueue<String> queue = new ConcurrentLinkedQueue<>();

        List<MsgProducer> producers = Stream.iterate(1, x -> x + 1).limit(producerSize)
                .map(num -> new MsgProducer(queue, String.format("%2s", num)))
                .collect(Collectors.toList());

        List<MsgConsumer> consumers = Stream.iterate(1, x -> x + 1).limit(consumerSize)
                .map(num -> new MsgConsumer(queue, String.format("%2s", num)))
                .collect(Collectors.toList());

        LocalDateTime startTime = LocalDateTime.now();
        LocalDateTime stopTime = startTime;
        ExecutorService executorProduce = Executors.newFixedThreadPool(producerSize);
        producers.forEach(msgProducer -> executorProduce.submit(msgProducer::produce));
        executorProduce.shutdown();
        ExecutorService executorConsumer = Executors.newFixedThreadPool(consumerSize);
        consumers.forEach(msgConsumer -> executorConsumer.submit(msgConsumer::consume));
        executorConsumer.shutdown();

        try {
            TimeUnit.SECONDS.sleep(10);
            stop = true;
            System.out.println("不再生产或消费消息...");
            stopTime = LocalDateTime.now();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        int msgProducedTotal = producers.stream()
                .mapToInt(MsgProducer::getProducedMsgCnt).sum();
        int msgConsumedTotal = consumers.stream()
                .mapToInt(MsgConsumer::getConsumedMsgCnt).sum();
        int percent = msgConsumedTotal * 100 / msgProducedTotal;
        long seconds = Duration.between(startTime, stopTime).getSeconds();
        int pps = (int) (msgProducedTotal / seconds);
        int cps = (int) (msgConsumedTotal / seconds);
        System.out.println(String.format("耗时 %s 秒， 共生产消息 %s 个，消费消息 %s 个，消费生产百分比： %s， 每秒生产消息: %s , 每秒消费消息 : %s",
                seconds, msgProducedTotal, msgConsumedTotal, percent, pps, cps));
    }


    private void testBlockingConsumer(int dataSize, int threadCnt) {
        class BlockingConsumer implements Runnable {
            private final BlockingQueue<Integer> queue;
            private final CountDownLatch countDownLatch;

            BlockingConsumer(BlockingQueue<Integer> queue, CountDownLatch countDownLatch) {
                this.queue = queue;
                this.countDownLatch = countDownLatch;
            }

            @Override
            public void run() {
                try {
                    // 不停消费队列中的数据
                    for (; ; ) {
                        Integer number = queue.poll();
                        // 拉取不到时退出
                        if (number == null) {
                            return;
                        }
//                        // 模拟消费过程
//                        TimeUnit.MILLISECONDS.sleep(20);
                    }
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
                } finally {
                    // 退出时递减门闩减一
                    countDownLatch.countDown();
                }
            }
        }
        // 准备数据
        List<Integer> array = makeTestData(dataSize);
        // 直接将数据全部扔进 ArrayBlockingQueue
//        ArrayBlockingQueue<Integer> queue = new ArrayBlockingQueue<>(array.size(), false, array);
        LinkedBlockingQueue<Integer> queue = new LinkedBlockingQueue<>(array);
        // 准备消费者
        CountDownLatch countDownLatch = new CountDownLatch(threadCnt);
        ExecutorService pool = Executors.newFixedThreadPool(threadCnt);
        LocalDateTime startTime = LocalDateTime.now();
        System.out.println(startTime + " : 阻塞队列开始消费...");
        for (int i = 0; i < threadCnt; i++) {
            pool.execute(new BlockingConsumer(queue, countDownLatch));
        }
        pool.shutdown();
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        LocalDateTime stopTime = LocalDateTime.now();
        System.out.println(stopTime + " : 阻塞队列消费结束...");
        System.out.println("阻塞队列消费耗时(ms):" + Duration.between(startTime, stopTime).toMillis());

    }

    private void testNoBlockingConsumer(int dataSize, int threadCnt) {
        class NoBlockingConsumer implements Runnable {
            private final ConcurrentLinkedQueue<Integer> queue;
            private final CountDownLatch countDownLatch;

            NoBlockingConsumer(ConcurrentLinkedQueue<Integer> queue, CountDownLatch countDownLatch) {
                this.queue = queue;
                this.countDownLatch = countDownLatch;
            }

            @Override
            public void run() {
                try {
                    // 不停消费队列中的数据
                    for (; ; ) {
                        // 拉取不到时退出
                        Integer number = queue.poll();
                        // 拉取不到时退出
                        if (number == null) {
                            return;
                        }
//                        // 模拟消费过程
//                        TimeUnit.MILLISECONDS.sleep(20);
                    }
//                } catch (InterruptedException e) {
//                    e.printStackTrace();
                } finally {
                    // 退出时递减门闩减一
                    countDownLatch.countDown();
                }
            }
        }
        // 准备数据
        List<Integer> array = makeTestData(dataSize);
        // 直接将数据全部扔进 ConcurrentLinkedQueue
        ConcurrentLinkedQueue<Integer> queue = new ConcurrentLinkedQueue<>(array);
        // 准备消费者
        CountDownLatch countDownLatch = new CountDownLatch(threadCnt);
        ExecutorService pool = Executors.newFixedThreadPool(threadCnt);
        LocalDateTime startTime = LocalDateTime.now();
        System.out.println(startTime + " : 无阻塞队列开始消费...");
        for (int i = 0; i < threadCnt; i++) {
            pool.execute(new NoBlockingConsumer(queue, countDownLatch));
        }
        pool.shutdown();
        try {
            countDownLatch.await();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        LocalDateTime stopTime = LocalDateTime.now();
        System.out.println(stopTime + " : 无阻塞队列消费结束...");
        System.out.println("无阻塞队列消费耗时(ms):" + Duration.between(startTime, stopTime).toMillis());
    }

    private List<Integer> makeTestData(int count) {
        Integer[] array = new Integer[count];
        Arrays.fill(array, 0);
        return Arrays.asList(array);
    }
}
