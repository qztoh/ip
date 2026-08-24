import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Random;
import java.util.Scanner;

/**
 * A minimal command-line entry point for the Loki task manager.
 */
public class Loki {
    private static Boolean activeSession = true;
    private static Random mischief = new Random();
    private static String commands[] = new String[100];
    private static int commandsIDX = 0;
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
            command = command.toLowerCase();
            switch (command) {
            case "faretheewell":
                activeSession = false;
                exit();
                break;
            case "list":
                listCommands();
                break;
            default:
                add(command);
                echo("added: " + command);
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
        String farewells[] = { 
            "We will meet again, mortal", 
            "For you, for all of us",
            "Toodles~",
            "You'll be back",
            "Oh to be burdened with glorious purpose" 
        };
        activeSession = false;
        bannerHeavy();
        System.out.println("        " + farewells[mischief.nextInt(farewells.length)]);
        bannerHeavy();
    }
    private static void echo(String str) {
        banner();
        System.out.println("    " + str + "\n");
    }
    private static void add(String command) {
        commands[commandsIDX++] = command;
    }
    private static void listCommands() {
        banner();
        for (int i = 0; i < commandsIDX; i++) {
            System.out.println(String.format("      %s. %s", i + 1, commands[i]));
        }
        System.out.println("");
    }
}
