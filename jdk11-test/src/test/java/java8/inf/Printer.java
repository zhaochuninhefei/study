package java8.inf;

public interface Printer {
    default void print() {
        System.out.println("众鸟高飞尽");
    }

    default void printAnathor() {
        System.out.println("孤云独去闲");
    }
}
