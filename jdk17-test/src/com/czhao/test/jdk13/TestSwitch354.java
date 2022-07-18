package com.czhao.test.jdk13;

/**
 * JEP 354: Switch Expressions (Second Preview)
 *
 * @author zhaochun
 */
public class TestSwitch354 {
    public static void main(String[] args) {
        TestSwitch354 me = new TestSwitch354();
        me.test01("苏轼");
        me.test01("杜甫");
        me.test01("唐寅");
    }

    private void test01(String name) {
        int dynasty = switch(name){
            case "李白", "杜甫", "白居易" -> {
                System.out.println("唐代诗人");
                yield 1;
            }
            case "苏轼", "辛弃疾" -> {
                System.out.println("宋代诗人");
                yield 2;
            }
            default -> {
                System.out.println("其他朝代诗人");
                yield 3;
            }
        };
        System.out.println(dynasty);
    }
}
