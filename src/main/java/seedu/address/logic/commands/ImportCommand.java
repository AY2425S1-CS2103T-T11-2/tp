package seedu.address.logic.commands;

import static seedu.address.logic.parser.CliSyntax.PREFIX_PATH;

import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import com.opencsv.CSVReader;
import com.opencsv.exceptions.CsvValidationException;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.assignment.Assignment;
import seedu.address.model.person.Address;
import seedu.address.model.person.Email;
import seedu.address.model.person.Github;
import seedu.address.model.person.Name;
import seedu.address.model.person.Person;
import seedu.address.model.person.Phone;
import seedu.address.model.person.Telegram;
import seedu.address.model.tag.Tag;

/**
 * Import command to load data from specified CSV file location.
 */
public class ImportCommand extends Command {

    public static final String COMMAND_WORD = "import";
    public static final String MESSAGE_USAGE = COMMAND_WORD + ": Imports person data from a CSV file.\n"
        + "Parameters: FILE_PATH\n"
        + "Example: " + COMMAND_WORD + " " + PREFIX_PATH + "data/persons.csv";

    private final String csvFilePath;

    public ImportCommand(String csvFilePath) {
        this.csvFilePath = csvFilePath;
    }

    @Override
    public CommandResult execute(Model model) throws CommandException {
        List<Person> newPersons = new ArrayList<>();
        try (CSVReader csvReader = new CSVReader(new FileReader(csvFilePath))) {
            String[] fields;

            // Skip header line
            csvReader.readNext();

            while ((fields = csvReader.readNext()) != null) {
                if (fields.length < 9) {
                    throw new CommandException("Invalid CSV format.");
                }
                Person person = parsePerson(fields);
                newPersons.add(person);
            }
        } catch (IOException | CsvValidationException e) {
            throw new CommandException("Error reading from the CSV file: " + e.getMessage());
        }
        model.replaceAllPersons(newPersons);

        return new CommandResult(String.format("Successfully imported %d persons.", newPersons.size()));
    }


    /**
     * Parses a person from CSV fields.
     */
    private Person parsePerson(String[] fields) throws CommandException {
        try {
            Name name = new Name(fields[0].trim());
            Phone phone = new Phone(fields[1].trim());
            Email email = new Email(fields[2].trim());
            Address address = new Address(fields[3].trim());
            Telegram telegram = new Telegram(fields[4].trim());
            Github github = new Github(fields[6].trim());

            // Process tags
            Set<Tag> tags = parseTags(fields[5].trim());

            // Process assignment (name and score)
            String assignmentNameStr = fields[7].trim();
            String assignmentScoreStr = fields[8].trim();

            if (assignmentNameStr.equals("N/A") || assignmentNameStr.isEmpty()) {
                return new Person(name, phone, email, address, telegram, tags, github);
            }

            float assignmentScore = Float.parseFloat(assignmentScoreStr);
            Assignment assignment = new Assignment(assignmentNameStr, assignmentScore);

            return new Person(name, phone, email, address, telegram, tags, github, assignment);

        } catch (IllegalArgumentException e) {
            throw new CommandException(e.getMessage());
        }
    }

    /**
     * Parses a set of tags from a string with tags in the format "[tag1],[tag2]".
     */
    private Set<Tag> parseTags(String tagField) {
        Set<Tag> tags = new HashSet<>();

        // Remove surrounding brackets for each tag
        tagField = tagField.trim();
        if (!tagField.isEmpty()) {
            // Split on commas first, then remove the square brackets from each tag
            String[] tagArray = tagField.split(",");
            for (String tag : tagArray) {
                tag = tag.trim(); // Remove any extra spaces
                if (tag.startsWith("[") && tag.endsWith("]")) {
                    tag = tag.substring(1, tag.length() - 1); // Remove the brackets
                }
                if (!tag.isEmpty()) {
                    tags.add(new Tag(tag));
                }
            }
        }
        return tags;
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof ImportCommand)) {
            return false;
        }
        ImportCommand other = (ImportCommand) obj;
        return this.csvFilePath.equals(other.csvFilePath);
    }
}



