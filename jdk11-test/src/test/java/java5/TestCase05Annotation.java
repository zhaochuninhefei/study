package java5;

import java.lang.annotation.*;

/**
 * @author zhaochun
 */
public class TestCase05Annotation {

    public static void main(String[] args) {
        TestCase05Annotation me = new TestCase05Annotation();
        System.out.println(me.toString());
    }

    // 编译器看到 @Override 注解，就知道这个方法须是重写父类的方法
    // 因此会严格检查方法声明信息是否与父类对应方法相同
    // 如返回值类型，参数列表等等
    @Override
    public String toString() {
        return "解落三秋叶，能开二月花。";
    }

    // 一个自定义注解的例子，用于AOP中对方法参数进行非空检查
    @Target(ElementType.PARAMETER)
    @Retention(RetentionPolicy.RUNTIME)
    @Documented
    public @interface ParamNotEmpty {
    }
}
