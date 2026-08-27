import java.io.IOException;
import java.nio.file.InvalidPathException;
import java.nio.file.NoSuchFileException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.List;

/**
 * Handles saving tasks to the application's data file.
 */
public class Storage {
    private static final Path DEFAULT_WK_DIR = Path.of("src/data/tasks.txt");
    private final Path workingDir;

    public Storage(String filePath) {
        if (filePath == null || filePath.isBlank()) {
            throw new IllegalArgumentException("Storage path cannot be empty");
        }
        try {
            this.workingDir = Path.of(filePath);
        } catch (InvalidPathException exception) {
            throw new IllegalArgumentException("Invalid storage path", exception);
        }
    }
    
    public Storage() {
        this.workingDir = DEFAULT_WK_DIR;
    }


    private Task taskify(String rawTask) throws LokiExceptions {
        if (rawTask == null || rawTask.isBlank()) {
            throw new LokiExceptions("Task record cannot be empty");
        }

        String[] fields = rawTask.split("\\s*\\|\\s*", -1);

        if (fields.length < 3) {
            throw new LokiExceptions("Invalid task format");
        }

        String type = fields[0].trim();
        int done = parseStatus(fields[1].trim());
        String title = fields[2].trim();

        if (title.isEmpty()) {
            throw new LokiExceptions("Task title cannot be empty");
        }

        // T | 0 | one
        // D | 0 | two | later
        // E | 0 | three | now-forever
        return switch (type) {
            case "T" -> {
                requireFieldCount(fields, 3);
                yield new ToDo(title, done);
            }
            case "D" -> {
                requireFieldCount(fields, 4);
                String due = fields[3].trim();
                if (due.isEmpty()) {
                    throw new LokiExceptions("Deadline cannot be empty");
                }
                try {
                    yield new Deadline(title, done, DateTimeParser.parseStored(due));
                } catch (IllegalArgumentException exception) {
                    throw new LokiExceptions("Invalid deadline date/time");
                }
            }
            case "E" -> {
                requireFieldCount(fields, 4);
                String schedule = fields[3].trim();
                int separator = schedule.indexOf("->");
                if (separator <= 0 || separator >= schedule.length() - 2) {
                    throw new LokiExceptions("Invalid event time format");
                }
                String from = schedule.substring(0, separator).trim();
                String to = schedule.substring(separator + 2).trim();
                if (from.isEmpty() || to.isEmpty()) {
                    throw new LokiExceptions("Invalid event time format");
                }
                try {
                    LocalDateTime start = DateTimeParser.parseStored(from);
                    LocalDateTime end = DateTimeParser.parseStored(to);
                    yield new Event(title, done, start, end);
                } catch (IllegalArgumentException exception) {
                    throw new LokiExceptions("Invalid event date/time");
                }
            }
            default -> throw new LokiExceptions("Unknown Task type; Is your file corrupted?");
        };

    }

    private int parseStatus(String status) throws LokiExceptions {
        if (status.equals("0") || status.equals("1")) {
            return Integer.parseInt(status);
        }
        throw new LokiExceptions("Invalid task status");
    }

    private void requireFieldCount(String[] fields, int expected)
            throws LokiExceptions {
        if (fields.length != expected) {
            throw new LokiExceptions("Invalid task format");
        }
    }


    public TaskList load() throws LokiExceptions {
        TaskList taskList = new TaskList();
        List<String> allTasks;
        try {
            allTasks = Files.readAllLines(workingDir, StandardCharsets.UTF_8);
        } catch (NoSuchFileException exception) {
            return taskList;
        } catch (IOException | SecurityException exception) {
            throw new LokiExceptions("Invalid File Path");
        }

        for (String rawTask : allTasks) {
            if (!rawTask.isBlank()) {
                taskList.add(taskify(rawTask));
            }
        }
        
        return taskList;
    }

    public void save(TaskList taskList) throws LokiExceptions {
        if (taskList == null) {
            throw new LokiExceptions("Task list cannot be null");
        }

        StringBuilder tasks = new StringBuilder();
        for (Task task : taskList) {
            if (task == null) {
                throw new LokiExceptions("Task list cannot contain null tasks");
            }
            String serializedTask = task.saveString();
            if (serializedTask == null || serializedTask.isBlank()
                    || serializedTask.indexOf('\n') >= 0 || serializedTask.indexOf('\r') >= 0) {
                throw new LokiExceptions("Task has an invalid storage format");
            }
            tasks.append(serializedTask);
            tasks.append("\n");   
        }
        try {
            Path parent = workingDir.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
            Files.writeString(workingDir, tasks.toString(), StandardCharsets.UTF_8);
        } catch (IOException | SecurityException exception) {
            throw new LokiExceptions("Unable to save file");
        }
        
    }
}
