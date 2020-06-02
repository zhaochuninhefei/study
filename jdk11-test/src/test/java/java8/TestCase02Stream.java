package java8;

import java8.sub.Evaluation;
import java8.sub.Poet;
import java8.sub.PoetExt;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author zhaochun
 */
public class TestCase02Stream {
    public static void main(String[] args) {
        TestCase02Stream me = new TestCase02Stream();
        me.test00_classic();
        me.test01_stream();
        me.test02_parallelStream();
        me.test03_refactorLambda();
//        me.test04_pfm_oneLoop();
//        me.test05_pfm_join();
//        me.test06_pfm_multLoop();
    }

    private void test00_classic() {
        // 一个经典的Stream操作
        // 操作步骤：  获取数据源 Source --> 数据转换 Transform --> 执行操作 Operation
        // pip line: 数据集Stream | filter Stream | map Stream | reduce 操作 获得结果
        int sum = Stream.of("", "1", null, "2", " ", "3")
                .filter(s -> s != null && s.trim().length() > 0)
                .map(Integer::parseInt)
                .reduce((left, right) -> right += left)
                .orElse(0);
        System.out.println(sum);
    }

    private void test01_stream() {
        List<Poet> poets = Poet.preparePoets();

        // foreach 等价于 poets.stream().forEach(System.out::println);
        poets.forEach(System.out::println);

        Stream<Poet> poetStream = poets.stream();
        poetStream.forEach(System.out::println);
        try {
            // 不能对同一个stream对象做两次操作，stream是流，不能回头，操作过一次之后就不能再操作了。
            poetStream.forEach(System.out::println);
        } catch (Throwable t) {
            System.out.println("stream has already been operated upon or closed. 别人嚼过的甘蔗你就别嚼了。。。");
        }
        // 但是重新从集合获取stream是可以重复操作的，因为是一个新的stream对象。
        poets.stream().forEach(System.out::println);

        // map  Collectors
        String strPoets = poets.stream()
                .map(poet -> poet.getName() + " 唐代大诗人")
                .collect(Collectors.joining(","));
        System.out.println(strPoets);

        // filter + map + collect 倒入set集合中
        Set<String> poetsLi = poets.stream()
                .filter(poet -> poet.getName().startsWith("李"))
                .map(poet -> "唐诗三李 之 " + poet.getName())
                .collect(Collectors.toSet());
        System.out.println(poetsLi);

        // 之前说对同一个stream对象只能操作一次，为何这里链式多次操作？
        // 因为 map, filter这些方法返回了一个新的stream对象

        // filter + findAny/findFirst 查找一个满足条件的数据
        Poet topPoet = poets.stream()
                .filter(poet -> poet.getEvaluation() > 4)
                .findAny()
//                .findFirst()
                // 关于 orElse, 后面讲 Optional 的时候再解释
                .orElse(new Poet("杜甫", 58, 5));
        System.out.println("最牛的诗人之一：" + topPoet.getName());

        // allMatch
        boolean all50plus = poets.stream()
                .allMatch(poet -> poet.getAge() > 50);
        System.out.println("大诗人们都活了50岁以上吗？" + (all50plus ? "是的" : "并没有"));

        // anyMatch
        boolean any50plus = poets.stream()
                .anyMatch(poet -> poet.getAge() > 50);
        System.out.println("大诗人们有活到50岁以上的吗？" + (any50plus ? "有的有的" : "居然没有"));

        // count max min sum
        // 5星诗人数量 count
        System.out.println("5星诗人数量:" + poets.stream()
                .filter(poet -> poet.getEvaluation() == 5)
                .count());
        // 年龄最大的诗人
        System.out.println("年龄最大的诗人:" + poets.stream()
                .max(Comparator.comparingInt(Poet::getAge))
                .orElse(null));
        // 年龄最小的诗人
        System.out.println("年龄最小的诗人:" + poets.stream()
                .min(Comparator.comparingInt(Poet::getAge))
                .orElse(null));
        // 年龄合计
        System.out.println("诗人们年龄合计:" + poets.stream()
                .mapToInt(Poet::getAge)
                .sum());

        // reduce 计算合计
        int sumAge = poets.stream()
                .mapToInt(Poet::getAge)
                .reduce((age, sum) -> sum += age)
//                .reduce(Integer::sum)
                .orElse(0);
        System.out.println("reduce计算出的年龄合计:" + sumAge);

        // reduce 有起始值的统计
        // 假设唐代其他诗人们的评价合计已经有了，假设是 100，但还未包括前面的7位，这里从 100 开始继续统计评价总值
        int sumEvaluation = poets.stream()
                .mapToInt(Poet::getEvaluation)
                .reduce(100, (left, right) -> right += left);
//                .reduce(100, Integer::sum);
        System.out.println("reduce计算出的有起始值的评价合计:" + sumEvaluation);

        // limit
        System.out.println("生成一个等差数组，限制长度为10：");
        Stream.iterate(1, n -> n + 3).limit(10).forEach(x -> System.out.print(x + " "));
        System.out.println();

        // distinct
        String distinctEvaluation = poets.stream()
                .map(poet -> String.valueOf(poet.getEvaluation()))
                .distinct()
                .collect(Collectors.joining(","));
        System.out.println("诗人们的评价分数(去重)：" + distinctEvaluation);

        // sort
        System.out.println("诗人们按年龄排序：");
        poets.stream()
                .sorted(Comparator.comparingInt(Poet::getAge))
                .forEach(System.out::println);

        // group
        Map<String, List<Poet>> poetsByAge = poets.stream()
                .collect(Collectors.groupingBy(poet -> {
                    int age = poet.getAge();
                    if (age < 20) {
                        return "1~19";
                    } else if (age < 30) {
                        return "20~29";
                    } else if (age < 40) {
                        return "30~39";
                    } else if (age < 50) {
                        return "40~49";
                    } else if (age < 60) {
                        return "50~59";
                    } else if (age < 70) {
                        return "60~69";
                    } else {
                        return "70~";
                    }
                }));
        System.out.println("将诗人们按年龄分组：");
        poetsByAge.keySet().stream()
                .sorted(String::compareTo)
                .forEach(s -> System.out.println(
                        String.format("%s : %s", s, poetsByAge.get(s).stream().map(Poet::getName).collect(Collectors.joining(",")))));

        // flatmap [(poet1, poet2, poet3),(poet4,poet5)] --> [poet1, poet2, poet3, poet4, poet5]
        System.out.println("通过flatmap将分组后的诗人集合扁平化:");
        List<Poet> lstFromGroup = poetsByAge.values().stream()
                .flatMap(poets1 -> poets1.stream())
                .collect(Collectors.toList());
        lstFromGroup.forEach(System.out::println);

    }

