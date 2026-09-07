package loki.logic;

import loki.exception.LokiExceptions;
import loki.model.Task;
import loki.model.TaskList;
import loki.parser.Parser;
import loki.storage.Storage;

/** Provides command processing for non-console user interfaces. */
public class Logic {
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
        try {
            String keyword = parser.parseKeyword(input);
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
            return exception.getMessage();
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
}
