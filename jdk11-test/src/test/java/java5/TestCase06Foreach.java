package java5;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhaochun
 */
public class TestCase06Foreach {
    public static void main(String[] args) {
        List<Integer> numbers = new ArrayList<Integer>();
        for (int i = 0; i < 10; i++) {
            numbers.add(i + 1);
        }

        for(Integer number : numbers) {
            System.out.println(number);
        }
    }
}