    private void test02_parallelStream() {
        List<Poet> poets = Poet.preparePoets();

        System.out.println("findAny:");
        for (int i = 0; i < 10; i++) {
            Poet topPoet1 = poets.parallelStream()
                    .filter(poet -> poet.getEvaluation() > 4)
                    .findAny()
                    .orElse(new Poet("XX", 50, 5));
            System.out.println("最牛的诗人之一：" + topPoet1.getName());
        }

        System.out.println("findFirst:");
        for (int i = 0; i < 10; i++) {
            Poet topPoet2 = poets.parallelStream()
                    .filter(poet -> poet.getEvaluation() > 4)
                    .findFirst()
                    .orElse(new Poet("XX", 50, 5));
            System.out.println("最牛的诗人之一：" + topPoet2.getName());
        }

        // parallelStream使用要谨慎，并不是所有的运算都可以并行执行的。
        int sumEvaluation = poets.parallelStream()
                .mapToInt(Poet::getEvaluation)
                .reduce(100, Integer::sum);
        System.out.println("reduce计算有初始值时，不应该用并行运算:" + sumEvaluation);
    }

    private void test03_refactorLambda() {
        List<Poet> poets = Poet.preparePoets();

        poets.stream()
                .filter(poet -> poet.getEvaluation() < 5)
                .forEach(poet -> System.out.println(poet.getName()));

        Predicate<Poet> poetPredicate = poet -> poet.getEvaluation() < 5;
        Consumer<Poet> poetConsumer = poet -> System.out.println(poet.getName());
        poets.stream()
                .filter(poetPredicate)
                .forEach(poetConsumer);

        Function<Poet, String> poetStringFunction = poet -> {
            int age = poet.getAge();
            if (age < 20) {
                return "1~19";
            } else if (age < 30) {
                return "20~29";
            } else if (age < 40) {
                return "30~39";
            } else if (age < 50) {
                return "40~49";
            } else if (age < 60) {
                return "50~59";
            } else if (age < 70) {
                return "60~69";
            } else {
                return "70~";
            }
        };
        Map<String, List<Poet>> poetsByAge = poets.stream()
                .collect(Collectors.groupingBy(poetStringFunction));
        System.out.println("将诗人们按年龄分组：");
        Consumer<String> stringConsumer = s -> System.out.println(
                String.format("%s : %s", s, poetsByAge.get(s).stream().map(Poet::getName).collect(Collectors.joining(","))));
        poetsByAge.keySet().stream()
                .sorted(String::compareTo)
                .forEach(stringConsumer);
    }

