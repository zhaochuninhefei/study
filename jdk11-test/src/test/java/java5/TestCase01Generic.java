package java5;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * @author zhaochun
 */
public class TestCase01Generic {
    public static void main(String[] args) {
        TestCase01Generic me = new TestCase01Generic();

        List<String> lst01 = new ArrayList<String>();
        lst01.add("独坐幽篁里，");
        lst01.add("弹琴复长啸。");
        lst01.add("深林人不知，");
        lst01.add("明月来相照。");
        me.test01(lst01);

        class subTest extends TestCase01Generic {
            @Override
            public void doSomething() {
                System.out.println("竹里馆");
            }
        }

        me.test02(new subTest());
    }

    // 用 ? 表示接受任何类型，可以避免调用方法时类型检查警告。
    private void test01(List<?> list) {
        for (Iterator<?> i = list.iterator(); i.hasNext(); ) {
            System.out.println((i.next().toString()));
        }
    }

    // 限制类型，此处表示参数类型必须继承TestCase01Generic
    private <T extends TestCase01Generic> void test02(T t) {
        t.doSomething();
    }

    public void doSomething() {
        System.out.println("TestCase01Generic.");
    }
}
