package loki.model;

import java.time.LocalDateTime;

import loki.parser.DateTimeParser;

/**
 * A task that must be completed before a specified time.
 */
public class Deadline extends Task {
    private final LocalDateTime due;

    /**
     * Creates a new unfinished deadline task.
     *
     * @param title the task description
     * @param due the deadline, kept in the user's display format
     */
    public Deadline(String title, String due) {
        this(title, DateTimeParser.parseUserInput(due));
    }

    public Deadline(String title, LocalDateTime due) {
        super(title);
        this.due = validateDateTime(due, "Deadline");
    }

    public Deadline(String title, boolean done, String due) {
        this(title, done, DateTimeParser.parseUserInput(due));
    }

    public Deadline(String title, boolean done, LocalDateTime due) {
        super(title, done);
        this.due = validateDateTime(due, "Deadline");
    }

    public Deadline(String title, int done, String due) {
        this(title, done, DateTimeParser.parseStored(due));
    }

    public Deadline(String title, int done, LocalDateTime due) {
        super(title, done);
        this.due = validateDateTime(due, "Deadline");
    }

    private static LocalDateTime validateDateTime(LocalDateTime value, String fieldName) {
        if (value == null) {
            throw new IllegalArgumentException(fieldName + " cannot be empty");
        }
        return value;
    }

    /**
     * Formats this task with its deadline.
     *
     * @return the formatted deadline task
     */
    @Override
    public String toString() {
        return String.format("[D]%s (by: %s)", super.toString(),
                DateTimeParser.formatForDisplay(due));
    }

    /**
     * Converts this deadline task into the storage format.
     *
     * @return the serialized deadline task
     */
    @Override
    public String saveString() {
        int status = isDone() ? 1 : 0;
        return String.format("D | %d | %s | %s", status, getTitle(),
                DateTimeParser.formatForStorage(due));
    }
}
