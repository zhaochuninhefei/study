package com.czhao.test.mutithread.juccollectors;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.ConcurrentModificationException;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.concurrent.TimeUnit;

/**
 * @author zhaochun
 */
public class CopyOnWriteArrayListTest {
    public static void main(String[] args) {
        CopyOnWriteArrayListTest me = new CopyOnWriteArrayListTest();
        me.testCopyOnWriteArrayList();
    }

    private void testCopyOnWriteArrayList() {
        String[] arrNames = new String[]{"张三", "李四", "王五"};

        System.out.println("===== 开始测试ArrayList =====");
        List<String> nameArrayList = new ArrayList<>(Arrays.asList(arrNames));
        System.out.println("开始时nameArrayList的元素：" + nameArrayList.toString());
        Thread t1 = new Thread(() -> {
            System.out.println("t1 开始遍历 nameArrayList 集合...");
            try {
                for (String name : nameArrayList) {
                    System.out.println("Talking with " + name + " start...");
                    TimeUnit.SECONDS.sleep(2);
                    System.out.println("Talking with " + name + " stop...");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ConcurrentModificationException e) {
                System.out.println("谁动了我的奶酪:" + e.toString());
            }
            System.out.println("t1 遍历 nameArrayList 集合结束.");
        });
        Thread t2 = new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (Throwable e) {
                e.printStackTrace();
            }
            System.out.println("t2 执行 nameArrayList.add ...");
            nameArrayList.add("随便谁吧 ");
            System.out.println("t2 执行 nameArrayList.add 结束");
        });
        t1.start();
        t2.start();
        try {
            t1.join();
            t2.join();
            System.out.println("结束时nameArrayList的元素：" + nameArrayList.toString());
            System.out.println("===== ArrayList测试结束 =====");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        for (int i = 0; i < 3; i++) {
            System.out.println("稍等...");
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }

        System.out.println("===== 开始测试CopyOnWriteArrayList =====");
        List<String> namesCopyOnWrite = new CopyOnWriteArrayList<>(arrNames);
        System.out.println("开始时namesCopyOnWrite的元素：" + namesCopyOnWrite.toString());
        Thread t3 = new Thread(() -> {
            System.out.println("t1 开始遍历 namesCopyOnWrite 集合...");
            try {
                for (String name : namesCopyOnWrite) {
                    System.out.println("Talking with " + name + " start...");
                    TimeUnit.SECONDS.sleep(2);
                    System.out.println("Talking with " + name + " stop...");
                }
            } catch (InterruptedException e) {
                e.printStackTrace();
            } catch (ConcurrentModificationException e) {
                System.out.println("谁动了我的奶酪:" + e.toString());
            }
            System.out.println("t1 遍历 namesCopyOnWrite 集合结束.");
        });
        Thread t4 = new Thread(() -> {
            try {
                TimeUnit.SECONDS.sleep(1);
            } catch (Throwable e) {
                e.printStackTrace();
            }
            System.out.println("t2 执行 namesCopyOnWrite.add ...");
            namesCopyOnWrite.add("随便谁吧 ");
            System.out.println("t2 执行 namesCopyOnWrite.add 结束");
        });
        t3.start();
        t4.start();
        try {
            t3.join();
            t4.join();
            System.out.println("结束时namesCopyOnWrite的元素：" + namesCopyOnWrite.toString());
            System.out.println("===== CopyOnWriteArrayList测试结束 =====");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}
