import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Scanner;

/**
 * A minimal command-line entry point for the Loki task manager.
 */
public class Loki {
    private static final ArrayList<Task> tasks = new ArrayList<>();
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
            try {
                String trimmedInput = input.trim();
                if (trimmedInput.isEmpty()) {
                    throw LokiExceptions.emptyInput();
                }

                String[] words = trimmedInput.split("\\s+");
                String keyword = words[0].toLowerCase();
                switch (keyword) {
                case "faretheewell":
                case "exit":
                    activeSession = false;
                    LokiDialogue.exit();
                    break;
                case "list":
                    if (tasks.isEmpty()) {
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
                case "todo":
                case "deadline":
                case "event":
                    Task task = createTask(input);
                    add(task);
                    LokiDialogue.obedient(task.toString());
                    LokiDialogue.echo(String.format("You have %s tasks left to conquer.", tasks.size()));
                    break;
                case "delete":
                    Task deletedTask = deleteTask(words);
                    LokiDialogue.obedient(deletedTask.toString());
                    LokiDialogue.echo(String.format("You have %s tasks left to conquer.", tasks.size()));
                    break;
                default:
                    throw LokiExceptions.unknownCommand();
                }
            } catch (LokiExceptions exception) {
                LokiDialogue.error(exception.getMessage());
            }
        }
        scanner.close();
    }
    private static void add(Task task) {
        tasks.add(task);
    }

    /**
     * Creates a task from a user's input line.
     *
     * @param input the complete input line
     * @return the task represented by the input
     */
    private static Task createTask(String input) throws LokiExceptions {
        String trimmedInput = input.trim();
        String[] words = trimmedInput.split("\\s+");
        String keyword = words[0].toLowerCase();

        switch (keyword) {
        case "todo":
            String title = afterKeyword(trimmedInput, keyword);
            if (title.isEmpty()) {
                throw LokiExceptions.invalidToDo();
            }
            return new ToDo(title);
        case "deadline":
            return createDeadline(trimmedInput, keyword);
        case "event":
            return createEvent(trimmedInput, keyword);
        default:
            throw LokiExceptions.unknownCommand();
        }
    }

    /**
     * Creates a deadline task using the `/by` marker.
     */
    private static Task createDeadline(String input, String keyword) throws LokiExceptions {
        String body = afterKeyword(input, keyword);
        String lowerBody = body.toLowerCase();
        int byIndex = lowerBody.indexOf("/by ");
        if (byIndex < 0) {
            throw LokiExceptions.invalidDeadline();
        }

        String title = body.substring(0, byIndex).trim();
        String due = body.substring(byIndex + 4).trim();
        if (title.isEmpty() || due.isEmpty()) {
            throw LokiExceptions.invalidDeadline();
        }
        return new Deadline(title, due);
    }

    /**
     * Creates an event task using `/from` and `/to` markers.
     */
    private static Task createEvent(String input, String keyword) throws LokiExceptions {
        String body = afterKeyword(input, keyword);
        String lowerBody = body.toLowerCase();
        int fromIndex = lowerBody.indexOf("/from ");
        int toIndex = lowerBody.indexOf("/to ", fromIndex + 6);
        if (fromIndex < 0 || toIndex < 0) {
            throw LokiExceptions.invalidEvent();
        }

        String title = body.substring(0, fromIndex).trim();
        String from = body.substring(fromIndex + 6, toIndex).trim();
        String to = body.substring(toIndex + 4).trim();
        if (title.isEmpty() || from.isEmpty() || to.isEmpty()) {
            throw LokiExceptions.invalidEvent();
        }
        return new Event(title, from, to);
    }

    /**
     * Removes a task-type keyword from the beginning of an input line.
     */
    private static String afterKeyword(String input, String keyword) {
        return input.substring(keyword.length()).trim();
    }

    private static void updateTaskStatus(String[] words, boolean mark) throws LokiExceptions {
        int taskIndex = taskIndexFrom(words);
        Task task = tasks.get(taskIndex);
        if (mark) {
            task.markDone();
        } else {
            task.unmarkDone();
        }
        LokiDialogue.obedient(task.toString());
    }

    /**
     * Removes a task using its one-based list number.
     *
     * @param words the command words, including the task number
     * @throws LokiExceptions if the task number is missing or invalid
     */
    private static Task deleteTask(String[] words) throws LokiExceptions {
        int taskIndex = taskIndexFrom(words);
        return tasks.remove(taskIndex);
    }

    /**
     * Converts a one-based task number from user input to an ArrayList index.
     */
    private static int taskIndexFrom(String[] words) throws LokiExceptions {
        if (words.length < 2) {
            throw LokiExceptions.invalidTaskNumber();
        }

        int taskIndex;
        try {
            taskIndex = Integer.parseInt(words[1]) - 1;
        } catch (NumberFormatException exception) {
            throw LokiExceptions.invalidTaskNumber();
        }

        if (taskIndex < 0 || taskIndex >= tasks.size()) {
            throw LokiExceptions.invalidTaskNumber();
        }
        return taskIndex;
    }

    private static void listTasks() {
        LokiDialogue.banner();
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println(String.format("      %s. %s", i + 1, tasks.get(i)));
        }
        System.out.println("");
    }
}
