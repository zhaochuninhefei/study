package com.czhao.test.mutithread.juclock;

import java.util.concurrent.locks.StampedLock;

public class Point {
    private double x, y;
    private final StampedLock sl = new StampedLock();

    // an exclusively locked method 使用排他锁的方法
    void move(double deltaX, double deltaY) {
        long stamp = sl.writeLock();
        try {
            x += deltaX;
            y += deltaY;
        } finally {
            sl.unlockWrite(stamp);
        }
    }

    // a read-only method 只读方法
    // upgrade from optimistic read to read lock 从乐观读锁升级到悲观读锁
    double distanceFromOrigin() {
        // 获取乐观读锁，返回的stamp是一个代表版本号的数字，0代表获取锁失败，后面对这个数字进行验证可以确认共享变量是否被写入过
        // 实际上tryOptimisticRead只是计算一个版本号，并没有什么上锁操作，因此速度极快，也不需要解锁。
        long stamp = sl.tryOptimisticRead();
        try {
            // 这里是 continue 语法的标签，代表循环里的某个continue是要对这一层循环直接执行下一条
            retryHoldingLock:
            // 下一次循环时先尝试获取悲观读锁，这里就是一个自旋操作
            for (; ; stamp = sl.readLock()) {
                // 如果版本号是0，代表获取锁失败，则自旋，继续获取悲观读锁
                if (stamp == 0L)
                    continue retryHoldingLock;
                // possibly racy reads 这里不知道咋翻译，但意思是从主存读取共享变量的值到工作内存
                // 或者说，是将堆中的共享变量的值压入方法栈里
                // 注意，这里有两个共享变量，因此后面要注意一致性问题(例如，x是新版本的值，y还是老版本的值)
                double currentX = x;
                double currentY = y;
                // 对版本号进行校验，如果返回false，则表示版本号变了，此时必须自旋，重新获取悲观读锁再重读一次，以避免出现x,y版本不一致问题
                // 这个操作并不能保证后续的计算所使用的是最新的值，只能保证它们是同一个版本的值(一致性)
                if (!sl.validate(stamp))
                    continue retryHoldingLock;
                // 不管现在获取的乐观读锁还是悲观读锁，都可以保证这里的x和y是版本一致的，直接运算并返回结果
                // 当然会有概率在上一步校验OK之后有写线程更新了x或y的值，但不会影响本次计算的一致性，只是计算上稍有延迟而已。
                // 在高并发写的场景下，这种计算延迟发生的概率较大。
                return Math.hypot(currentX, currentY);
            }
        } finally {
            // 根据当前stamp确认已经升级为悲观读锁的话才需要解锁
            if (StampedLock.isReadLockStamp(stamp))
                sl.unlockRead(stamp);
        }
    }

    // upgrade from optimistic read to write lock 从乐观读升级到写锁
    void moveIfAtOrigin(double newX, double newY) {
        long stamp = sl.tryOptimisticRead();
        try {
            retryHoldingLock:
            for (; ; stamp = sl.writeLock()) {
                if (stamp == 0L)
                    continue retryHoldingLock;
                // possibly racy reads
                double currentX = x;
                double currentY = y;
                if (!sl.validate(stamp))
                    continue retryHoldingLock;
                // 如果当前位置是原点，就移动(给x,y赋值为传入的值)
                if (currentX != 0.0 || currentY != 0.0)
                    break;
                // 尝试将锁升级为写锁
                stamp = sl.tryConvertToWriteLock(stamp);
                // 升级失败则进入自旋
                if (stamp == 0L)
                    continue retryHoldingLock;
                // exclusive access 获取到写锁后，这里的操作就是排他的了
                x = newX;
                y = newY;
                return;
            }
        } finally {
            if (StampedLock.isWriteLockStamp(stamp))
                sl.unlockWrite(stamp);
        }
    }

    // Upgrade read lock to write lock 悲观读锁升级为写锁
    void moveIfAtOrigin2(double newX, double newY) {
        long stamp = sl.readLock();
        try {
            // 如果当前位置是原点，就移动(给x,y赋值为传入的值)
            while (x == 0.0 && y == 0.0) {
                // 尝试升级锁为写锁
                long ws = sl.tryConvertToWriteLock(stamp);
                if (ws != 0L) {
                    // 升级写锁成功，这里的操作是排他的了
                    stamp = ws;
                    x = newX;
                    y = newY;
                    break;
                } else {
                    // 升级写锁失败，强制释放悲观读锁
                    sl.unlockRead(stamp);
                    // 尝试获取写锁
                    stamp = sl.writeLock();
                }
            }
        } finally {
            // 无论是悲观读锁还是写锁，最终都要释放
            sl.unlock(stamp);
        }
    }
}
