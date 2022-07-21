package com.czhao.test.jdk17;

import java.util.Comparator;
import java.util.concurrent.ThreadLocalRandom;
import java.util.random.RandomGenerator;
import java.util.random.RandomGeneratorFactory;

/**
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

        // Random,伪随机数生成器(线性同余算法)
        RandomGenerator random = RandomGeneratorFactory.of("Random").create();
        System.out.println(random.nextInt());

        // SecureRandom 密码学强随机数生成器(RNG)
        RandomGenerator secureRandom = RandomGeneratorFactory.of("SecureRandom").create();
        System.out.println(secureRandom.nextInt());

        // SplittableRandom 伪随机数生成器(SplitMix算法)
        RandomGenerator splittableRandom = RandomGeneratorFactory.of("SplittableRandom").create();
        System.out.println(splittableRandom.nextInt());
        // ThreadLocalRandom,伪随机数生成器(SplitMix算法)
        System.out.println(ThreadLocalRandom.current().nextInt());

        // Xoroshiro128PlusPlus 伪随机数生成器(xoshiro/xoroshiro算法之一)
        RandomGenerator xoroshiro128PlusPlus = RandomGeneratorFactory.of("Xoroshiro128PlusPlus").create();
        System.out.println(xoroshiro128PlusPlus.nextInt());

        // Xoshiro256PlusPlus 伪随机数生成器(xoshiro/xoroshiro算法之一)
        RandomGenerator xoshiro256PlusPlus = RandomGeneratorFactory.of("Xoshiro256PlusPlus").create();
        System.out.println(xoshiro256PlusPlus.nextInt());

        // L128X256MixRandom 伪随机数生成器(LXM PRNG算法家族之一)
        RandomGenerator l128X256MixRandom = RandomGeneratorFactory.of("L128X256MixRandom").create();
        System.out.println(l128X256MixRandom.nextInt());

        // L128X128MixRandom 伪随机数生成器(LXM PRNG算法家族之一)
        RandomGenerator l128X128MixRandom = RandomGeneratorFactory.of("L128X128MixRandom").create();
        System.out.println(l128X128MixRandom.nextInt());

        // L128X1024MixRandom 伪随机数生成器(LXM PRNG算法家族之一)
        RandomGenerator l128X1024MixRandom = RandomGeneratorFactory.of("L128X1024MixRandom").create();
        System.out.println(l128X1024MixRandom.nextInt());

        // L64X256MixRandom 伪随机数生成器(LXM PRNG算法家族之一)
        RandomGenerator l64X256MixRandom = RandomGeneratorFactory.of("L64X256MixRandom").create();
        System.out.println(l64X256MixRandom.nextInt());

        // L64X128StarStarRandom 伪随机数生成器(LXM PRNG算法家族之一)
        RandomGenerator l64X128StarStarRandom = RandomGeneratorFactory.of("L64X128StarStarRandom").create();
        System.out.println(l64X128StarStarRandom.nextInt());

        // L64X128MixRandom 伪随机数生成器(LXM PRNG算法家族之一)
        RandomGenerator l64X128MixRandom = RandomGeneratorFactory.of("L64X128MixRandom").create();
        System.out.println(l64X128MixRandom.nextInt());

        // L64X1024MixRandom 伪随机数生成器(LXM PRNG算法家族之一)
        RandomGenerator l64X1024MixRandom = RandomGeneratorFactory.of("L64X1024MixRandom").create();
        System.out.println(l64X1024MixRandom.nextInt());

        // L32X64MixRandom 伪随机数生成器(LXM PRNG算法家族之一)
        RandomGenerator l32X64MixRandom = RandomGeneratorFactory.of("L32X64MixRandom").create();
        System.out.println(l32X64MixRandom.nextInt());

        // JDK17默认随机数生成器 L32X64MixRandom
        RandomGenerator generator = RandomGenerator.getDefault();
        System.out.println(generator.nextInt());
    }
}
