import java.util.Random;

/**
 * Contains Loki's presentation and flavour text.
 */
public class LokiDialogue {
    private static final String DIALOGUE_INDENT = "        ";
    private static final Random MISCHIEF = new Random();
    private static final String[] FAREWELLS = {
        "We will meet again, mortal",
        "For you, for all of us",
        "Toodles~",
        "You'll be back",
        "Oh to be burdened with glorious purpose",
        "Farewell. And do try to make your next appearance somewhat more interesting."
    };
    private static final String[] INVALID_TASK_RESPONSES = {
        "That task does not exist. \n        Unless, of course, you've discovered a way to rearrange reality without telling me.",
        "An invalid index. \n        Remarkable. Even I cannot mark a task that isn't there.",
        "I searched the list. Nothing. \n        You appear to have assigned meaning to an empty space.",
        "That task is beyond the bounds of this list. \n        A rather unfortunate little oversight.",
        "You've asked me to alter something that does not exist. \n        I'm good, but I'm not that good.",
        "Incorrect index. \n        Shall we try again before I blame this entirely on the mortal operating the controls?"
    };
    private static final String[] VALID_TASK_RESPONSES = {
        "Consider it done.",
        "The task is complete.",
        "As Odin would have it",
        "Done. Surely you didn’t expect anything less.",
        "Completed, precisely as ordered",
        "Your task has been accomplished. Another problem resolved by yours truly.",
        "It is done.",
        "Finished. As expected of a God such as I."
    };

    /**
     * Prints a heavy divider.
     */
    public static void bannerHeavy() {
        System.out.println("======================================================");
    }

    /**
     * Prints a light divider.
     */
    public static void banner() {
        System.out.println("------------------------------------------------------");
    }

    /**
     * Prints Loki's greeting.
     */
    public static void greeting() {
        printHeavyDialogue(
            "Greetings, mortal",
            "Loki the Trickster God at your service"
        );
    }

    /**
     * Prints a randomly selected farewell.
     */
    public static void exit() {
        printHeavyDialogue(pickRandom(FAREWELLS));
    }

    /**
     * Prints a response to a user's input.
     *
     * @param text the response to print
     */
    public static void echo(String text) {
        printDialogue(text);
    }

    /**
     * Prints a randomly selected response for an invalid task index.
     */
    public static void youarestupid() {
        printDialogue(pickRandom(INVALID_TASK_RESPONSES));
    }

    /**
     * Prints one or more dialogue lines using the normal banner format.
     *
     * @param lines the dialogue lines to print
     */
    private static void printDialogue(String... lines) {
        banner();
        for (String line : lines) {
            System.out.println(DIALOGUE_INDENT + line);
        }
        banner();
    }

    /**
     * Prints one or more dialogue lines using the heavy banner format.
     *
     * @param lines the dialogue lines to print
     */
    private static void printHeavyDialogue(String... lines) {
        bannerHeavy();
        for (String line : lines) {
            System.out.println(DIALOGUE_INDENT + line);
        }
        bannerHeavy();
    }
    /**
     * Prints a randomly selected response for a valid task update.
     */
    public static void obedient(String task) {
        printDialogue(pickRandom(VALID_TASK_RESPONSES) + "\n          " + task);
    }

    /**
     * Selects one item from a collection of dialogue choices.
     *
     * @param choices the possible dialogue choices
     * @return one randomly selected choice
     */
    private static String pickRandom(String[] choices) {
        return choices[MISCHIEF.nextInt(choices.length)];
    }
}
