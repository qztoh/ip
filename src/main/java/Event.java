/**
 * A task scheduled between a specific start time and end time.
 */
public class Event extends Task {
    private final String from;
    private final String to;

    /**
     * Creates a new unfinished event task.
     *
     * @param title the event description
     * @param from the event start time, kept in the user's display format
     * @param to the event end time, kept in the user's display format
     */
    public Event(String title, String from, String to) {
        super(title);
        this.from = validateStorageField(from, "Event start time");
        this.to = validateStorageField(to, "Event end time");
    }
    
    public Event(String title, boolean done, String from, String to) {
        super(title, done);
        this.from = validateStorageField(from, "Event start time");
        this.to = validateStorageField(to, "Event end time");
    }

    public Event(String title, int done, String from, String to) {
        super(title, done);
        this.from = validateStorageField(from, "Event start time");
        this.to = validateStorageField(to, "Event end time");
    }

    /**
     * Formats this task with its start and end times.
     *
     * @return the formatted event task
     */
    @Override
    public String toString() {
        return String.format("[E]%s (from: %s to: %s)", super.toString(), from, to);
    }

    /**
     * Converts this event task into the storage format.
     *
     * @return the serialized event task
     */
    @Override
    public String saveString() {
        int status = isDone() ? 1 : 0;
        return String.format("E | %d | %s | %s-%s", status, getTitle(), from, to);
    }
}
