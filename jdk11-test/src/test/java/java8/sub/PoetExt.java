package java8.sub;

/**
 * @author zhaochun
 */
public class PoetExt extends Poet {
    private String description;

    public PoetExt(String name, int age, int evaluation, String description) {
        super(name, age, evaluation);
        this.description = description;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    @Override
    public String toString() {
        return "PoetExt{" +
                "name='" + this.getName() + '\'' +
                ", description='" + description + '\'' +
                '}';
    }
}
