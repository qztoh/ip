import java.time.LocalDateTime;

/**
 * A task scheduled between a specific start time and end time.
 */
public class Event extends Task {
    private final LocalDateTime from;
    private final LocalDateTime to;

    /**
     * Creates a new unfinished event task.
     *
     * @param title the event description
     * @param from the event start time, kept in the user's display format
     * @param to the event end time, kept in the user's display format
     */
    public Event(String title, String from, String to) {
        this(title, DateTimeParser.parseUserInput(from), DateTimeParser.parseUserInput(to));
    }

    public Event(String title, LocalDateTime from, LocalDateTime to) {
        super(title);
        this.from = validateDateTime(from, "Event start time");
        this.to = validateDateTime(to, "Event end time");
        if (this.from.isAfter(this.to)) {
            throw new IllegalArgumentException("Event start must not be after its end");
        }
    }

    public Event(String title, boolean done, String from, String to) {
        this(title, done, DateTimeParser.parseUserInput(from), DateTimeParser.parseUserInput(to));
    }

    public Event(String title, boolean done, LocalDateTime from, LocalDateTime to) {
        super(title, done);
        this.from = validateDateTime(from, "Event start time");
        this.to = validateDateTime(to, "Event end time");
        if (this.from.isAfter(this.to)) {
            throw new IllegalArgumentException("Event start must not be after its end");
        }
    }

    public Event(String title, int done, String from, String to) {
        this(title, done, DateTimeParser.parseStored(from), DateTimeParser.parseStored(to));
    }

    public Event(String title, int done, LocalDateTime from, LocalDateTime to) {
        super(title, done);
        this.from = validateDateTime(from, "Event start time");
        this.to = validateDateTime(to, "Event end time");
        if (this.from.isAfter(this.to)) {
            throw new IllegalArgumentException("Event start must not be after its end");
        }
    }

    private static LocalDateTime validateDateTime(LocalDateTime value, String fieldName) {
        if (value == null) {
            throw new IllegalArgumentException(fieldName + " cannot be empty");
        }
        return value;
    }

    /**
     * Formats this task with its start and end times.
     *
     * @return the formatted event task
     */
    @Override
    public String toString() {
        return String.format("[E]%s (from: %s to: %s)", super.toString(),
                DateTimeParser.formatForDisplay(from), DateTimeParser.formatForDisplay(to));
    }

    /**
     * Converts this event task into the storage format.
     *
     * @return the serialized event task
     */
    @Override
    public String saveString() {
        int status = isDone() ? 1 : 0;
        String schedule = DateTimeParser.formatForStorage(from) + " -> "
                + DateTimeParser.formatForStorage(to);
        return String.format("E | %d | %s | %s", status, getTitle(), schedule);
    }
}
