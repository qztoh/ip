/**
 * A task that must be completed before a specified time.
 */
public class Deadline extends Task {
    private final String due;

    /**
     * Creates a new unfinished deadline task.
     *
     * @param title the task description
     * @param due the deadline, kept in the user's display format
     */
    public Deadline(String title, String due) {
        super(title);
        this.due = due;
    }

    /**
     * Formats this task with its deadline.
     *
     * @return the formatted deadline task
     */
    @Override
    public String toString() {
        return String.format("[D]%s (by: %s)", super.toString(), this.due);
    }

    /**
     * Converts this deadline task into the storage format.
     *
     * @return the serialized deadline task
     */
    @Override
    public String saveString() {
        int status = isDone() ? 1 : 0;
        return String.format("D | %d | %s | %s", status, getTitle(), due);
    }
}
