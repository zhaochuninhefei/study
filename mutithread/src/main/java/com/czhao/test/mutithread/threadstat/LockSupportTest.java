package com.czhao.test.mutithread.threadstat;

import java.util.Scanner;
import java.util.concurrent.locks.LockSupport;

/**
 * @author zhaochun
 */
public class LockSupportTest {
    public static void main(String[] args) {
        LockSupportTest me = new LockSupportTest();
        me.testLockSupport();
    }

    private void testLockSupport() {
        class EchoPrinterAgain {
            public void echoPrint1() {
                Scanner scanner = new Scanner(System.in);
                System.out.println("我是胡汉三，我要跑路了。。。");
                // 暂停当前线程，进入WAITING状态
                LockSupport.park();
                if (Thread.currentThread().isInterrupted()) {
                    System.out.println("我胡汉三回来了。。。有人 interrupt。。。");
                } else {
                    System.out.println("我胡汉三回来了。。。有人 unpark。。。");
                }
            }
        }

        // 因为锁是EchoPrinter的一个实例，这里需要先生成实例
        EchoPrinterAgain echoPrinterAgain = new EchoPrinterAgain();


        // 创建线程1并启动，线程1将运行echoPrint方法
        Thread t1 = new Thread(echoPrinterAgain::echoPrint1);


        t1.start();

        // 尝试在子线程LockSupport.park之前先LockSupport.unpark
        LockSupport.unpark(t1);
        System.out.println("先执行了 LockSupport.unpark(t1)");

        // 主线程等待1秒钟，以确保线程1启动
        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        System.out.println(String.format("线程 %s 的状态 %s", "胡汉三", t1.getState().toString()));
        // 唤醒t1
        LockSupport.unpark(t1);
//        t1.interrupt();
    }
}
