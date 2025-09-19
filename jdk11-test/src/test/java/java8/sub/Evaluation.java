package java8.sub;

/**
 * @author zhaochun
 */
public class Evaluation {
    private int evaluation;
    private String description;

    public Evaluation() {
    }

    public Evaluation(int evaluation, String description) {
        this.evaluation = evaluation;
        this.description = description;
    }

    public int getEvaluation() {
        return evaluation;
    }

    public void setEvaluation(int evaluation) {
        this.evaluation = evaluation;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }
}
