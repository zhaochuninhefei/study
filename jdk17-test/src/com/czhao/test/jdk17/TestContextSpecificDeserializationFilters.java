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
        Person person = new Person("张三", 20, new PersonID("xxx001"));
        byte[] bytes = me.writePerson(person);
        me.readPersonWithoutFilter(bytes);
        me.readPersonWithFilter(bytes,
                "com.czhao.test.jdk17.TestContextSpecificDeserializationFilters$Person",
                "com.czhao.test.jdk17.TestContextSpecificDeserializationFilters$PersonID");
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
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        try (ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream)) {
            Object object = objectInputStream.readObject();
            System.out.println(object.toString());
        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
        }
    }

    private void readPersonWithFilter(byte[] bytes, String... classPtns) {
        ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bytes);
        try (ObjectInputStream objectInputStream = new ObjectInputStream(byteArrayInputStream)) {
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
