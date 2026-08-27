/**
 * A task that does not have a deadline or scheduled time.
 */
public class ToDo extends Task {
    /**
     * Creates a new unfinished to-do task.
     *
     * @param title the task description
     */
    public ToDo(String title) {
        super(title);
    }

    public ToDo(String title, boolean done) {
        super(title, done);
    }
    
    public ToDo(String title, int done) {
        super(title, done);
    }

    /**
     * Formats this task with the to-do task type marker.
     *
     * @return the formatted to-do task
     */
    @Override
    public String toString() {
        return "[T]" + super.toString();
    }

    /**
     * Converts this to-do task into the storage format.
     *
     * @return the serialized to-do task
     */
    @Override
    public String saveString() {
        int status = isDone() ? 1 : 0;
        return String.format("T | %d | %s", status, getTitle());
    }
}
