package com.czhao.test.jdk15;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Base64;

/**
 * @author zhaochun
 */
public class TestHiddenClass {

    public static void main(String[] args) {
        TestHiddenClass me = new TestHiddenClass();
//        me.printHiddenClassBytesInBase64();
        // me.printHiddenClassBytesInBase64() 的输出
        var hiddenClassBytesBase64 =  "yv66vgAAAD0ANgoAAgADBwAEDAAFAAYBABBqYXZhL2xhbmcvT2JqZWN0AQAGPGluaXQ+AQADKClWEgAAAAgMAAkACgEAF21ha2VDb25jYXRXaXRoQ29uc3RhbnRzAQAmKExqYXZhL2xhbmcvU3RyaW5nOylMamF2YS9sYW5nL1N0cmluZzsJAAwADQcADgwADwAQAQAQamF2YS9sYW5nL1N5c3RlbQEAA291dAEAFUxqYXZhL2lvL1ByaW50U3RyZWFtOwgAEgEAI0hlbGxvLCAlcyAhCkhlbGxvLCBIaWRkZW5DbGFzcyAhCiVuCgAUABUHABYMABcAGAEAE2phdmEvaW8vUHJpbnRTdHJlYW0BAAZwcmludGYBADwoTGphdmEvbGFuZy9TdHJpbmc7W0xqYXZhL2xhbmcvT2JqZWN0OylMamF2YS9pby9QcmludFN0cmVhbTsHABoBACxjb20vY3poYW8vdGVzdC9qZGsxNS9oaWRkZW5jbGFzcy9IaWRkZW5DbGFzcwEABENvZGUBAA9MaW5lTnVtYmVyVGFibGUBABJMb2NhbFZhcmlhYmxlVGFibGUBAAR0aGlzAQAuTGNvbS9jemhhby90ZXN0L2pkazE1L2hpZGRlbmNsYXNzL0hpZGRlbkNsYXNzOwEACHNheUhlbGxvAQAEbmFtZQEAEkxqYXZhL2xhbmcvU3RyaW5nOwEACnByaW50SGVsbG8BABUoTGphdmEvbGFuZy9TdHJpbmc7KVYBAApTb3VyY2VGaWxlAQAQSGlkZGVuQ2xhc3MuamF2YQEAEEJvb3RzdHJhcE1ldGhvZHMPBgApCgAqACsHACwMAAkALQEAJGphdmEvbGFuZy9pbnZva2UvU3RyaW5nQ29uY2F0RmFjdG9yeQEAmChMamF2YS9sYW5nL2ludm9rZS9NZXRob2RIYW5kbGVzJExvb2t1cDtMamF2YS9sYW5nL1N0cmluZztMamF2YS9sYW5nL2ludm9rZS9NZXRob2RUeXBlO0xqYXZhL2xhbmcvU3RyaW5nO1tMamF2YS9sYW5nL09iamVjdDspTGphdmEvbGFuZy9pbnZva2UvQ2FsbFNpdGU7CAAvAQAISGVsbG8sIAEBAAxJbm5lckNsYXNzZXMHADIBACVqYXZhL2xhbmcvaW52b2tlL01ldGhvZEhhbmRsZXMkTG9va3VwBwA0AQAeamF2YS9sYW5nL2ludm9rZS9NZXRob2RIYW5kbGVzAQAGTG9va3VwACEAGQACAAAAAAADAAEABQAGAAEAGwAAAC8AAQABAAAABSq3AAGxAAAAAgAcAAAABgABAAAACAAdAAAADAABAAAABQAeAB8AAAABACAACgABABsAAAA7AAEAAgAAAAcrugAHAACwAAAAAgAcAAAABgABAAAACgAdAAAAFgACAAAABwAeAB8AAAAAAAcAIQAiAAEACQAjACQAAQAbAAAAQAAGAAEAAAASsgALEhEEvQACWQMqU7YAE1exAAAAAgAcAAAACgACAAAADgARABIAHQAAAAwAAQAAABIAIQAiAAAAAwAlAAAAAgAmACcAAAAIAAEAKAABAC4AMAAAAAoAAQAxADMANQAZ";
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
