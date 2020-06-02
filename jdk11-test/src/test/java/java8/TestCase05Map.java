package java8;

import java8.sub.Poet;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * @author zhaochun
 */
public class TestCase05Map {
    public static void main(String[] args) {
        TestCase05Map me = new TestCase05Map();
        me.test01_mapOperation();
    }

    private void test01_mapOperation() {
        List<Poet> poets = Poet.preparePoets();
        // 利用 Collectors.toMap 将Stream中的数据集转换为Map
        Map<String, Poet> poetMap = poets.stream().collect(Collectors.toMap(Poet::getName, poet -> poet));

        // Map没有stream() 方法，但有其他的新方法来支持日常使用

        // foreach
        poetMap.forEach((s, poet) -> {
            System.out.printf("%s 活了 %s 岁。 %n", s, poet.getAge());
            System.out.printf("%s 评价 : %s 。 %n", s, poet.getEvaluation());
        });

        // putIfAbsent 判断map中是否已经存在目标key，没有或为null的话put一个value进去。
        Poet censhen = poetMap.get("岑参");
        if (censhen == null) {
            censhen = new Poet("岑参", 51, 4);
            poetMap.put("岑参", censhen);
        }
        System.out.println(censhen);
        // 上面的代码现在可以直接使用 putIfAbsent 了。
        poetMap.putIfAbsent("岑参", new Poet("岑参", 51, 5));
        // 结果 "岑参" 的评价依旧是 4 而不是 5，因为 putIfAbsent 不会替换已经存在的value。
        System.out.println(poetMap.get("岑参"));

        // computeIfPresent : 如果指定键的值存在且非空，则尝试在给定键及其当前映射值的情况下计算新映射。
        // "岑参"已经加入了poetMap
        poetMap.computeIfPresent("岑参", (s, poet) -> new Poet(s, 51,4));
        // computeIfPresent会替换已经存在的value
        System.out.println(poetMap.get("岑参"));
        // "孟浩然"尚未加入poetMap
        poetMap.computeIfPresent("孟浩然", (s, poet) -> new Poet(s, 51,3));
        // computeIfPresent只在key已经存在时替换value
        System.out.println(poetMap.containsKey("孟浩然"));
        // computeIfAbsent 只在key不存在时put一个非空的value
        poetMap.computeIfAbsent("孟浩然", s -> new Poet(s, 51,3));
        System.out.println(poetMap.get("孟浩然"));
        // computeIfAbsent 与 putIfAbsent 区别在于传入参数不同，一个是lambda表达式，一个是具体的value。

        // 根据 key , value删除
        poetMap.remove("孟浩然", new Poet("孟浩然", 51,3));
        // 删除失败，因为value不是一个对象
        System.out.println(poetMap.containsKey("孟浩然"));
        poetMap.remove("孟浩然", poetMap.get("孟浩然"));
        // 删除成功
        System.out.println(poetMap.containsKey("孟浩然"));

        // getOrDefault
        System.out.println(poetMap.getOrDefault("孟浩然", new Poet("XX", 20, 1)));

        // merge
        Map<String, String> lines = new HashMap<>();
        lines.merge("杜甫名句", "星垂平野阔，", (value, newValue) -> value.concat(newValue));
        System.out.println(lines.get("杜甫名句"));
        lines.merge("杜甫名句", "月涌大江流。", String::concat);
        System.out.println(lines.get("杜甫名句"));
    }
}
