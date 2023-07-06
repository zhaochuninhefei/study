package com.czhao.test.mutithread.juctools;

import java.util.concurrent.Exchanger;
import java.util.concurrent.TimeUnit;

/**
 * @author zhaochun
 */
public class ExchangerTest {
    public static void main(String[] args) {
        ExchangerTest me = new ExchangerTest();
        me.testSpy();
    }

    private void testSpy() {
        class Spy {
            private final String name;
            private final Exchanger<String> exchanger;

            public Spy(String name, Exchanger<String> exchanger) {
                this.name = name;
                this.exchanger = exchanger;
            }

            public void exchangeMessage(String myMsg) {
                try {
                    System.out.println(name + " 到达接头地点...");
                    String otherMsg = exchanger.exchange(myMsg);
                    System.out.println(name + " 从对方处获得情报:" + otherMsg);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }

        Exchanger<String> exchanger = new Exchanger<>();
        new Thread(() -> new Spy("深海", exchanger).exchangeMessage("黑桃Q计划下个月1号去津港。")).start();
        try {
            TimeUnit.SECONDS.sleep(3);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        new Thread(() -> new Spy("小船", exchanger).exchangeMessage("红桃K昨天去了趟医院。")).start();
    }
}
