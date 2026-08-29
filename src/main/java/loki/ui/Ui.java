package loki.ui;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Random;
import java.util.Scanner;

import loki.exception.LokiExceptions;
import loki.model.Task;
import loki.model.TaskList;

/**
 * Handles interaction with the user, including input and console output.
 */
public class Ui implements AutoCloseable {
    private static final String DIALOGUE_INDENT = "        ";
    private static final String SPLASH = "|        ____   |   / |____|\n"
            + "|       /    \\  |  /     |  \n"
            + "|       |    |  |<       |  \n"
            + "|       \\    /  |  \\ |  \n"
            + "|_______ \\__/   |   \\ |____|\n";
    private static final Random MISCHIEF = new Random();
    private static final String[] FAREWELLS = loadDialogue("farewells.txt");
    private static final String[] INVALID_TASK_RESPONSES = loadDialogue("invalid-task-responses.txt");
    private static final String[] VALID_TASK_RESPONSES = loadDialogue("valid-task-responses.txt");
    private static final String[] LOKIEXCEPTION = loadDialogue("loki-exceptions.txt");

    private final Scanner scanner;

    /** Creates a UI that reads commands from standard input. */
    public Ui() {
        scanner = new java.util.Scanner(System.in);
    }

    /**
     * Returns whether another command is available.
     *
     * @return true if another input line is available
     */
    public boolean hasNextLine() {
        return scanner.hasNextLine();
    }

    /**
     * Reads the next command line.
     *
     * @return the next input line
     */
    public String readLine() {
        return scanner.nextLine();
    }

    /** Prints Loki's splash screen, logo, and greeting. */
    public void showWelcome() throws IOException {
        String logo = Files.readString(resolveAsset("loki.txt"));
        banner();
        System.out.println(SPLASH);
        System.out.println(logo);
        greeting();
    }

    /** Prints a heavy divider. */
    public void bannerHeavy() {
        System.out.println("======================================================");
    }

    /** Prints a light divider. */
    public void banner() {
        System.out.println("------------------------------------------------------");
    }

    /** Prints Loki's greeting. */
    public void greeting() {
        printHeavyDialogue(
            "Greetings, mortal",
            "Loki the Trickster God at your service"
        );
    }

    /** Prints a randomly selected farewell. */
    public void exit() {
        printHeavyDialogue(pickRandom(FAREWELLS));
    }

    /** Prints a response to a user's input. */
    public void echo(String text) {
        printDialogue(text);
    }

    /** Prints a randomly selected response for an invalid task index. */
    public void youarestupid() {
        printDialogue(pickRandom(INVALID_TASK_RESPONSES));
    }

    /** Prints a temporary response for an unrecognised command. */
    public void notUnderstanding() {
        printDialogue(pickRandom(LOKIEXCEPTION));
    }

    /** Prints an input or storage error. */
    public void error(String message) {
        printDialogue(message);
    }

    /** Prints a randomly selected response to a successful task operation. */
    public void obedient(String task) {
        printDialogue(pickRandom(VALID_TASK_RESPONSES) + "\n          " + task);
    }

    /** Prints all tasks in their one-based list order. */
    public void listTasks(TaskList tasks) throws LokiExceptions {
        banner();
        for (int i = 1; i <= tasks.size(); i++) {
            System.out.println(String.format("      %s. %s", i, tasks.get(i)));
        }
        System.out.println("");
    }

    /**
     * Prints matching tasks using their original one-based list positions.
     *
     * @param tasks the complete task list
     * @param matchingTasks the tasks returned by a search
     * @throws LokiExceptions if a matching task cannot be retrieved by its index
     */
    public void showSearchResults(TaskList tasks, List<Task> matchingTasks)
            throws LokiExceptions {
        if (matchingTasks.isEmpty()) {
            echo("No matching tasks found.");
            return;
        }

        banner();
        for (int i = 1; i <= tasks.size(); i++) {
            if (matchingTasks.contains(tasks.get(i))) {
                System.out.println(String.format("      %s. %s", i, tasks.get(i)));
            }
        }
        System.out.println("");
    }

    /** Prints the response after a task is added. */
    public void taskAdded(Task task, int taskCount) {
        obedient(task.toString());
        taskCount(taskCount);
    }

    /** Prints the response after a task is deleted. */
    public void taskDeleted(Task task, int taskCount) {
        obedient(task.toString());
        taskCount(taskCount);
    }

    /** Prints the number of tasks remaining. */
    public void taskCount(int taskCount) {
        echo(String.format("You have %s tasks left to conquer.", taskCount));
    }

    /** Returns a randomly selected Loki exception message. */
    public static String randomExceptionMessage() {
        return "Loki error: " + pickRandom(LOKIEXCEPTION);
    }

    @Override
    public void close() {
        scanner.close();
    }

    private void printDialogue(String... lines) {
        banner();
        for (String line : lines) {
            System.out.println(DIALOGUE_INDENT + line);
        }
        banner();
    }

    private void printHeavyDialogue(String... lines) {
        bannerHeavy();
        for (String line : lines) {
            System.out.println(DIALOGUE_INDENT + line);
        }
        bannerHeavy();
    }

    private static String pickRandom(String[] choices) {
        return choices[MISCHIEF.nextInt(choices.length)];
    }

    private static String[] loadDialogue(String filename) {
        try {
            String content = Files.readString(resolveAsset(filename));
            content = content.replaceFirst("\\r?\\n$", "");
            String[] entries = content.split("\\r?\\n\\+\\r?\\n");
            if (entries.length == 0) {
                throw new IOException("file contains no dialogue entries");
            }
            return entries;
        } catch (IOException exception) {
            throw new ExceptionInInitializerError(
                    "Unable to read dialogue file " + filename + ": " + exception.getMessage());
        }
    }

    private static Path resolveAsset(String filename) {
        Path workingDirectoryAsset = Path.of(filename);
        if (Files.exists(workingDirectoryAsset)) {
            return workingDirectoryAsset;
        }
        return Path.of("src", "assets", filename);
    }
}
