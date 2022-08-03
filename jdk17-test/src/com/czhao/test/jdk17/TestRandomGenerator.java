package com.czhao.test.jdk17;

import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.Comparator;
import java.util.SplittableRandom;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadLocalRandom;
import java.util.concurrent.TimeUnit;
import java.util.random.RandomGenerator;
import java.util.random.RandomGeneratorFactory;

/**
 * JEP 356:	Enhanced Pseudo-Random Number Generators
 *
 * @author zhaochun
 */
public class TestRandomGenerator {
    public static void main(String[] args) {
        TestRandomGenerator me = new TestRandomGenerator();
        me.test01();
    }

    private void test01() {
        // 打印所有已注册的随机数生成器及其算法属性
        RandomGeneratorFactory.all()
                .sorted(Comparator.comparing(RandomGeneratorFactory::name))
                .forEach(factory -> System.out.printf("name : %s , group: %s , 可跳跃: %s , 可分割: %s%n",
                        factory.name(),
                        factory.group(),
                        factory.isJumpable(),
                        factory.isSplittable()));

        // SecureRandom 密码学安全的伪随机数生成器(CSPRNG)
        SecureRandom secureRandom = (SecureRandom)RandomGeneratorFactory.of("SecureRandom").create();
        System.out.println("SecureRandom : " + secureRandom.nextInt());
        System.out.println("SecureRandom : " + secureRandom.nextInt());

        // Random,伪随机数生成器(线性同余算法)
        //  默认使用当前时间戳生成种子(并不是直接使用时间戳作为种子)
        RandomGenerator random = RandomGeneratorFactory.of("Random").create();
        System.out.println("Random : " + random.nextInt());
        System.out.println("Random : " + random.nextInt());

        // SplittableRandom 伪随机数生成器(SplitMix算法)
        //  默认使用黄金比例((1+sqrt(5))/2)生成种子，这里改为用 secureRandom生成的密码学安全的伪随机数作为种子
        SplittableRandom splittableRandom = (SplittableRandom)RandomGeneratorFactory.of("SplittableRandom").create(secureRandom.nextLong());
        System.out.println("SplittableRandom in main thread : " + splittableRandom.nextInt());
        System.out.println("SplittableRandom in main thread : " + splittableRandom.nextInt());
        // ThreadLocalRandom,伪随机数生成器(SplitMix算法) 使用内部种子
        System.out.println("ThreadLocalRandom in main thread : " + ThreadLocalRandom.current().nextInt());
        System.out.println("ThreadLocalRandom in main thread : " + ThreadLocalRandom.current().nextInt());
        ExecutorService executorService = Executors.newFixedThreadPool(5);
        for (int i = 0; i < 10; i++) {
            SplittableRandom splittableRandomSub = splittableRandom.split();
            executorService.submit(() -> {
                System.out.println("ThreadLocalRandom in sub thread : " + ThreadLocalRandom.current().nextInt());
                System.out.println("ThreadLocalRandom in sub thread : " + ThreadLocalRandom.current().nextInt());
                System.out.println("SplittableRandom in sub thread : " + splittableRandomSub.nextInt());
                System.out.println("SplittableRandom in sub thread : " + splittableRandomSub.nextInt());
            });
        }
        executorService.shutdown();
        try {
            executorService.awaitTermination(10, TimeUnit.SECONDS);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        // Xoroshiro128PlusPlus 伪随机数生成器(xoshiro/xoroshiro算法之一)
        //  默认使用黄金比例((1+sqrt(5))/2)生成种子
        RandomGenerator xoroshiro128PlusPlus = RandomGeneratorFactory.of("Xoroshiro128PlusPlus").create();
        System.out.println("Xoroshiro128PlusPlus : " + xoroshiro128PlusPlus.nextInt());
        System.out.println("Xoroshiro128PlusPlus : " + xoroshiro128PlusPlus.nextInt());

        // Xoshiro256PlusPlus 伪随机数生成器(xoshiro/xoroshiro算法之一)
        //  默认使用黄金比例((1+sqrt(5))/2)生成种子，这里改为secureRandom生成的密码学安全的伪随机数作为种子
        RandomGenerator xoshiro256PlusPlus = RandomGeneratorFactory.of("Xoshiro256PlusPlus").create(secureRandom.nextLong());
        System.out.println("Xoshiro256PlusPlus : " + xoshiro256PlusPlus.nextInt());
        System.out.println("Xoshiro256PlusPlus : " + xoshiro256PlusPlus.nextInt());

        // L128X256MixRandom 伪随机数生成器(LXM PRNG算法家族之一)
        //  默认使用黄金比例((1+sqrt(5))/2)生成种子
        RandomGenerator l128X256MixRandom = RandomGeneratorFactory.of("L128X256MixRandom").create();
        System.out.println("L128X256MixRandom : " + l128X256MixRandom.nextInt());
        System.out.println("L128X256MixRandom : " + l128X256MixRandom.nextInt());

        // L128X128MixRandom 伪随机数生成器(LXM PRNG算法家族之一)
        //  默认使用黄金比例((1+sqrt(5))/2)生成种子，这里改为secureRandom生成的密码学安全的伪随机数作为种子
        RandomGenerator l128X128MixRandom = RandomGeneratorFactory.of("L128X128MixRandom").create(secureRandom.nextLong());
        System.out.println("L128X128MixRandom : " + l128X128MixRandom.nextInt());
        System.out.println("L128X128MixRandom : " + l128X128MixRandom.nextInt());

        // L128X1024MixRandom 伪随机数生成器(LXM PRNG算法家族之一)
        //  默认使用黄金比例((1+sqrt(5))/2)生成种子
        RandomGenerator l128X1024MixRandom = RandomGeneratorFactory.of("L128X1024MixRandom").create();
        System.out.println("L128X1024MixRandom : " + l128X1024MixRandom.nextInt());
        System.out.println("L128X1024MixRandom : " + l128X1024MixRandom.nextInt());

        // L64X256MixRandom 伪随机数生成器(LXM PRNG算法家族之一)
        //  默认使用黄金比例((1+sqrt(5))/2)生成种子
        RandomGenerator l64X256MixRandom = RandomGeneratorFactory.of("L64X256MixRandom").create();
        System.out.println("L64X256MixRandom : " + l64X256MixRandom.nextInt());
        System.out.println("L64X256MixRandom : " + l64X256MixRandom.nextInt());

        // L64X128StarStarRandom 伪随机数生成器(LXM PRNG算法家族之一)
        //  默认使用黄金比例((1+sqrt(5))/2)生成种子
        RandomGenerator l64X128StarStarRandom = RandomGeneratorFactory.of("L64X128StarStarRandom").create();
        System.out.println("L64X128StarStarRandom : " + l64X128StarStarRandom.nextInt());
        System.out.println("L64X128StarStarRandom : " + l64X128StarStarRandom.nextInt());

        // L64X128MixRandom 伪随机数生成器(LXM PRNG算法家族之一)
        //  默认使用黄金比例((1+sqrt(5))/2)生成种子
        RandomGenerator l64X128MixRandom = RandomGeneratorFactory.of("L64X128MixRandom").create();
        System.out.println("L64X128MixRandom : " + l64X128MixRandom.nextInt());
        System.out.println("L64X128MixRandom : " + l64X128MixRandom.nextInt());

        // L64X1024MixRandom 伪随机数生成器(LXM PRNG算法家族之一)
        //  默认使用黄金比例((1+sqrt(5))/2)生成种子
        RandomGenerator l64X1024MixRandom = RandomGeneratorFactory.of("L64X1024MixRandom").create();
        System.out.println("L64X1024MixRandom : " + l64X1024MixRandom.nextInt());
        System.out.println("L64X1024MixRandom : " + l64X1024MixRandom.nextInt());

        // L32X64MixRandom 伪随机数生成器(LXM PRNG算法家族之一)
        //  默认使用黄金比例((1+sqrt(5))/2)生成种子
        //  L32X64MixRandom的实现有BUG，无论是否显式指定种子，其首个随机数都是一样的。
        //  参考: https://stackoverflow.com/questions/72146414/default-algo-for-randomgenerator-l32x64mixrandom-generates-the-same-number-eac
        //       https://bugs.openjdk.org/browse/JDK-8282551
        RandomGenerator l32X64MixRandom = RandomGeneratorFactory.of("L32X64MixRandom").create(secureRandom.nextLong());
        System.out.println("L32X64MixRandom : " + l32X64MixRandom.nextInt());
        System.out.println("L32X64MixRandom : " + l32X64MixRandom.nextInt());

        // JDK17默认随机数生成器 L32X64MixRandom
        //  与L32X64MixRandom有同样的BUG： 无论是否显式指定种子，其首个随机数都是一样的。
        RandomGenerator generator = RandomGenerator.getDefault();
        System.out.println("RandomGenerator Default : " + generator.nextInt());
        System.out.println("RandomGenerator Default : " + generator.nextInt());
    }
}
