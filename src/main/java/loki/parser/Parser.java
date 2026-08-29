package loki.parser;

import java.time.LocalDateTime;
import java.util.Locale;

import loki.exception.LokiExceptions;
import loki.model.Deadline;
import loki.model.Event;
import loki.model.Task;
import loki.model.ToDo;

/**
 * Converts raw user commands into command keywords, tasks, and task numbers.
 */
public class Parser {
    /** Creates a parser for user commands. */
    public Parser() {
    }

    /** Creates a parser for user commands. */
    public Parser() {
    }

    /**
     * Extracts and normalizes the command keyword.
     *
     * @param input the raw user command.
     * @return the lower-case command keyword.
     * @throws LokiExceptions if the command is empty.
     */
    public String parseKeyword(String input) throws LokiExceptions {
        if (input == null || input.isBlank()) {
            throw LokiExceptions.emptyInput();
        }
        return input.trim().split("\\s+")[0].toLowerCase(Locale.ROOT);
    }

    /**
     * Parses a task creation command.
     *
     * @param input the raw task command.
     * @return the corresponding task.
     * @throws LokiExceptions if the command is malformed.
     */
    public Task parseTask(String input) throws LokiExceptions {
        String trimmedInput = requireInput(input);
        String keyword = parseKeyword(trimmedInput);

        return switch (keyword) {
        case "todo" -> parseToDo(trimmedInput, keyword);
        case "deadline" -> parseDeadline(trimmedInput, keyword);
        case "event" -> parseEvent(trimmedInput, keyword);
        default -> throw LokiExceptions.unknownCommand();
        };
    }

    /**
     * Parses a one-based task number from a command.
     *
     * @param input the raw command.
     * @return the one-based task number.
     * @throws LokiExceptions if the command does not contain exactly one number.
     */
    public int parseTaskNumber(String input) throws LokiExceptions {
        String[] words = requireInput(input).split("\\s+");
        if (words.length != 2) {
            throw LokiExceptions.invalidTaskNumber();
        }

        try {
            return Integer.parseInt(words[1]);
        } catch (NumberFormatException exception) {
            throw LokiExceptions.invalidTaskNumber();
        }
    }

    /**
     * Parses the title in a to-do command.
     *
     * @param input the raw task command
     * @param keyword the already-parsed command keyword
     * @return the parsed to-do task
     * @throws LokiExceptions if the title is missing
     */
    private Task parseToDo(String input, String keyword) throws LokiExceptions {
        String title = afterKeyword(input, keyword);
        if (title.isEmpty()) {
            throw LokiExceptions.invalidToDo();
        }
        return new ToDo(title);
    }

    /**
     * Parses the title and due date in a deadline command.
     *
     * @param input the raw task command
     * @param keyword the already-parsed command keyword
     * @return the parsed deadline task
     * @throws LokiExceptions if the command or due date is malformed
     */
    private Task parseDeadline(String input, String keyword) throws LokiExceptions {
        String body = afterKeyword(input, keyword);
        String lowerBody = body.toLowerCase(Locale.ROOT);
        int byIndex = lowerBody.indexOf("/by ");
        if (byIndex < 0) {
            throw LokiExceptions.invalidDeadline();
        }

        String title = body.substring(0, byIndex).trim();
        String due = body.substring(byIndex + 4).trim();
        if (title.isEmpty() || due.isEmpty()) {
            throw LokiExceptions.invalidDeadline();
        }

        try {
            return new Deadline(title, DateTimeParser.parseUserInput(due));
        } catch (IllegalArgumentException exception) {
            throw LokiExceptions.invalidDeadline();
        }
    }

    /**
     * Parses the title, start time, and end time in an event command.
     *
     * @param input the raw task command
     * @param keyword the already-parsed command keyword
     * @return the parsed event task
     * @throws LokiExceptions if the command or either time is malformed
     */
    private Task parseEvent(String input, String keyword) throws LokiExceptions {
        String body = afterKeyword(input, keyword);
        String lowerBody = body.toLowerCase(Locale.ROOT);
        int fromIndex = lowerBody.indexOf("/from ");
        int toIndex = lowerBody.indexOf("/to ", fromIndex + 6);
        if (fromIndex < 0 || toIndex < 0) {
            throw LokiExceptions.invalidEvent();
        }

        String title = body.substring(0, fromIndex).trim();
        String from = body.substring(fromIndex + 6, toIndex).trim();
        String to = body.substring(toIndex + 4).trim();
        if (title.isEmpty() || from.isEmpty() || to.isEmpty()) {
            throw LokiExceptions.invalidEvent();
        }

        try {
            LocalDateTime start = DateTimeParser.parseUserInput(from);
            LocalDateTime end = DateTimeParser.parseUserInput(to);
            return new Event(title, start, end);
        } catch (IllegalArgumentException exception) {
            throw LokiExceptions.invalidEvent();
        }
    }

    /**
     * Returns the portion of a command after its keyword.
     *
     * @param input the trimmed command
     * @param keyword the command keyword
     * @return the trimmed command body
     */
    private String afterKeyword(String input, String keyword) {
        return input.substring(keyword.length()).trim();
    }

    /**
     * Validates and trims raw command input.
     *
     * @param input the raw command
     * @return the trimmed command
     * @throws LokiExceptions if the command is null or blank
     */
    private String requireInput(String input) throws LokiExceptions {
        if (input == null || input.isBlank()) {
            throw LokiExceptions.emptyInput();
        }
        return input.trim();
    }
}
