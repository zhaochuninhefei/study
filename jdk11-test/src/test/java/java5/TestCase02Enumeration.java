package java5;

/**
 * @author zhaochun
 */
public class TestCase02Enumeration {
    public static void main(String[] args) {
        TestCase02Enumeration me = new TestCase02Enumeration();
        me.test01(Color.red);
    }

    private void test01(Color color) {
        switch (color) {
            case red:
                System.out.println("霜叶红于二月花");
                break;
            case black:
                System.out.println("黑云压城城欲摧");
                break;
            case white:
                System.out.println("一行白鹭上青天");
                break;
            case yellow:
                System.out.println("故人西辞黄鹤楼");
        }

        System.out.println(Color.black.compareTo(color));
        System.out.println(Color.white.compareTo(color));
        System.out.println(Color.red.compareTo(color));
        System.out.println(Color.yellow.compareTo(color));
    }

    enum Color {
        black, white, red, yellow
    }
}
