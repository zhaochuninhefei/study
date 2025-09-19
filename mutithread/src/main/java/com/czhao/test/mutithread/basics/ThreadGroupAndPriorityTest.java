package com.czhao.test.mutithread.basics;

/**
 * @author zhaochun
 */
public class ThreadGroupAndPriorityTest {
    public static void main(String[] args) {
        System.out.println("main主线程的ThreadGroup:");
        System.out.println(Thread.currentThread().getThreadGroup().getName());
        Thread t1 = new Thread();
        System.out.println("main主创建的子线程的ThreadGroup:");
        System.out.println(t1.getThreadGroup().getName());

        System.out.println("main主线程的优先级:");
        System.out.println(Thread.currentThread().getPriority());
        System.out.println("main主创建的子线程的默认优先级:");
        System.out.println(t1.getPriority());

        t1.setPriority(10);
        System.out.println("设置子线程的优先级:");
        System.out.println(t1.getPriority());

        Thread t2 = new Thread();
        t2.setDaemon(true);
        System.out.println("守护线程的默认优先级:");
        System.out.println(t2.getPriority());

        System.setSecurityManager(new SecurityManager());
        SecurityManager s = System.getSecurityManager();
        System.out.println(s.getThreadGroup().getName());
    }
}
