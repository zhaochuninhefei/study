package jdk11;

import java.util.Arrays;
import java.util.Optional;

/**
 * Optional增强
 *
 * @author zhaochun
 */
public class TestCase05Optional {
    public static void main(String[] args) {
        TestCase05Optional me = new TestCase05Optional();
        me.test01_2stream_default();
    }

    private void test01_2stream_default() {
        // 可以将Optional对象直接转为stream
        Optional.of("Hello openJDK11").stream()
                .flatMap(s -> Arrays.stream(s.split(" ")))
                .forEach(System.out::println);

        // 可以为Optional对象提供一个默认的Optional对象
        System.out.println(Optional.empty()
                .or(() -> Optional.of("default"))
                .get());
    }

}
