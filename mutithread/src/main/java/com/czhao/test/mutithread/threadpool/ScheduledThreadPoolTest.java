package com.czhao.test.mutithread.threadpool;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

/**
 * @author zhaochun
 */
public class ScheduledThreadPoolTest {
    public static void main(String[] args) {
        ScheduledThreadPoolTest me = new ScheduledThreadPoolTest();
        me.testScheduledThreadPool();
    }

    private void testScheduledThreadPool() {
        class TaskInSchedule implements Runnable {
            private final String taskName;

            public TaskInSchedule(String taskName) {
                this.taskName = taskName;
            }

            @Override
            public void run() {
                System.out.println("Start task in " + taskName);
                try {
                    TimeUnit.MILLISECONDS.sleep(200);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println("Stop task in " + taskName);
            }
        }

        int taskCnt = 3;
        ScheduledExecutorService scheduledExecutorService = Executors.newScheduledThreadPool(5);
        for (int i = 0; i < taskCnt; i++) {
            TaskInSchedule task = new TaskInSchedule("task" + (i + 1));
            switch (i) {
                case 0:
                    // 5秒后执行，只执行一次
                    scheduledExecutorService.schedule(task, 5, TimeUnit.SECONDS);
                    break;
                case 1:
                    // 2秒后开始执行，每5秒启动一次
                    scheduledExecutorService.scheduleAtFixedRate(task, 2, 5, TimeUnit.SECONDS);
                    break;
                case 2:
                    // 0秒后开始执行，每次执行结束后再过5秒执行下一次
                    scheduledExecutorService.scheduleWithFixedDelay(task, 0, 5, TimeUnit.SECONDS);
                    break;
            }
        }
        try {
            TimeUnit.SECONDS.sleep(20);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
        // 20秒后结束计划任务
        scheduledExecutorService.shutdown();
    }
}
