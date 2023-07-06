package com.czhao.test.mutithread.juctools;

import java.util.Arrays;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Phaser;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

/**
 * @author zhaochun
 */
public class PhaserTest {
    public static void main(String[] args) {
        PhaserTest me = new PhaserTest();
        me.testProjectDevelop();
    }

    /**
     * 假设一个项目开发，使用瀑布式开发过程管理模式，将开发分为"设计"，"编码"，"测试"三个阶段，每个阶段全部结束之后才进入下一个阶段，但每个阶段参与的成员数量并不相同。
     */
    private void testProjectDevelop() {
        // 项目成员类
        class ProjectMember {
            // 姓名
            private final String name;
            // 成员类型 1:全程参与;2:参与设计和测试;3:参与编码和测试
            // 注意，成员类型不是项目阶段，而是指定这个成员参与哪些阶段。
            // 这里假设只有这三种参与类型
            private final int type;

            public ProjectMember(String name, int type) {
                this.name = name;
                this.type = type;
            }

            public void doDesign() {
                System.out.println(name + " 开始设计工作...");
                try {
                    TimeUnit.SECONDS.sleep(new Random().nextInt(5));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(name + " 设计工作结束...");
            }

            public void doCoding() {
                System.out.println(name + " 开始编码工作...");
                try {
                    TimeUnit.SECONDS.sleep(new Random().nextInt(5));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(name + " 编码工作结束...");
            }

            public void doTest() {
                System.out.println(name + " 开始测试工作...");
                try {
                    TimeUnit.SECONDS.sleep(new Random().nextInt(5));
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                System.out.println(name + " 测试工作结束...");
            }

            @Override
            public String toString() {
                return "ProjectMember{" +
                        "name='" + name + '\'' +
                        ", type=" + type +
                        '}';
            }

            public int getType() {
                return type;
            }
        }

        // 准备全部的项目成员
        Random random = new Random();
        String[] memberNames = new String[]{"赵", "钱", "孙", "李", "周", "吴", "郑", "王"};
        List<ProjectMember> projectMembers = Arrays.stream(memberNames)
                .map(s -> {
                    int type;
                    // 确保每种类型至少有一个成员
                    switch (s) {
                        case "赵":
                            type = 1;
                            break;
                        case "钱":
                            type = 2;
                            break;
                        case "孙":
                            type = 3;
                            break;
                        default:
                            type = random.nextInt(3) + 1;
                    }
                    return new ProjectMember(s, type);
                })
                .collect(Collectors.toList());

        // 打印一下项目成员：
        System.out.println("***** 项目成员 *****");
        // 全程参与的成员
        projectMembers.stream()
                .filter(projectMember -> projectMember.getType() == 1)
                .forEach(projectMember -> System.out.println(projectMember.toString()));
        // 只参与设计和测试的成员
        projectMembers.stream()
                .filter(projectMember -> projectMember.getType() == 2)
                .forEach(projectMember -> System.out.println(projectMember.toString()));
        // 只参与编码和测试的成员
        projectMembers.stream()
                .filter(projectMember -> projectMember.getType() == 3)
                .forEach(projectMember -> System.out.println(projectMember.toString()));
        System.out.println("**********");

        // 定义项目开发阶段，可以将 phaser 理解为项目，它有三个阶段: 设计 -> 编码 -> 测试
        Phaser phaser = new Phaser() {
            // onAdvance是每个阶段结束时触发的事件，phase是刚结束的阶段序号，registeredParties是当前剩下的成员数量。
            // phase从0开始递增，第一次触发onAdvance时，值为0，代表第一阶段结束。
            // onAdvance主要是由各个线程执行arriveAndAwaitAdvance所达成的同步事件触发的。
            // 所以一个phaser有多少个阶段，是由同步次数决定的。
            @Override
            protected boolean onAdvance(int phase, int registeredParties) {
                // 各阶段成员数量，去除作为管理员的项目经理，即主线程
                int memberCount = registeredParties - 1;
                // 判断是哪个阶段结束后触发的事件
                switch (phase) {
                    case 0:
                        System.out.println("----- 设计阶段结束，当前成员数量：" + memberCount);
                        break;
                    case 1:
                        System.out.println("----- 编码阶段结束，当前成员数量：" + memberCount);
                        break;
                    case 2:
                        System.out.println("----- 测试阶段结束，当前成员数量：" + memberCount);
                        break;
                }
                // 判断项目所有阶段是否都已结束，是则返回true，终止项目
                // 可以使用 phase 当前阶段序号判断，也可以使用 当前成员数量判断
                return phase > 2 || memberCount == 0;
            }
        };

        // 主线程注册，相当于项目经理作为管理者全程参与
        // 所谓注册，就是给 phaser 的已注册数字加一
        // 主线程要在各个成员之前先注册，这样就可以通过主线程的arriveAndAwaitAdvance来保证不会因为各个成员的多线程并发导致某个阶段提前结束。
        phaser.register();

        // 全程参与的成员
        // 全程参与，所以在主线程第一次arriveAndAwaitAdvance之前就应该注册进来
        projectMembers.stream()
                .filter(projectMember -> projectMember.getType() == 1)
                .forEach(projectMember -> {
                    // 往 phaser 注册，代表开始参与项目
                    phaser.register();
                    // 这个单独开启的线程就代表某个成员开始工作了
                    new Thread(() -> {
                        // 开始设计
                        projectMember.doDesign();
                        // 等待其他设计人员设计工作结束
                        // 类似栅栏，当前线程在这里被阻塞，直到有跟已注册数字相等数量的线程都执行到该方法时才继续执行
                        // 请思考：会不会有这样的可能，所有应该注册的线程还没有全部注册，就因为部分线程已经开始执行，碰巧就满足该条件于是继续执行了？
                        phaser.arriveAndAwaitAdvance();

                        // 开始编码
                        projectMember.doCoding();
                        // 等待其他编码人员编码工作结束
                        phaser.arriveAndAwaitAdvance();

                        // 开始测试
                        projectMember.doTest();
                        // 测试结束后退出项目，会将已注册数字减一
                        phaser.arriveAndDeregister();
                    }).start();
                });

        // 参与设计的成员
        // 设计是第一个阶段，因此参与设计的成员在主线程第一次arriveAndAwaitAdvance之前就应该注册
        projectMembers.stream()
                .filter(projectMember -> projectMember.getType() == 2)
                .forEach(projectMember -> {
                    phaser.register();
                    new Thread(() -> {
                        // 开始设计
                        projectMember.doDesign();
                        // 完成自己的设计工作就可以暂时离开项目
                        phaser.arriveAndDeregister();

                        // type 2 代表参与设计与测试的成员，设计结束时，该成员需要先离开，即注销在phaser中的注册；
                        // 后面在整个项目进行到测试阶段再重新加入进来，即重新注册
                    }).start();
                });

        // 主线程不断自旋执行arriveAndAwaitAdvance，确保同步时机的正确
        while (!phaser.isTerminated()) {
            // 主线程在各个阶段结束后同步，返回值是下一个阶段序号，
            // 这里的阶段序号跟前面定义的onAdvance中的参数phase不一样，不是刚结束的阶段序号，而是下一个阶段序号
            int phase = phaser.arriveAndAwaitAdvance();
            if (phase == 1) {
                System.out.println("========== 编码阶段已开始 ==========");
                // 编码阶段开始后，动态注册参与编码与测试的成员
                projectMembers.stream()
                        .filter(projectMember -> projectMember.getType() == 3)
                        .forEach(projectMember -> {
                            // 注册，表示从这个阶段开始参与
                            phaser.register();
                            new Thread(() -> {
                                // 开始编码
                                projectMember.doCoding();
                                // 等待其他编码人员编码工作结束
                                phaser.arriveAndAwaitAdvance();

                                // 开始测试
                                projectMember.doTest();
                                // 测试结束后退出项目
                                phaser.arriveAndDeregister();
                            }).start();
                        });
            } else if (phase == 2) {
                System.out.println("========== 测试阶段已开始 ==========");
                // 测试阶段开始后，之前设计结束后离开的人员重新加入，参与测试
                projectMembers.stream()
                        .filter(projectMember -> projectMember.getType() == 2)
                        .forEach(projectMember -> {
                            // 注册到，表示从这个阶段开始参与
                            phaser.register();
                            new Thread(() -> {
                                // 开始测试
                                projectMember.doTest();
                                // 测试结束后退出项目
                                phaser.arriveAndDeregister();
                            }).start();
                        });
            }
        }
    }
}
