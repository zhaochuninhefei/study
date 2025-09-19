package java8.inf;

public interface Printer2 {
    default void print() {
        System.out.println("只有敬亭山");
    }

    static void printHello(String name) {
        System.out.println("Hello " + name);
    }

    static void printBye(String name) {
        System.out.println("Goodbye " + name);
    }
}
