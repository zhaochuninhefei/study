package com.czhao.test.jdk14;

/**
 * JEP 368: Text Blocks (Second Preview)
 *
 * @author zhaochun
 */
public class TestTextBlock368 {
    public static void main(String[] args) {
        TestTextBlock368 me = new TestTextBlock368();
        me.test01();
    }

    private void test01() {
        String inOneLine = """
                山一程，\
                水一程，\
                身向榆关那畔行，\
                夜深千帐灯。
                风一更，\
                雪一更，\
                聒碎乡心梦不成，\
                故园无此声。
                """;
        System.out.println(inOneLine);

        String singleSpace = """
                山一程\s，
                水一程\s，
                身向榆关那畔行\s，
                夜深千帐灯\s。
                风一更\s，
                雪一更\s，
                聒碎乡心梦不成\s，
                故园无此声\s。
                """;
        System.out.println(singleSpace);

        String type = "String";
        String code = """
              public void print(""" + type + """
                                                 o) {
                  System.out.println(Objects.toString(o));
              }
              """;
        System.out.println(code);
    }
}
