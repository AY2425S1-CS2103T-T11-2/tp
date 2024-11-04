package seedu.address.logic.parser;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Stream;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.assignment.Assignment;

/**
 * Parses assignment data from a CSV field, with each assignment represented as "assignmentName | score".
 * The parser validates assignment names and scores and maps them to Assignment objects.
 */
public class CsvAssignmentParser {

    /**
     * Parses the assignments field, where each assignment is represented as "assignmentName | score".
     *
     * @param assignmentField string representing assignments and their scores
     * @param model           the application model
     * @return a map of assignment names to Assignment objects
     * @throws CommandException if assignment name or score is invalid
     */
    public static Map<String, Assignment> parseAssignment(String assignmentField, Model model)
            throws NumberFormatException, CommandException {
        Map<String, Assignment> assignments = new HashMap<>();
        assignmentField = assignmentField.trim();

        if (!assignmentField.isEmpty()) {
            String[] assignmentArray = assignmentField.split(",");
            for (String assignment : assignmentArray) {
                processAssignment(assignment.trim(), assignments, model);
            }
        }
        return assignments;
    }

    /**
     * Processes each assignment entry by validating the name and score, and adds it to the assignments map.
     *
     * @param assignment  string representing a single assignment
     * @param assignments map to store validated assignments
     * @param model       the application model
     * @throws CommandException if assignment name or score is invalid
     */
    public static void processAssignment(String assignment, Map<String, Assignment> assignments, Model model)
            throws CommandException {
        List<String> individual = Stream.of(assignment.split("\\|"))
            .map(String::trim).toList(); // | used as delimiter between name and score

        // Get assignment score and name from each individual assignment
        String asgnName = individual.get(0);
        float asgnScore = Float.parseFloat(individual.get(1));

        // Throws Command exception if assignment name is invalid
        if (!model.hasAssignment(asgnName)) {
            throw new CommandException("Invalid assignment name: " + asgnName);
        }

        // Throws Command exception if score is invalid (not parser exception because this happens during execution)
        if (asgnScore > model.maxScore(asgnName) || asgnScore < 0) {
            throw new CommandException("Score must be between 0.0 and " + model.maxScore(asgnName));
        }

        assignments.put(asgnName, new Assignment(asgnName, asgnScore));
    }
}
