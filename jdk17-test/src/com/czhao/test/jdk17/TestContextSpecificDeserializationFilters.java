package com.czhao.test.jdk17;

import java.io.*;

/**
 * JEP 415:	Context-Specific Deserialization Filters
 *
 * @author zhaochun
 */
public class TestContextSpecificDeserializationFilters {
    public static void main(String[] args) {
        TestContextSpecificDeserializationFilters me = new TestContextSpecificDeserializationFilters();
        // 创建对象 person
        Person person = new Person("张三", 20, new PersonID("xxx001"));
        // 序列化为字节流
        byte[] bytes = me.writePerson(person);
        // 没有过滤器的反序列化
        me.readPersonWithoutFilter(bytes);
        // 带有过滤器的反序列化，两个内部类Person与PersonID都允许反序列化
        me.readPersonWithFilter(bytes,
                "com.czhao.test.jdk17.TestContextSpecificDeserializationFilters$Person",
                "com.czhao.test.jdk17.TestContextSpecificDeserializationFilters$PersonID");
        // 带有过滤器的反序列化，只有一个内部类Person允许反序列化
        // 会抛出异常: java.io.InvalidClassException: filter status: REJECTED
        me.readPersonWithFilter(bytes,
                "com.czhao.test.jdk17.TestContextSpecificDeserializationFilters$Person");
    }

    private byte[] writePerson(Person person) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
        try (ObjectOutputStream oos = new ObjectOutputStream(byteArrayOutputStream)) {
            oos.writeObject(person);
        } catch (IOException e) {
            e.printStackTrace();
        }
        return byteArrayOutputStream.toByteArray();
    }

    private void readPersonWithoutFilter(byte[] bytes) {
        try (ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(bytes))) {
            Object object = objectInputStream.readObject();
            System.out.println(object.toString());
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void readPersonWithFilter(byte[] bytes, String... classPtns) {
        try (ObjectInputStream objectInputStream = new ObjectInputStream(new ByteArrayInputStream(bytes))) {
            // 编写过滤条件: 允许 classPtns 指定的所有类，允许`java.base`模块下的所有类，禁止其他类。
            String ptn = String.join(";", classPtns) + ";java.base/*;!*";
            ObjectInputFilter filter = ObjectInputFilter.Config.createFilter(ptn);
            objectInputStream.setObjectInputFilter(filter);
            Object object = objectInputStream.readObject();
            System.out.println(object.toString());
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    record Person(String name, Integer age, PersonID personID) implements Serializable {
    }

    record PersonID(String id) implements Serializable {
    }
}
