package jdk11;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.Collectors;

/**
 * 本地变量类型推断
 *
 * @author zhaochun
 */
public class TestCase01TypeInference {
    public static void main(String[] args) {
        TestCase01TypeInference me = new TestCase01TypeInference();
        me.testVar();
    }

    private void testVar() {
        // Java10以后可以用var定义一个局部变量，不用显式写出它的类型。
        String strBeforeJava10 = "strBeforeJava10";
        var strFromJava10 = "strFromJava10";
        System.out.println(strBeforeJava10);
        System.out.println(strFromJava10);

        // 但要注意，被var定义的变量仍然是静态类型，编译器会试图去推断其类型。
        // 因此不兼容的类型是不能重新赋值的。
        // 例如下面的语句编译会失败，"InCompatible types."
//        strFromJava10 = 10;

        // 只要编译器无法推断出变量类型，就会编译错误！
        // 例如下面这些都无法通过编译:
//        var testVarWithoutInitial;
//        var testNull = null;
//        var testLamda = () -> System.out.println("test");
//        var testMethodByLamda = () -> giveMeString();
//        var testMethod2 = this::giveMeString;

        // 局部变量类型推断可以用于简化泛型声明。如下所示，Map <String，List <Integer >>类型，可以被简化为单个var关键字，从而避免大量样板代码：
        var testList = new ArrayList<Map<String, List<Integer>>>();
        for (var curEle : testList) {
            // curEle能够被推断出类型是 Map<String, List<Integer>>
            if (curEle != null) {
                curEle.put("test", new ArrayList<>());
            }
        }

        // 从Java 11开始，lambda参数也允许使用var关键字：
        Predicate<String> predNotNull = (var a) -> a != null && a.trim().length() > 0;
        String strAfterFilter = Arrays.stream((new String[]{"a", "", null, "x"}))
                .filter(predNotNull)
                .collect(Collectors.joining(","));
        System.out.println(strAfterFilter);
    }

    private String giveMeString() {
        return "a string.";
    }
}
