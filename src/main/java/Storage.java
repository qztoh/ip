import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;

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

    /* 
    public ArrayList<Task> load() throws LokiExceptions {
        ArrayList<Task> taskList = new ArrayList<>();
        String allTasks;
        try {
            allTasks = Files.readString(workingDir);
            
        } catch (IOException e) {
            throw new LokiExceptions("Invalid File Path");
        }

        for (String rawTask : allTasks.split("\\+")) {
            
        }

        return taskList;
    }
    */

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
