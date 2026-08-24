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
    private static int tasksCount = 0;
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
                if (tasksCount == 0) {
                    LokiDialogue.echo("You lack any tasks"); 
                } else {
                    listTasks();
                }
                break;
            case "mark":
                updateTaskStatus(words, true);
                break;
            case "unmark":
                updateTaskStatus(words, false);
                break;
            default:
                Task task = createTask(input);
                add(task);
                LokiDialogue.obedient(task.toString());
                LokiDialogue.echo(String.format("You have %s tasks left to conquer.", tasksCount));
            }
        }
        scanner.close();
    }
    private static void add(Task task) {
        tasks[tasksIndex++] = task;
        tasksCount++;
    }

    /**
     * Creates a task from a user's input line.
     *
     * @param input the complete input line
     * @return the task represented by the input
     */
    private static Task createTask(String input) {
        String trimmedInput = input.trim();
        String[] words = trimmedInput.split("\\s+");
        String keyword = words[0].toLowerCase();

        switch (keyword) {
        case "todo":
            return new ToDo(afterKeyword(trimmedInput, keyword));
        case "deadline":
            return createDeadline(trimmedInput, keyword);
        case "event":
            return createEvent(trimmedInput, keyword);
        default:
            return new ToDo(input);
        }
    }

    /**
     * Creates a deadline task using the `/by` marker.
     */
    private static Task createDeadline(String input, String keyword) {
        String body = afterKeyword(input, keyword);
        String lowerBody = body.toLowerCase();
        int byIndex = lowerBody.indexOf("/by ");
        if (byIndex < 0) {
            return new ToDo(input);
        }

        String title = body.substring(0, byIndex).trim();
        String due = body.substring(byIndex + 4).trim();
        return new Deadline(title, due);
    }

    /**
     * Creates an event task using `/from` and `/to` markers.
     */
    private static Task createEvent(String input, String keyword) {
        String body = afterKeyword(input, keyword);
        String lowerBody = body.toLowerCase();
        int fromIndex = lowerBody.indexOf("/from ");
        int toIndex = lowerBody.indexOf("/to ", fromIndex + 6);
        if (fromIndex < 0 || toIndex < 0) {
            return new ToDo(input);
        }

        String title = body.substring(0, fromIndex).trim();
        String from = body.substring(fromIndex + 6, toIndex).trim();
        String to = body.substring(toIndex + 4).trim();
        return new Event(title, from, to);
    }

    /**
     * Removes a task-type keyword from the beginning of an input line.
     */
    private static String afterKeyword(String input, String keyword) {
        return input.substring(keyword.length()).trim();
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
            LokiDialogue.obedient(task.toString());
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
