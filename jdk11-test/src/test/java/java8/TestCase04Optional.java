package java8;

import java.util.NoSuchElementException;
import java.util.Optional;

/**
 * @author zhaochun
 */
public class TestCase04Optional {
    public static void main(String[] args) {
        TestCase04Optional me = new TestCase04Optional();
        me.test01_simple("天生我材必有用");
        me.test01_simple(null);

        me.test02_returnOptional("天生我材必有用");
        me.test02_returnOptional(null);

        System.out.println("over");
    }

    private void test01_simple(String line) {
        if (line != null) {
            System.out.println(line.trim());
        } else {
            try {
                Optional<String> line1 = Optional.of(line);
            } catch (NullPointerException e) {
                System.out.println("Optional.of 不能传入null!");
            }

            Optional<String> empty = Optional.ofNullable(line);
            System.out.println("Optional.ofNullable 可以传入null");
        }

        try {
            // 这是一个不好的使用范例
            printLine(Optional.ofNullable(line));
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }

    // 不推荐将参数类型设计为Optional，Optional适合用于返回值类型
    private void printLine(Optional<String> line) {
        try {
            System.out.println(line.get().trim());
        } catch (NoSuchElementException e) {
            System.out.println("Optional.get 如果line是null，get会抛NoSuchElementException异常！");
        }
        // 仅在原来对象非null时执行传入的lambda表达式
        line.ifPresent(s -> System.out.println(s.trim()));
        // 利用orElse，当原来对象是null时，使用orElse传入的默认值
        System.out.println(line.orElse(""));
        // 利用orElseGet，当原来对象是null时，使用orElseGet传入的lambda表达式
        System.out.println(line.orElseGet(() -> "天生我材必有用，" + "千金散尽还复来。"));
        // 利用orElseThrow，当原来对象是null时，抛出自己定义的异常
        System.out.println(line.orElseThrow(() -> new RuntimeException("也可以抛出自己定义的异常！")));
    }

    // 使用Optional的正确打开姿势
    private void test02_returnOptional(String line) {
        Optional<String> lineOpt = createLineOptional(line);

        // 仅在原来对象非null时执行传入的lambda表达式
        lineOpt.ifPresent(s -> System.out.println(s.trim()));
        // 利用orElse，当原来对象是null时，使用orElse传入的默认值
        System.out.println(lineOpt.orElse(""));
        // 利用orElseGet，当原来对象是null时，使用orElseGet传入的lambda表达式
        System.out.println(lineOpt.orElseGet(() -> "天生我材必有用，" + "千金散尽还复来。"));
        // 利用orElseThrow，当原来对象是null时，抛出自己定义的异常
        System.out.println(lineOpt.orElseThrow(() -> new RuntimeException("也可以抛出自己定义的异常！")));
    }

    private Optional<String> createLineOptional(String line) {
        // 实际开发中，这里也许会有比较复杂的逻辑，用于返回一个对象，而该方法不保证返回对象不为null;
        // 因此使用该方法的地方必须判断返回值是否为null。。。
        // 但如果我们将返回值用Optional包起来，那么对于调用该方法的地方而言，非空判断就可以很优雅了。
        return Optional.ofNullable(line);
    }

}
