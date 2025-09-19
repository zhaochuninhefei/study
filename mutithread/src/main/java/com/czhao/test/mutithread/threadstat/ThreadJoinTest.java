package com.czhao.test.mutithread.threadstat;

/**
 * @author zhaochun
 */
public class ThreadJoinTest {
    public static void main(String[] args) {
        ThreadJoinTest me = new ThreadJoinTest();
        me.testJoin();
    }

    private void testJoin() {
        class Poet implements Runnable {
            @Override
            public void run() {
                System.out.println("诗人：先喝点小酒。。。");
                try {
                    Thread.sleep(3000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                String line = "诗人：大海啊，都是水～～～";
                System.out.println(line);
            }
        }

        Thread poet = new Thread(new Poet());
        poet.start();

        System.out.println("观众：翘首以待。。。");
        try {
            // 当前线程进入WAITING状态，一直到poet线程执行结束。
            poet.join();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        System.out.println("观众：这样的诗，我每天能写一箩筐。。。");
    }
}
