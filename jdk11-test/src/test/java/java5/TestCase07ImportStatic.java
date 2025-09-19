package java5;

import static java5.TestCase07ImportStatic.TestInner.test;
import static java.lang.System.out;
import static java.lang.Integer.*;

/**
 * @author zhaochun
 */
public class TestCase07ImportStatic {
    public static void main(String[] args) {
        test();
        out.println(MIN_VALUE);
        out.println(toBinaryString(100));
    }

    static class TestInner {
        public static void test() {
            System.out.println("TestInner");
        }
    }
}
