import java.util.Random;

/**
 * Contains Loki's presentation and flavour text.
 */
public class LokiDialogue {
    private static final Random MISCHIEF = new Random();
    private static final String[] FAREWELLS = {
        "We will meet again, mortal",
        "For you, for all of us",
        "Toodles~",
        "You'll be back",
        "Oh to be burdened with glorious purpose"
    };
    private static final String[] INVALID_TASK_RESPONSES = {
        "That task does not exist. Unless, of course, you've discovered a way to rearrange reality without telling me.",
        "An invalid index. Remarkable. Even I cannot mark a task that isn't there.",
        "I searched the list. Nothing. You appear to have assigned meaning to an empty space.",
        "That task is beyond the bounds of this list. A rather unfortunate little oversight.",
        "You've asked me to alter something that does not exist. I'm good, but I'm not that good.",
        "Incorrect index. Shall we try again before I blame this entirely on the mortal operating the controls?"
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
        bannerHeavy();
        System.out.println("        Greetings, mortal");
        System.out.println("        Loki the Trickster God at your service");
        bannerHeavy();
    }

    /**
     * Prints a randomly selected farewell.
     */
    public static void exit() {
        bannerHeavy();
        System.out.println("        " + pickRandom(FAREWELLS));
        bannerHeavy();
    }

    /**
     * Prints a response to a user's input.
     *
     * @param text the response to print
     */
    public static void echo(String text) {
        banner();
        System.out.println("    " + text + "\n");
    }

    /**
     * Prints a randomly selected response for an invalid task index.
     */
    public static void youarestupid() {
        System.out.println(pickRandom(INVALID_TASK_RESPONSES));
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
