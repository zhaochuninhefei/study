package com.czhao.test.jdk15;

import java.util.List;
import java.util.stream.Collectors;

/**
 * JEP 384: Records (Second Preview)
 *
 * @author zhaochun
 */
public class TestRecord384 {

    public sealed interface Expr
            permits ConstantExpr, PlusExpr, TimesExpr, NegExpr {
    }

    public record ConstantExpr(int i) implements Expr {
    }

    public record PlusExpr(Expr a, Expr b) implements Expr {
    }

    public record TimesExpr(Expr a, Expr b) implements Expr {
    }

    public record NegExpr(Expr e) implements Expr {
    }

    private List<Integer> findMaxNumber(List<String> numbers) {
        // 声明一个本地Record
        record NumberRecord(String numberStr, int number) {
        }

        return numbers.stream()
                .map(num -> new NumberRecord(num, Integer.parseInt(num)))
                .sorted((n1, n2) -> Integer.compare(n2.number(), n1.number()))
                .map(NumberRecord::number)
                .collect(Collectors.toList());
    }

    record NewPoint(@Deprecated int x, @Deprecated int y, Integer px, Integer py) {
    }

    public static void main(String[] args) {
        TestRecord384 me = new TestRecord384();
        NewPoint np1 = new NewPoint(0, 0, 10, 20);
        NewPoint np2 = new NewPoint(0, 0, 10, 20);
        System.out.println(np1.toString());
        System.out.println(np1.equals(np2));
        System.out.println(np1.x());
    }

}
