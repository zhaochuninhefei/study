package java8.sub;

import java.util.ArrayList;
import java.util.List;

/**
 * @author zhaochun
 */
public class Poet {
    private String name;
    private int age;
    private int evaluation;

    public Poet() {
    }

    public Poet(String name, int age, int evaluation) {
        this.name = name;
        this.age = age;
        this.evaluation = evaluation;
    }

    @Override
    public String toString() {
        return "Poet{" +
                "name='" + name + '\'' +
                ", age=" + age +
                ", evaluation=" + evaluation +
                '}';
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public int getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(int evaluation) {
        this.evaluation = evaluation;
    }

    public static List<Poet> preparePoets() {
        List<Poet> poets = new ArrayList<>();
        // 年龄未必准确，评价不能当真
        poets.add(new Poet("王维", 61, 4));
        poets.add(new Poet("李白", 61, 5));
        poets.add(new Poet("杜甫", 58, 5));
        poets.add(new Poet("白居易", 74, 4));
        poets.add(new Poet("李商隐", 45, 4));
        poets.add(new Poet("杜牧", 50, 4));
        poets.add(new Poet("李贺", 26, 4));
        return poets;
    }
}
