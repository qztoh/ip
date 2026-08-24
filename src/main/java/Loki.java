import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * A minimal command-line entry point for the Loki task manager.
 */
public class Loki {
    public static Boolean activeSession = true;
    public static void main(String[] args) throws IOException {
        String splash = "|        ____   |   / |____|\n"
                + "|       /    \\  |  /     |  \n"
                + "|       |    |  |<       |  \n"
                + "|       \\    /  |  \\     |  \n"
                + "|_______ \\__/   |   \\ |____|\n";

        Path pathLoki = Path.of("loki.txt");
        String logo = Files.readString(pathLoki);

        banner();
        System.out.println(splash);
        System.out.println(logo);
        greeting();
        exit();
    }

    private static void banner() {
        System.out.println("======================================================");
    }
    private static void greeting() {
        banner();
        System.out.println("Greetings, mortal");
        System.out.println("Loki the Trickster God at your service");
    }
    private static void exit() {
        activeSession = false;
        banner();
        System.out.println("We will meet again, mortal");
        banner();
    }
}
