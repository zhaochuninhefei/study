package com.czhao.test.jdk15.hiddenclass;

import java.io.IOException;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.Method;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

/**
 * JEP 371: Hidden Classes
 *
 * @author zhaochun
 */
public class TestHiddenClass {

    public static void main(String[] args) {
        TestHiddenClass me = new TestHiddenClass();
//        me.printHiddenClassBytesInBase64();
        // me.printHiddenClassBytesInBase64() 的输出
        var hiddenClassBytesBase64 =  "yv66vgAAAD0ANgoAAgADBwAEDAAFAAYBABBqYXZhL2xhbmcvT2JqZWN0AQAGPGluaXQ+AQADKClWEgAAAAgMAAkACgEAF21ha2VDb25jYXRXaXRoQ29uc3RhbnRzAQAmKExqYXZhL2xhbmcvU3RyaW5nOylMamF2YS9sYW5nL1N0cmluZzsJAAwADQcADgwADwAQAQAQamF2YS9sYW5nL1N5c3RlbQEAA291dAEAFUxqYXZhL2lvL1ByaW50U3RyZWFtOwgAEgEAI0hlbGxvLCAlcyAhCkhlbGxvLCBIaWRkZW5DbGFzcyAhCiVuCgAUABUHABYMABcAGAEAE2phdmEvaW8vUHJpbnRTdHJlYW0BAAZwcmludGYBADwoTGphdmEvbGFuZy9TdHJpbmc7W0xqYXZhL2xhbmcvT2JqZWN0OylMamF2YS9pby9QcmludFN0cmVhbTsHABoBACxjb20vY3poYW8vdGVzdC9qZGsxNS9oaWRkZW5jbGFzcy9IaWRkZW5DbGFzcwEABENvZGUBAA9MaW5lTnVtYmVyVGFibGUBABJMb2NhbFZhcmlhYmxlVGFibGUBAAR0aGlzAQAuTGNvbS9jemhhby90ZXN0L2pkazE1L2hpZGRlbmNsYXNzL0hpZGRlbkNsYXNzOwEACHNheUhlbGxvAQAEbmFtZQEAEkxqYXZhL2xhbmcvU3RyaW5nOwEACnByaW50SGVsbG8BABUoTGphdmEvbGFuZy9TdHJpbmc7KVYBAApTb3VyY2VGaWxlAQAQSGlkZGVuQ2xhc3MuamF2YQEAEEJvb3RzdHJhcE1ldGhvZHMPBgApCgAqACsHACwMAAkALQEAJGphdmEvbGFuZy9pbnZva2UvU3RyaW5nQ29uY2F0RmFjdG9yeQEAmChMamF2YS9sYW5nL2ludm9rZS9NZXRob2RIYW5kbGVzJExvb2t1cDtMamF2YS9sYW5nL1N0cmluZztMamF2YS9sYW5nL2ludm9rZS9NZXRob2RUeXBlO0xqYXZhL2xhbmcvU3RyaW5nO1tMamF2YS9sYW5nL09iamVjdDspTGphdmEvbGFuZy9pbnZva2UvQ2FsbFNpdGU7CAAvAQAISGVsbG8sIAEBAAxJbm5lckNsYXNzZXMHADIBACVqYXZhL2xhbmcvaW52b2tlL01ldGhvZEhhbmRsZXMkTG9va3VwBwA0AQAeamF2YS9sYW5nL2ludm9rZS9NZXRob2RIYW5kbGVzAQAGTG9va3VwACEAGQACAAAAAAADAAEABQAGAAEAGwAAAC8AAQABAAAABSq3AAGxAAAAAgAcAAAABgABAAAACAAdAAAADAABAAAABQAeAB8AAAABACAACgABABsAAAA7AAEAAgAAAAcrugAHAACwAAAAAgAcAAAABgABAAAACgAdAAAAFgACAAAABwAeAB8AAAAAAAcAIQAiAAEACQAjACQAAQAbAAAAQAAGAAEAAAASsgALEhEEvQACWQMqU7YAE1exAAAAAgAcAAAACgACAAAADgARABIAHQAAAAwAAQAAABIAIQAiAAAAAwAlAAAAAgAmACcAAAAIAAEAKAABAC4AMAAAAAoAAQAxADMANQAZ";
        me.testInvokeHiddenClass(hiddenClassBytesBase64);
    }

    private void testInvokeHiddenClass(String hiddenClassBytesBase64) {
        // bytes 是模拟一个动态生成的class的字节数组。
        // hiddenClassBytesBase64 是其Base64编码，方便测试。
        byte[] bytes = Base64.getDecoder().decode(hiddenClassBytesBase64);
        try {
            // 使用 MethodHandles.lookup().defineHiddenClass 读取动态类字节数组，创建隐藏类
            Class<?> hiddenClass = MethodHandles.lookup().defineHiddenClass(bytes, true, MethodHandles.Lookup.ClassOption.NESTMATE)
                    .lookupClass();
            System.out.println("HiddenClass Name : " + hiddenClass.getName());

            // 反射得到初始化隐藏类实例
            Object hiddenClassObj = hiddenClass.getConstructors()[0].newInstance();
            // 遍历隐藏类所有方法，并反射执行
            for (Method method : hiddenClass.getDeclaredMethods()) {
                System.out.println("method name : " + method.getName());
                if ("sayHello".equals(method.getName())) {
                    System.out.println(method.invoke(hiddenClassObj, "zhaochun"));
                } else if ("printHello".equals(method.getName())) {
                    method.invoke(hiddenClassObj, "zhaochun");
                }
            }

            // 隐藏类中方法的另一种执行方式
            MethodHandle mhPrintHello = MethodHandles.lookup().findStatic(hiddenClass, "printHello", MethodType.methodType(void.class, String.class));
            mhPrintHello.invokeExact("zhaochun");

            MethodHandle mhSayHello = MethodHandles.lookup().findVirtual(hiddenClass, "sayHello", MethodType.methodType(String.class, String.class));
            System.out.println(mhSayHello.invoke(hiddenClassObj, "zhaochun"));

        } catch (Throwable e) {
            e.printStackTrace();
        }
    }

    private void printHiddenClassBytesInBase64() {
        String classPath = "/home/zhaochun/work/sources/study/jdk17-test/out/production/jdk17-test/com/czhao/test/jdk15/hiddenclass/HiddenClass.class";
        try {
            byte[] bytes = Files.readAllBytes(Paths.get(classPath));
            System.out.println(Base64.getEncoder().encodeToString(bytes));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
