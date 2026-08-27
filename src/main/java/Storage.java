import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;

/**
 * Handles saving tasks to the application's data file.
 */
public class Storage {
    private static final Path DEFAULT_WK_DIR = Path.of("src/data/tasks.txt");
    private final Path workingDir;

    public Storage(String filePath) {
        this.workingDir = Path.of(filePath);
    }
    
    public Storage() {
        this.workingDir = DEFAULT_WK_DIR;
    }


    private Task taskify(String rawTask) throws LokiExceptions {
        String[] fields = rawTask.split("\\s*\\|\\s*", -1);

        if (fields.length < 3) {
            throw new LokiExceptions("Invalid task format");
        }

        String type = fields[0];
        int done = parseStatus(fields[1]);
        String title = fields[2];

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
            yield new Deadline(title, done, fields[3]);
        }
        case "E" -> {
            requireFieldCount(fields, 4);
            String[] times = fields[3].split("-", 2);
            if (times.length != 2 || times[0].isEmpty() || times[1].isEmpty()) {
                throw new LokiExceptions("Invalid event time format");
            }
            yield new Event(title, done, times[0], times[1]);
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


    public ArrayList<Task> load() throws LokiExceptions {
        ArrayList<Task> taskList = new ArrayList<>();
        List<String> allTasks;
        try {
            allTasks = Files.readAllLines(workingDir, StandardCharsets.UTF_8);
            
        } catch (IOException e) {
            throw new LokiExceptions("Invalid File Path");
        }

        for (String rawTask : allTasks) {
            if (!rawTask.isBlank()) {
                taskList.add(taskify(rawTask));
            }
        }
        
        return taskList;
    }

    public void save(ArrayList<Task> taskList) throws LokiExceptions {
        StringBuilder tasks = new StringBuilder();
        for (Task task : taskList) {
            tasks.append(task.saveString());
            tasks.append("\n");   
        }
        try {
            Path parent = workingDir.getParent();
            if (parent != null) {
                Files.createDirectories(parent);
            }
            Files.write(workingDir, tasks.toString().getBytes(StandardCharsets.UTF_8));
        } catch (IOException e) {
            throw new LokiExceptions("Unable to save file");
        }
        
    }
}
