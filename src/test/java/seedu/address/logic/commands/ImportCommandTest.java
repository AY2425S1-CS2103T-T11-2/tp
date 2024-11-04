package seedu.address.logic.commands;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static seedu.address.logic.commands.CommandTestUtil.assertCommandFailure;
import static seedu.address.logic.commands.ImportCommand.CORRECT_HEADER_USAGE;
import static seedu.address.testutil.TypicalPersons.getTypicalAddressBook;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.exceptions.CommandException;
import seedu.address.model.Model;
import seedu.address.model.ModelManager;
import seedu.address.model.UserPrefs;
import seedu.address.model.util.SampleAssignmentsUtil;

/**
 * Contains tests for {@code ImportCommand}.
 */
public class ImportCommandTest {
    private final Model model = new ModelManager(
        getTypicalAddressBook(),
        new UserPrefs(),
        SampleAssignmentsUtil.getSamplePredefinedAssignments());

    /**
     * Tests the execution of a valid import command, expecting success.
     * Verifies that the command result feedback matches the expected success message.
     *
     * @throws CommandException if command execution fails unexpectedly.
     */
    @Test
    public void validImportCommandResult_success() throws CommandException {
        String projectDir = System.getProperty("user.dir");
        String filePath = projectDir + "/src/test/data/valid.csv";
        ImportCommand command = new ImportCommand(filePath);
        CommandResult commandResult = command.execute(model);
        assertEquals("Successfully imported 3 persons.", commandResult.getFeedbackToUser());
    }

    /**
     * Tests the execution of an import command with an invalid CSV header, expecting failure.
     * Verifies that the command fails with the expected error message indicating incorrect header format.
     */
    @Test
    public void invalidCsvHeaderExecution_fail() {
        String projectDir = System.getProperty("user.dir");
        String filePath = projectDir + "/src/test/data/invalidHeader.csv";
        ImportCommand command = new ImportCommand(filePath);
        String expectedMsg = "Error reading from the CSV file: " + "Header is defined incorrectly!\n"
            + CORRECT_HEADER_USAGE;
        assertCommandFailure(command, model, expectedMsg);
    }

    /**
     * Tests the execution of an import command with extra columns in the CSV header, expecting failure.
     * Verifies that the command fails with the expected error message indicating extra columns in the header.
     */
    @Test
    public void extraCsvHeaders_fail() {
        String projectDir = System.getProperty("user.dir");
        String filePath = projectDir + "/src/test/data/extraHeader.csv";
        ImportCommand command = new ImportCommand(filePath);
        String expectedMsg = "Error reading from the CSV file: " + "There are extra columns!\n"
            + "Please ensure there is only be 8 corresponding header/data columns\n" + CORRECT_HEADER_USAGE;
        assertCommandFailure(command, model, expectedMsg);
    }

    /**
     * Tests the execution of an import command with fewer columns in the CSV header than expected, expecting failure.
     * Verifies that the command fails with the expected error message indicating missing columns in the header.
     */
    @Test
    public void missingCsvHeadersEntry_fail() {
        String projectDir = System.getProperty("user.dir");
        String filePath = projectDir + "/src/test/data/missingHeaderEntry.csv";
        ImportCommand command = new ImportCommand(filePath);
        String expectedMsg = "Error reading from the CSV file: "
            + "There are lesser columns in header than expected!\n" + CORRECT_HEADER_USAGE;
        assertCommandFailure(command, model, expectedMsg);
    }

    /**
     * Tests the execution of an import command with an empty CSV header, expecting failure.
     * Verifies that the command fails with the expected error message indicating that all headers must be valid.
     */
    @Test
    public void missingCsvHeader_fail() {
        String projectDir = System.getProperty("user.dir");
        String filePath = projectDir + "/src/test/data/missingHeader.csv";
        ImportCommand command = new ImportCommand(filePath);
        String expectedMsg = "Error reading from the CSV file: "
            + "CSV header is empty/contains empty values, please ensure"
            + " all headers are valid.\n"
            + CORRECT_HEADER_USAGE;
        assertCommandFailure(command, model, expectedMsg);
    }

    /**
     * Tests the execution of an import command with missing data rows, expecting partial success.
     * Verifies that the command result feedback indicates the correct number of persons imported.
     *
     * @throws CommandException if command execution fails unexpectedly.
     */
    @Test
    public void missingDataRow_success() throws CommandException {
        String projectDir = System.getProperty("user.dir");
        String filePath = projectDir + "/src/test/data/missingRow.csv";
        ImportCommand command = new ImportCommand(filePath);
        CommandResult commandResult = command.execute(model);
        assertEquals("Successfully imported 2 persons.", commandResult.getFeedbackToUser());
    }

    /**
     * Tests the execution of an import command with data rows containing only commas, expecting partial success.
     * Verifies that the command result feedback indicates the correct number of persons imported.
     *
     * @throws CommandException if command execution fails unexpectedly.
     */
    @Test
    public void dataRowAllComa_success() throws CommandException {
        String projectDir = System.getProperty("user.dir");
        String filePath = projectDir + "/src/test/data/dataRowAllComma.csv";
        ImportCommand command = new ImportCommand(filePath);
        CommandResult commandResult = command.execute(model);
        assertEquals("Successfully imported 2 persons.", commandResult.getFeedbackToUser());
    }

    /**
     * Tests the execution of an import command with no data rows, expecting failure.
     * Verifies that the command fails with the expected error message indicating no person data is present.
     *
     * @throws CommandException if command execution fails unexpectedly.
     */
    @Test
    public void noDataRow_fail() throws CommandException {
        String projectDir = System.getProperty("user.dir");
        String filePath = projectDir + "/src/test/data/noDataRow.csv";
        ImportCommand command = new ImportCommand(filePath);
        String expectedMsg = "Error reading from the CSV file: "
            + "There is no person data present";
        assertCommandFailure(command, model, expectedMsg);
    }
}
