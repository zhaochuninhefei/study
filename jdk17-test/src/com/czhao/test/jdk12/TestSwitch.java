package com.czhao.test.jdk12;

/**
 * @author zhaochun
 */
public class TestSwitch {
    public static void main(String[] args) {
        TestSwitch me = new TestSwitch();
        me.test01("苏轼");
        me.test01("杜甫");
        me.test01("唐寅");
    }

    private void test01(String name) {
        switch (name) {
            case "李白":
            case "杜甫":
            case "白居易":
                System.out.println("唐代诗人");
                break;
            case "苏轼":
            case "辛弃疾":
                System.out.println("宋代诗人");
                break;
            default:
                System.out.println("其他朝代诗人");
        }
    }

    private int test02(String name) {
        switch (name) {
            case "李白":
            case "杜甫":
            case "白居易":
                int tmp = 1;
                return tmp;
            case "苏轼":
            case "辛弃疾":
                // 这里不能再定义一个 tmp 变量，因为与前面分支里定义的 tmp 冲突了，故而只能换个变量名
                int tmp2 = 2;
                return tmp2;
            default:
                int tmp3 = 3;
                return tmp3;
        }
    }

    private int test03(String name) {
        int tmp = 0;
        switch (name) {
            case "李白":
            case "杜甫":
            case "白居易":
                tmp = 1;
                break;
            case "苏轼":
            case "辛弃疾":
                tmp = 2;
                break;
            default:
                tmp = 3;
        }
        return tmp;
    }

    private int test04(String name) {
        int tmp = switch (name) {
            case "李白", "杜甫", "白居易" -> 1;
            case "苏轼", "辛弃疾" -> 2;
            default -> 3;
        };
        return tmp;
    }
}
