package loki.logic;

import loki.exception.LokiExceptions;
import loki.model.Task;
import loki.model.TaskList;
import loki.parser.Parser;
import loki.storage.Storage;

/** Provides command processing for non-console user interfaces. */
public class Logic {
    private static final String TODO_USAGE = "Usage: todo <task description>\n"
            + "Example: todo Buy groceries";
    private static final String DEADLINE_USAGE = "Usage: deadline <task description> /by <date/time>\n"
            + "Example: deadline Return book /by 2019-06-06\n"
            + "Use yyyy-MM-dd, yyyy-MM-dd HHmm, or d/M/yyyy HHmm for date/time values.";
    private static final String EVENT_USAGE = "Usage: event <title> /from <date/time> /to <date/time>\n"
            + "Example: event Project meeting /from 2019-08-06 1400 /to 2019-08-06 1600\n"
            + "Use yyyy-MM-dd, yyyy-MM-dd HHmm, or d/M/yyyy HHmm for date/time values.";

    private final Storage storage;
    private final Parser parser;
    private final TaskList tasks;

    /** Creates logic backed by Loki's default task file. */
    public Logic() {
        this(new Storage());
    }

    /**
     * Creates logic backed by a specified task file.
     *
     * @param filePath the path of the task file.
     */
    public Logic(String filePath) {
        this(new Storage(filePath));
    }

    /**
     * Loads persisted tasks and initializes the command-processing collaborators.
     *
     * @param storage the storage implementation to use.
     */
    private Logic(Storage storage) {
        this.storage = storage;
        parser = new Parser();
        tasks = new TaskList();
        try {
            tasks.addAll(storage.load());
        } catch (LokiExceptions exception) {
            throw new IllegalStateException("Unable to load Loki tasks", exception);
        }
    }

    /**
     * Executes one command and returns the response for display in a GUI.
     *
     * @param input the command entered by the user.
     * @return the response to display.
     */
    public String processCommand(String input) {
        String keyword = null;
        try {
            keyword = parser.parseKeyword(input);
            return switch (keyword) {
                case "faretheewell", "exit" -> {
                    save();
                    yield "Farewell, mortal.";
                }
                case "list" -> formatTaskList();
                case "mark" -> tasks.mark(parser.parseTaskNumber(input)).toString();
                case "unmark" -> tasks.unmark(parser.parseTaskNumber(input)).toString();
                case "todo", "deadline", "event" -> addTask(input);
                case "delete" -> deleteTask(input);
                default -> throw LokiExceptions.unknownCommand();
            };
        } catch (LokiExceptions | IllegalArgumentException exception) {
            return formatErrorResponse(keyword, exception.getMessage());
        }
    }

    /** Saves the current task list to storage. */
    public void save() throws LokiExceptions {
        storage.save(tasks);
    }

    /**
     * Adds a parsed task and returns the resulting response.
     *
     * @param input the task creation command.
     * @return the added task and remaining task count.
     * @throws LokiExceptions if the task command is malformed.
     */
    private String addTask(String input) throws LokiExceptions {
        Task task = parser.parseTask(input);
        tasks.add(task);
        return task + "\nYou have " + tasks.size() + " tasks left to conquer.";
    }

    /**
     * Deletes a task and returns the resulting response.
     *
     * @param input the deletion command.
     * @return the deleted task and remaining task count.
     * @throws LokiExceptions if the task number is invalid.
     */
    private String deleteTask(String input) throws LokiExceptions {
        Task deletedTask = tasks.delete(parser.parseTaskNumber(input));
        return "Deleted: " + deletedTask + "\nYou have " + tasks.size() + " tasks left to conquer.";
    }

    /**
     * Formats all tasks in their one-based list order.
     *
     * @return the formatted task list, or an empty-list response.
     */
    private String formatTaskList() {
        if (tasks.isEmpty()) {
            return "You lack any tasks";
        }

        StringBuilder response = new StringBuilder();
        int index = 1;
        for (Task task : tasks) {
            response.append(index).append(". ").append(task).append(System.lineSeparator());
            index++;
        }
        return response.toString().trim();
    }

    /**
     * Adds a command-specific usage tip to malformed event responses.
     *
     * @param keyword the parsed command keyword, if available.
     * @param errorMessage the base error message.
     * @return the error message with a usage tip when appropriate.
     */
    private String formatErrorResponse(String keyword, String errorMessage) {
        String usageTip = null;
        if (keyword != null) {
            usageTip = switch (keyword) {
                case "todo" -> TODO_USAGE;
                case "deadline" -> DEADLINE_USAGE;
                case "event" -> EVENT_USAGE;
                default -> null;
            };
        }
        return usageTip == null
                ? errorMessage
                : errorMessage + System.lineSeparator() + usageTip;
    }
}
