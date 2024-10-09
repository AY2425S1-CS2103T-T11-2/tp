package seedu.address.model.assignment;

/**
 * The type Assignment.
 */
public class Assignment {
    public final String assignmentName;
    public final float score;

    /**
     * Instantiates a new Assignment.
     *
     * @param assignmentName the assignment name
     * @param score          the score
     */
    public Assignment(String assignmentName, float score) {
        this.assignmentName = assignmentName;
        this.score = score;
    }

    @Override
    public String toString() {
        return "Assignment:" + assignmentName + " " + score;
    }
}
