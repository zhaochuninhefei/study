package java5;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhaochun
 */
public class TestCase03Autoboxing {
    public static void main(String[] args) {
        List<Integer> lstInt = new ArrayList<Integer>();
        lstInt.add(1);
        lstInt.add(2);
        lstInt.add(3);

        for (int i = 0; i < lstInt.size(); i++) {
            System.out.println(lstInt.get(i).toString());
            System.out.println(lstInt.get(i) + 1);
        }
    }
}
