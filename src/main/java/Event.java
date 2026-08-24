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
        this.from = from;
        this.to = to;
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
}
