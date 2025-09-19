package java8;

import java8.inf.Printer;
import java8.inf.Printer2;

/**
 * @author zhaochun
 */
public class TestCase03DefaultMethod {
    public static void main(String[] args) {
        TestCase03DefaultMethod me = new TestCase03DefaultMethod();
        me.test01();
    }

    private void test01() {
        PrintClass printClass = new PrintClass();
        printClass.print();
        printClass.printAnathor();

        PrintClass2 printClass2 = new PrintClass2();
        printClass2.print();
        printClass2.helloAndBye();
    }

    class PrintClass implements Printer {
    }

    class PrintClass2 implements Printer, Printer2 {
        @Override
        public void print() {
            System.out.println("相看两不厌");
            Printer2.super.print();
        }

        public void helloAndBye() {
            Printer2.printHello("Java8");
            Printer2.printBye("Java8");
        }

    }
}
