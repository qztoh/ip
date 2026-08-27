import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;

/**
 * Owns the application's tasks and operations on their one-based indices.
 */
public class TaskList implements Iterable<Task> {
    private final ArrayList<Task> tasks = new ArrayList<>();

    /** Adds a non-null task to the end of the list. */
    public void add(Task task) {
        if (task == null) {
            throw new IllegalArgumentException("Task cannot be null");
        }
        tasks.add(task);
    }

    /** Adds every task from another collection. */
    public void addAll(Iterable<Task> otherTasks) {
        if (otherTasks == null) {
            throw new IllegalArgumentException("Tasks cannot be null");
        }
        for (Task task : otherTasks) {
            add(task);
        }
    }

    /**
     * Returns a task using a one-based index.
     *
     * @throws LokiExceptions if the index is outside the list
     */
    public Task get(int oneBasedIndex) throws LokiExceptions {
        validateIndex(oneBasedIndex);
        return tasks.get(oneBasedIndex - 1);
    }

    /** Returns a task using a validated one-based index for UI display. */
    Task getUnchecked(int oneBasedIndex) {
        return tasks.get(oneBasedIndex - 1);
    }

    /** Removes and returns a task using a one-based index. */
    public Task delete(int oneBasedIndex) throws LokiExceptions {
        validateIndex(oneBasedIndex);
        return tasks.remove(oneBasedIndex - 1);
    }

    /** Marks a task complete using a one-based index. */
    public Task mark(int oneBasedIndex) throws LokiExceptions {
        Task task = get(oneBasedIndex);
        task.markDone();
        return task;
    }

    /** Marks a task incomplete using a one-based index. */
    public Task unmark(int oneBasedIndex) throws LokiExceptions {
        Task task = get(oneBasedIndex);
        task.unmarkDone();
        return task;
    }

    public int size() {
        return tasks.size();
    }

    public boolean isEmpty() {
        return tasks.isEmpty();
    }

    /** Returns a read-only view for code that only needs to inspect tasks. */
    public List<Task> asList() {
        return Collections.unmodifiableList(tasks);
    }

    @Override
    public Iterator<Task> iterator() {
        return asList().iterator();
    }

    private void validateIndex(int oneBasedIndex) throws LokiExceptions {
        if (oneBasedIndex < 1 || oneBasedIndex > tasks.size()) {
            throw LokiExceptions.invalidTaskNumber();
        }
    }
}