    private void test04_pfm_oneLoop() {
        List<String> numbers = new ArrayList<>();
        for (int i = 0; i < 100; i++) {
            numbers.add("a" + i);
        }

        System.out.println("=== loop with fori ===");
        LocalDateTime startTime = LocalDateTime.now();
        for (int i = 0; i < numbers.size(); i++) {
            String whatever = numbers.get(i) + "b";
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        LocalDateTime stopTime = LocalDateTime.now();
        System.out.println("loop with fori time(millis):" + Duration.between(startTime, stopTime).toMillis());

        System.out.println("=== loop with Iterator ===");
        startTime = LocalDateTime.now();
        for (String num : numbers) {
            String whatever = num + "b";
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        stopTime = LocalDateTime.now();
        System.out.println("loop with Iterator time(millis):" + Duration.between(startTime, stopTime).toMillis());

        System.out.println("=== loop with stream ===");
        startTime = LocalDateTime.now();
        numbers.stream().forEach(num -> {
            String whatever = num + "b";
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        stopTime = LocalDateTime.now();
        System.out.println("loop with stream time(millis):" + Duration.between(startTime, stopTime).toMillis());

        System.out.println("=== loop with parallelStream ===");
        startTime = LocalDateTime.now();
        numbers.parallelStream().forEach(num -> {
            String whatever = num + "b";
            try {
                Thread.sleep(500);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        });
        stopTime = LocalDateTime.now();
        System.out.println("loop with parallelStream time(millis):" + Duration.between(startTime, stopTime).toMillis());
    }

    private void test05_pfm_join() {
        // poets件数
        int n = 100000;
        // evaluations件数
        int m = 100;
        List<Poet> poets = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            String name = String.format("诗人%010d", i + 1);
            poets.add(new Poet(name, (int) (80 * Math.random()) + 10, (int) (m * Math.random()) + 1));
        }
        List<Evaluation> evaluations = new ArrayList<>();
        for (int i = 0; i < m; i++) {
            evaluations.add(new Evaluation(i + 1, (i + 1) + "星"));
        }

        // 要实现的逻辑是，poets 与 evaluations 做 join 获得 PoetExt集合

        // 显式双层迭代器循环嵌套的写法：
        List<PoetExt> poetExts = new ArrayList<>();
        System.out.println("=== 显式双层迭代器循环 ===");
        LocalDateTime startTime = LocalDateTime.now();
        for(Poet poet : poets) {
            int eva = poet.getEvaluation();
            for(Evaluation evaluation : evaluations) {
                if (eva == evaluation.getEvaluation()) {
                    PoetExt poetExt = new PoetExt(poet.getName(), poet.getAge(), eva, evaluation.getDescription());
                    poetExts.add(poetExt);
                    break;
                }
            }
        }
        LocalDateTime stopTime = LocalDateTime.now();
        System.out.println("显式双层迭代器循环 time(millis):" + Duration.between(startTime, stopTime).toMillis());
        System.out.printf("%s 的件数： %d 与第一件结果： %s %n", "显式双层迭代器循环", poetExts.size(), poetExts.get(0).toString());

        // Stream写法：
        System.out.println("=== Stream ===");
        startTime = LocalDateTime.now();
        poetExts = poets.stream()
                .map(poet -> {
                    Evaluation eva = evaluations.stream()
                            .filter(evaluation -> evaluation.getEvaluation() == poet.getEvaluation())
                            .findAny()
                            .orElseThrow();
                    return new PoetExt(poet.getName(), poet.getAge(), poet.getEvaluation(), eva.getDescription());
                })
                .collect(Collectors.toList());
        stopTime = LocalDateTime.now();
        System.out.println("Stream time(millis):" + Duration.between(startTime, stopTime).toMillis());
        System.out.printf("%s 的件数： %d 与第一件结果： %s %n", "Stream", poetExts.size(), poetExts.get(0).toString());

        // parallelStream
        System.out.println("=== parallelStream ===");
        startTime = LocalDateTime.now();
        poetExts = poets.parallelStream()
                .map(poet -> {
                    Evaluation eva = evaluations.parallelStream()
                            .filter(evaluation -> evaluation.getEvaluation() == poet.getEvaluation())
                            .findAny()
                            .orElseThrow();
                    return new PoetExt(poet.getName(), poet.getAge(), poet.getEvaluation(), eva.getDescription());
                })
                .collect(Collectors.toList());
        stopTime = LocalDateTime.now();
        System.out.println("parallelStream time(millis):" + Duration.between(startTime, stopTime).toMillis());
        System.out.printf("%s 的件数： %d 与第一件结果： %s %n", "parallelStream", poetExts.size(), poetExts.get(0).toString());
    }

    private void test06_pfm_multLoop() {
        // poets件数
        int n = 100000;
        // evaluations件数
        int m = 1000;
        List<Poet> poets = new ArrayList<>();
        for (int i = 0; i < n; i++) {
            String name = String.format("诗人%010d", i + 1);
            poets.add(new Poet(name, (int) (80 * Math.random()) + 10, (int) (m * Math.random()) + 1));
        }
        List<Evaluation> evaluations = new ArrayList<>();
        for (int i = 0; i < m; i++) {
            evaluations.add(new Evaluation(i + 1, (i + 1) + "星"));
        }

        // 将评价列表转换为Map
        Map<Integer, String> evaluationMap = evaluations.stream()
                .collect(Collectors.toMap(Evaluation::getEvaluation, Evaluation::getDescription));

        // 下面我们模拟这样一段逻辑：从 poets 中找到所有评价 > m/2 的诗人，把它们拼接为"诗人名:评价描述"的字段，然后再过滤掉"诗人名:评价描述"中不包含0的记录。

        // 虽然上述逻辑可以在一次循环中实现，但在实际开发中，往往有更复杂的逻辑导致我们经常按业务逻辑把它拆成数个循环处理。
        // 因此下面我们的模拟代码并未做一次循环搞定的优化。
        System.out.println("=== 多次循环实现数据转换逻辑 ===");
        LocalDateTime startTime = LocalDateTime.now();
        List<Poet> betterPoets = new ArrayList<>();
        for(Poet poet : poets) {
            if (poet.getEvaluation() > m / 2) {
                betterPoets.add(poet);
            }
        }
        List<String> poetWithEva2 = new ArrayList<>();
        for(Poet poet : betterPoets) {
            poetWithEva2.add(poet.getName() + ":" + evaluationMap.get(poet.getEvaluation()));
        }
        List<String> poetWithEva3 = new ArrayList<>();
        for(String s : poetWithEva2) {
            if (s != null && s.contains("0")) {
                poetWithEva3.add(s);
            }
        }
        LocalDateTime stopTime = LocalDateTime.now();
        System.out.println("多次循环实现数据转换逻辑 time(millis):" + Duration.between(startTime, stopTime).toMillis());

        System.out.println("=== Stream实现数据转换逻辑 ===");
        startTime = LocalDateTime.now();
        List<String> poetWithEva = poets.stream()
                .filter(poet -> poet.getEvaluation() > m / 2)
                .map(poet -> poet.getName() + ":" + evaluationMap.get(poet.getEvaluation()))
                .filter(s -> s.contains("0"))
                .collect(Collectors.toList());
        stopTime = LocalDateTime.now();
        System.out.println("Stream实现数据转换逻辑 time(millis):" + Duration.between(startTime, stopTime).toMillis());

        System.out.println("=== 一次循环实现数据转换逻辑 ===");
        startTime = LocalDateTime.now();
        List<String> lastLst = new ArrayList<>();
        for(Poet poet : poets) {
            if (poet.getEvaluation() > m / 2) {
                String tmp = poet.getName() + ":" + evaluationMap.get(poet.getEvaluation());
                if (tmp.contains("0")) {
                    lastLst.add(tmp);
                }
            }
        }
        stopTime = LocalDateTime.now();
        System.out.println("一次循环实现数据转换逻辑 time(millis):" + Duration.between(startTime, stopTime).toMillis());
    }

}
