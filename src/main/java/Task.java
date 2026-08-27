/**
 * Represents one task entered by the user.
 */
public abstract class Task {
    private final String title;
    private boolean done;

    /**
     * Creates a new unfinished task.
     *
     * @param title the task description
     */
    public Task(String title) {
        this.title = title;
        this.done = false;
    }

    /**
     * Returns whether this task has been completed.
     *
     * @return true if the task is done
     */
    public boolean isDone() {
        return done;
    }

    /**
     * Marks this task as completed.
     */
    public void markDone() {
        done = true;
    }

    /**
     * Marks this task as incomplete.
     */
    public void unmarkDone() {
        done = false;
    }

    /**
     * Returns the task title for use by subclasses when serializing the task.
     *
     * @return the task title
     */
    protected String getTitle() {
        return title;
    }

    /**
     * Formats the task with a completion indicator.
     *
     * @return the task's display text
     */
    @Override
    public String toString() {
        String status = done ? "X" : " ";
        return "[" + status + "] " + title;
    }

    /**
     * Converts this task into its storage format.
     *
     * @return the serialized task
     */
    public abstract String saveString();
}
