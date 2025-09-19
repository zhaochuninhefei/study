package com.czhao.test.mutithread.juccollectors;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Random;
import java.util.concurrent.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author zhaochun
 */
public class BlockingQueueTest {
    public static void main(String[] args) {
        BlockingQueueTest me = new BlockingQueueTest();

//        me.testMsgQueue();

        // blockingQueueType 1:ArrayBlockingQueue ; 2:LinkedBlockingQueue
//        me.testBlockingQueue(100, 1000, 1);
        me.testBlockingQueue(200, 200, 2);
    }

    private static volatile boolean stop = false;

    static class MsgConsumer {
        private BlockingQueue<String> queue;

        private String name;
        private int consumedMsgCnt;

        public MsgConsumer(BlockingQueue<String> queue, String name) {
            this.queue = queue;
            this.name = name;
        }

        public void consume() {
            while (true) {
                if (stop) break;
                try {
                    String msg = queue.take();
                    consumedMsgCnt++;
                    System.out.println(String.format("消费者 %s 消费一个消息:[%s]，截至目前已经消费 %s 个消息。", name, msg, consumedMsgCnt));
                    if (stop) break;
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

    static class MsgProducer {
        private BlockingQueue<String> queue;

        private String name;
        private int producedMsgCnt;

        public MsgProducer(BlockingQueue<String> queue, String name) {
            this.queue = queue;
            this.name = name;
        }

        public void produce() {
            while (true) {
                if (stop) break;
                try {
                    String msg = "你好啊";
                    queue.put(msg);
                    producedMsgCnt++;
                    System.out.println(String.format("生产者 %s 生产了一个消息:[%s]，截至目前已生产 %s 个消息", name, msg, producedMsgCnt));
                    if (stop) break;
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

    private void testBlockingQueue(int producerSize, int consumerSize, int blockingQueueType) {
        BlockingQueue<String> queue = blockingQueueType == 1 ? new ArrayBlockingQueue<>(512) : new LinkedBlockingQueue<>();

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
            // 等待之前陷入阻塞的生产者与消费者完成最后一次生产或消费
            TimeUnit.SECONDS.sleep(2);
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

    private void testMsgQueue() {
        class MsgConsumer {
            private ArrayBlockingQueue<String> queue;

            public MsgConsumer(ArrayBlockingQueue<String> queue) {
                this.queue = queue;
            }

            public void consume() {
                while (true) {
                    try {
                        System.out.println(String.format("消费者收到一个消息:[%s]，队列中还剩 %s 个消息。", queue.take(), queue.size()));
                        TimeUnit.SECONDS.sleep(new Random().nextInt(5));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        class MsgProducer {
            private ArrayBlockingQueue<String> queue;

            public MsgProducer(ArrayBlockingQueue<String> queue) {
                this.queue = queue;
            }

            public void produce() {
                while (true) {
                    try {
                        String msg = "你好啊";
                        queue.put(msg);
                        System.out.println(String.format("生产者发出一个消息:[%s]，队列剩余空间: %s", msg, queue.remainingCapacity()));
                        TimeUnit.SECONDS.sleep(new Random().nextInt(5));
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }

        ArrayBlockingQueue<String> queue = new ArrayBlockingQueue<>(10);
        new Thread(() -> new MsgProducer(queue).produce()).start();
        new Thread(() -> new MsgConsumer(queue).consume()).start();
    }
}
