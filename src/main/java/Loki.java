import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Scanner;

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

        Scanner scanner = new Scanner(System.in);
        boolean activeSession = true;

        while (activeSession) {
            String command = scanner.nextLine();
            if (command.equalsIgnoreCase("faretheewell")) {
                activeSession = false;
                exit();
            } else {
                echo(command);
            }
        }
    }

    private static void bannerHeavy() {
        System.out.println("======================================================");
    }
    private static void banner() {
        System.out.println("------------------------------------------------------");
    }
    private static void greeting() {
        bannerHeavy();
        System.out.println("        Greetings, mortal");
        System.out.println("        Loki the Trickster God at your service");
        bannerHeavy();
    }
    private static void exit() {
        activeSession = false;
        bannerHeavy();
        System.out.println("        We will meet again, mortal");
        bannerHeavy();
    }
    private static void echo(String str) {
        banner();
        System.out.println("    " + str + "\n");
    }
}
