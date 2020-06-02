package java8;

import java.util.*;
import java.util.function.Supplier;

/**
 * @author zhaochun
 */
public class TestCase01Lambda {
    public static void main(String[] args) {
        TestCase01Lambda me = new TestCase01Lambda();
        me.test01_simpleLambda();
        me.test02_finalVars();
        me.test03_methodReference();
    }

    private void test03_methodReference() {
        /* java8为一些简单的lambda表达式提供了更简单的写法：方法引用 */

        // 特定对象的方法引用 instance::method
        new Thread(this::test02_finalVars).start();
        // 上面这句等价于下面这句：
//        new Thread(() -> this.test02_finalVars()).start();

        // 静态方法引用 Class::static_method
        new Thread(TestCase01Lambda::printSomething).start();
        // 等价于:
//        new Thread(() -> TestCase01Lambda.printSomething()).start();

        // 特定类的任意对象的方法引用 Class::method
        List<String> lines = new ArrayList<>();
        lines.add("a005");
        lines.add("a001");
        lines.add("a003");
        Collections.sort(lines, String::compareTo);
        // 等价于：
//        Collections.sort(lines, (o1, o2) -> o1.compareTo(o2));
        System.out.println(lines);

        // 构造器引用 Class< T >::new
        Set<String> lineSet = transferElements(lines, HashSet::new);
        // 等价于
//        lineSet = transferElements(lines, () -> new HashSet<>());
        System.out.println(lineSet);
    }

    private void test02_finalVars() {
        String a = "王维";
        new Thread(() -> {
            // lambda表达式里可以使用外部的final局部变量(不用显式声明final)
            System.out.println(a);
            // 下面这句编译不过，不能对"lambda表达式里使用的外部局部变量"重新赋值。
            // 即lambda内部使用的外部局部变量是隐式final的。
//            a = "李白";
        }).start();
        // 在lambda外面也不能对a重新赋值，因为需要在lambda表达式里使用，因此a是隐式final的。
//        a = "李白";
    }

    private void test01_simpleLambda() {
        // Java8之前经常使用匿名内部类，很臃肿，语义不直观
        String s1 = joinStr(new TestLambda() {
            @Override
            public String join(String a, String b) {
                return a + ", " + b;
            }
        }, "问君能有几多愁", "恰似一江春水向东流");
        System.out.println(s1);

        // Java8开始使用lambda表达式，很简洁，语义直观，更接近自然语言
        TestLambda simpleJoin = (a, b) -> a + ", " + b;
        String s2 = joinStr(simpleJoin, "高堂明镜悲白发", "朝如青丝暮成雪");
        System.out.println(s2);
        // 或直接写为:
        String s3 = joinStr((a, b) -> a + ", " + b, "高堂明镜悲白发", "朝如青丝暮成雪");
        System.out.println(s3);

        // 可以声明类型，逻辑实现复杂时，用 {} 包起来
        TestLambda joinWithCheck = (String a, String b) -> {
            if (a != null && b != null) {
                return a + ", " + b;
            } else {
                return "空空如也";
            }
        };
        String s4 = joinStr(joinWithCheck, null, null);
        System.out.println(s4);
    }

    private String joinStr(TestLambda testLambda, String a, String b) {
        return testLambda.join(a, b);
    }

    @FunctionalInterface
    interface TestLambda {
        String join(String a, String b);
    }

    private static void printSomething() {
        System.out.println("大漠孤烟直，长河落日圆。");
    }

    private static <T, SOURCE extends Collection<T>, DEST extends Collection<T>> DEST transferElements(
            SOURCE sourceCollection,
            Supplier<DEST> collectionFactory) {

        DEST result = collectionFactory.get();
        result.addAll(sourceCollection);
        return result;
    }
}
