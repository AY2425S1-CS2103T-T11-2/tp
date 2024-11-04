package seedu.address.logic.parser;

import static seedu.address.logic.commands.ImportCommand.CORRECT_HEADER_USAGE;

import seedu.address.logic.commands.exceptions.CommandException;

/**
 * Parses CSV row data, validating headers and ensuring proper formatting.
 */
public class CsvRowParser {

    /**
     * Checks if all fields in a row are empty.
     *
     * @param fields the row data from the CSV file
     * @return true if all fields are empty or contain only whitespace, false otherwise
     */
    public static boolean isRowEmpty(String[] fields) {
        for (String field : fields) {
            if (field != null && !field.trim().isEmpty()) {
                return false;
            }
        }
        return true;
    }

    /**
     * Checks if the CSV headers match the expected headers.
     *
     * @param headers the headers from the CSV file
     * @param expectedHeaders the expected headers in the correct order
     * @throws CommandException if the headers are incorrect
     */
    public static void checkHeaders(String[] headers, String[] expectedHeaders) throws CommandException {
        if (headers.length == 0) {
            throw new CommandException("CSV header is empty/contains empty values, please ensure"
                + " all headers are valid.\n"
                + CORRECT_HEADER_USAGE);
        }

        for (int i = 0; i < headers.length; i++) {
            headers[i] = headers[i].trim(); // Trim whitespace from headers
        }

        for (String header : headers) {
            if (header.isEmpty() && headers.length <= 8) {
                throw new CommandException("CSV header is empty/contains empty values, please ensure"
                    + " all headers are valid.\n"
                    + CORRECT_HEADER_USAGE);
            }
        }

        if (headers.length > expectedHeaders.length) {
            throw new CommandException("There are extra columns!\n"
                + "Please ensure there is only be 8 corresponding header/data columns\n" + CORRECT_HEADER_USAGE);
        }

        if (headers.length < expectedHeaders.length) {
            throw new CommandException("There are lesser columns in header than expected!\n" + CORRECT_HEADER_USAGE);
        }

        for (int i = 0; i < headers.length; i++) {
            if (!headers[i].trim().equalsIgnoreCase(expectedHeaders[i])) {
                throw new CommandException("Header is defined incorrectly!\n" + CORRECT_HEADER_USAGE);
            }
        }
    }

    /**
     * Cleans the row headers, removing any Byte Order Mark (BOM) if present.
     *
     * @param headers the headers from the CSV file
     * @return the cleaned headers
     */
    public static String[] cleanRow(String[] headers) {
        if (headers.length > 0 && headers[0] != null) {
            headers[0] = headers[0].replace("\uFEFF", ""); // Remove BOM if present
        }
        return headers;
    }
}
