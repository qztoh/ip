import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

/**
 * A minimal command-line entry point for the Loki task manager.
 */
public class Loki {
    private static final Task[] tasks = new Task[100];
    private static int tasksIndex = 0;
    public static void main(String[] args) throws IOException {
        String splash = "|        ____   |   / |____|\n"
                + "|       /    \\  |  /     |  \n"
                + "|       |    |  |<       |  \n"
                + "|       \\    /  |  \\     |  \n"
                + "|_______ \\__/   |   \\ |____|\n";

        Path pathLoki = Path.of("loki.txt");
        String logo = Files.readString(pathLoki);

        LokiDialogue.banner();
        System.out.println(splash);
        System.out.println(logo);
        LokiDialogue.greeting();

        Scanner scanner = new Scanner(System.in);
        boolean activeSession = true;

        while (activeSession) {
            String input = scanner.nextLine();
            String[] words = input.trim().split("\\s+");
            String keyword = words[0].toLowerCase();
            switch (keyword) {
            case "faretheewell":
            case "exit":
                activeSession = false;
                LokiDialogue.exit();
                break;
            case "list":
                listTasks();
                break;
            case "mark":
                updateTaskStatus(words, true);
                break;
            case "unmark":
                updateTaskStatus(words, false);
                break;
            default:
                add(new Task(input));
                LokiDialogue.echo("added: " + input);
            }
        }
    }
    private static void add(Task task) {
        tasks[tasksIndex++] = task;
    }
    private static void updateTaskStatus(String[] words, boolean mark) {
        try {
            int taskIndex = Integer.parseInt(words[1]) - 1;
            if (taskIndex < 0 || taskIndex >= tasksIndex) {
                throw new IndexOutOfBoundsException();
            }

            Task task = tasks[taskIndex];
            if (mark) {
                task.markDone();
            } else {
                task.unmarkDone();
            }
            // System.out.println("compliant");
            LokiDialogue.obedient(task.toString());
            // System.out.println(task);
        } catch (IndexOutOfBoundsException | NumberFormatException exception) {
            LokiDialogue.youarestupid();
        }
    }
    private static void listTasks() {
        LokiDialogue.banner();
        for (int i = 0; i < tasksIndex; i++) {
            System.out.println(String.format("      %s. %s", i + 1, tasks[i]));
        }
        System.out.println("");
    }
}
