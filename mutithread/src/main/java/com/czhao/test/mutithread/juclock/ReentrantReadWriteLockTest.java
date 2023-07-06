package com.czhao.test.mutithread.juclock;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * @author zhaochun
 */
public class ReentrantReadWriteLockTest {
    public static void main(String[] args) {
        ReentrantReadWriteLockTest me = new ReentrantReadWriteLockTest();
        me.testCache();
    }

    private void testCache() {
        class Cache {
            private final Map<String, Object> cache = new HashMap<>();
            private final ReentrantReadWriteLock readWriteLock = new ReentrantReadWriteLock();

            public void put(String key, Object value) {
                // 这里使用trylock仅仅为了在获取锁失败时输出信息
//                readWriteLock.writeLock().lock();
                if (readWriteLock.writeLock().tryLock()) {
                    try {
                        System.out.println(LocalDateTime.now() + " " + Thread.currentThread().getName() + " 正在向缓存写入数据...");
                        Thread.sleep(500);
                        this.cache.put(key, value);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    } finally {
                        // unlock 时如果没有获得锁会抛出
                        readWriteLock.writeLock().unlock();
                    }
                } else {
                    System.out.println(LocalDateTime.now() + " " + Thread.currentThread().getName() + " 获得写锁失败!");
                }
            }

            public Object get(String key) {
//                readWriteLock.readLock().lock();
                if (readWriteLock.readLock().tryLock()) {
                    try {
                        System.out.println(LocalDateTime.now() + " " + Thread.currentThread().getName() + " 正在从缓存读取数据...");
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        return this.cache.get(key);
                    } finally {
                        readWriteLock.readLock().unlock();
                    }
                } else {
                    System.out.println(LocalDateTime.now() + " " + Thread.currentThread().getName() + " 获得读锁失败!");
                    return null;
                }
            }
        }
        Cache cache = new Cache();

        Thread t1 = new Thread(() -> {
            while (true) {
                cache.put("name", "张三");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "写线程1");

        Thread t2 = new Thread(() -> {
            while (true) {
                cache.get("name");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "读线程1");

        Thread t3 = new Thread(() -> {
            while (true) {
                cache.get("name");
                try {
                    Thread.sleep(1000);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        }, "读线程2");

        t1.start();
        t2.start();
        t3.start();

        // 主线程每隔一秒打印一次两个子线程的状态
        while (true) {
            try {
                Thread.sleep(1000);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
            System.out.println(String.format("线程 %s 的状态 %s", t1.getName(), t1.getState().toString()));
            System.out.println(String.format("线程 %s 的状态 %s", t2.getName(), t2.getState().toString()));
            System.out.println(String.format("线程 %s 的状态 %s", t3.getName(), t3.getState().toString()));
        }
    }
}
