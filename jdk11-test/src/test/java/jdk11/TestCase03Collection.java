package jdk11;

import java.util.*;

/**
 * Collection增强
 *
 * @author zhaochun
 */
public class TestCase03Collection {
    public static void main(String[] args) {
        TestCase03Collection me = new TestCase03Collection();
        me.test01_of_copyOf();
    }

    private void test01_of_copyOf() {
        // List,Set,Map有了新的增强方法。

        // List.of根据传入的参数列表创建一个新的不可变List集合；
        // List.copyOf根据传入的list对象创建一个不可变副本。
        var listImmutable = List.of("a", "b", "c");
        var listImmutableCopy = List.copyOf(listImmutable);
        // 由于拷贝的集合本身就是一个不可变对象，因此拷贝实际上并没有创建新的对象，直接使用了原来的不可变对象。
        System.out.println(listImmutable == listImmutableCopy);
        // 不可变对象不能进行修改
        try {
            listImmutable.add("d");
        } catch (Throwable t) {
            System.out.println("listImmutable can not be modified!");
        }
        try {
            listImmutableCopy.add("d");
        } catch (Throwable t) {
            System.out.println("listImmutableCopy can not be modified!");
        }

        // 如果想快速新建一个可变的集合对象，可以直接使用之前的不可变集合作为构造参数，创建一个新的可变集合。
        var listVariable = new ArrayList<>(listImmutable);
        var listVariableCopy = List.copyOf(listVariable);
        // 新创建的可变集合当然是一个新的对象，从这个新对象拷贝出来的不可变副本也是一个新的对象，并不是之前的不可变集合。
        System.out.println(listVariable == listImmutable);
        System.out.println(listVariable == listVariableCopy);
        System.out.println(listImmutable == listVariableCopy);
        // 新的可变集合当然是可以修改的
        try {
            listVariable.add("d");
        } catch (Throwable t) {
            System.out.println("listVariable can not be modified!");
        }
        // 可变集合拷贝出来的副本依然是不可变的
        try {
            listVariableCopy.add("d");
        } catch (Throwable t) {
            System.out.println("listVariableCopy can not be modified!");
        }

        // Set的of和copyOf与List类似。
        var set = Set.of("a", "c", "r", "e");
        var setCopy = Set.copyOf(set);
        System.out.println(set == setCopy);
        // 但要注意，用of创建不可变Set时，要确保元素不重复，否则运行时会抛出异常: "java.lang.IllegalArgumentException: duplicate element"
        try {
            var setErr = Set.of("a", "b", "a");
        } catch (Throwable t) {
            t.printStackTrace();
        }
        // 当然创建可变set后添加重复元素不会抛出异常，但会被去重
        var setNew = new HashSet<>(set);
        setNew.add("c");
        System.out.println(setNew.toString());

        // Map的of和copyOf与list,set类似
        var map = Map.of("a", 1, "b", 2);
        var mapCopy = Map.copyOf(map);
        System.out.println(map == mapCopy);
        // 当然也要注意创建不可变Map时，key不能重复
        try {
            var mapErr = Map.of("a", 1, "b", 2, "a", 3);
        } catch (Throwable t) {
            t.printStackTrace();
        }
    }
}
