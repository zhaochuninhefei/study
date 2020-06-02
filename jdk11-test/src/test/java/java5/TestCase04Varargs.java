package java5;

/**
 * @author zhaochun
 */
public class TestCase04Varargs {
    public static void main(String[] args) {
        TestCase04Varargs me = new TestCase04Varargs();
        me.test01("One ring to rule them all,");
        me.test01("one ring to find them,", "One ring to bring them all ", "and in the darkness bind them.");
    }

    private void test01(String ... args) {
        for (String s : args) {
            System.out.println(s);
        }
    }
}
