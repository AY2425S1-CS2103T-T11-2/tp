package seedu.address.model.assignment;

/**
 * Class to hold the data read from assignment storage at run time.
 */
public record PredefinedAssignment(String name, Float maxScore) {

    @Override
    public boolean equals(Object obj) {
        if (obj instanceof PredefinedAssignment other) {
            return name.equals(other.name) && maxScore.equals(other.maxScore());
        }
        return false;
    }
}
