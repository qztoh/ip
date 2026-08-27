import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.List;
import java.util.Random;

/**
 * Handles all console output for Loki.
 *
 * <p>Keeping presentation logic here allows the command-processing code in
 * {@link Loki} to focus on interpreting commands and updating tasks.</p>
 */
public class LokiUi {
    private static final String DIALOGUE_INDENT = "        ";
    private static final String SPLASH = "|        ____   |   / |____|\n"
            + "|       /    \\  |  /     |  \n"
            + "|       |    |  |<       |  \n"
            + "|       \\    /  |  \\     |  \n"
            + "|_______ \\__/   |   \\ |____|\n";
    private static final Random MISCHIEF = new Random();
    private static final String[] FAREWELLS = loadDialogue("farewells.txt");
    private static final String[] INVALID_TASK_RESPONSES = loadDialogue("invalid-task-responses.txt");
    private static final String[] VALID_TASK_RESPONSES = loadDialogue("valid-task-responses.txt");
    private static final String[] LOKIEXCEPTION = loadDialogue("loki-exceptions.txt");

    private LokiUi() {
        // Utility class; do not instantiate.
    }

    /** Prints Loki's splash screen, logo, and greeting. */
    public static void showWelcome() throws IOException {
        String logo = Files.readString(resolveAsset("loki.txt"));
        banner();
        System.out.println(SPLASH);
        System.out.println(logo);
        greeting();
    }

    /** Prints a heavy divider. */
    public static void bannerHeavy() {
        System.out.println("======================================================");
    }

    /** Prints a light divider. */
    public static void banner() {
        System.out.println("------------------------------------------------------");
    }

    /** Prints Loki's greeting. */
    public static void greeting() {
        printHeavyDialogue(
            "Greetings, mortal",
            "Loki the Trickster God at your service"
        );
    }

    /** Prints a randomly selected farewell. */
    public static void exit() {
        printHeavyDialogue(pickRandom(FAREWELLS));
    }

    /** Prints a response to a user's input. */
    public static void echo(String text) {
        printDialogue(text);
    }

    /** Prints a randomly selected response for an invalid task index. */
    public static void youarestupid() {
        printDialogue(pickRandom(INVALID_TASK_RESPONSES));
    }

    /** Prints a temporary response for an unrecognised command. */
    public static void notUnderstanding() {
        printDialogue(pickRandom(LOKIEXCEPTION));
    }

    /** Prints an input error. */
    public static void error(String message) {
        printDialogue(message);
    }

    /** Prints a randomly selected response for a successful task update. */
    public static void obedient(String task) {
        printDialogue(pickRandom(VALID_TASK_RESPONSES) + "\n          " + task);
    }

    /** Prints all tasks in their one-based list order. */
    public static void listTasks(List<Task> tasks) {
        banner();
        for (int i = 0; i < tasks.size(); i++) {
            System.out.println(String.format("      %s. %s", i + 1, tasks.get(i)));
        }
        System.out.println("");
    }

    /** Prints the response after a task is added. */
    public static void taskAdded(Task task, int taskCount) {
        obedient(task.toString());
        taskCount(taskCount);
    }

    /** Prints the response after a task is deleted. */
    public static void taskDeleted(Task task, int taskCount) {
        obedient(task.toString());
        taskCount(taskCount);
    }

    /** Prints the number of tasks remaining. */
    public static void taskCount(int taskCount) {
        echo(String.format("You have %s tasks left to conquer.", taskCount));
    }

    /** Returns a randomly selected Loki exception message. */
    public static String randomExceptionMessage() {
        return "Loki error: " + pickRandom(LOKIEXCEPTION);
    }

    /** Prints one or more dialogue lines using the normal banner format. */
    private static void printDialogue(String... lines) {
        banner();
        for (String line : lines) {
            System.out.println(DIALOGUE_INDENT + line);
        }
        banner();
    }

    /** Prints one or more dialogue lines using the heavy banner format. */
    private static void printHeavyDialogue(String... lines) {
        bannerHeavy();
        for (String line : lines) {
            System.out.println(DIALOGUE_INDENT + line);
        }
        bannerHeavy();
    }

    /** Selects one item from a collection of dialogue choices. */
    private static String pickRandom(String[] choices) {
        return choices[MISCHIEF.nextInt(choices.length)];
    }

    /** Loads dialogue entries separated by a standalone plus sign. */
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

    /** Resolves an asset from the working directory or the source asset folder. */
    private static Path resolveAsset(String filename) {
        Path workingDirectoryAsset = Path.of(filename);
        if (Files.exists(workingDirectoryAsset)) {
            return workingDirectoryAsset;
        }
        return Path.of("src", "assets", filename);
    }
}
