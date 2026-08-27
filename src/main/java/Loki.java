import java.io.IOException;

/**
 * Coordinates user interaction, command parsing, task operations, and storage.
 */
public class Loki {
    /** Starts the Loki task manager. */
    public static void main(String[] args) throws IOException {
        Ui ui = new Ui();
        Storage storage = new Storage();
        Parser parser = new Parser();
        TaskList tasks = new TaskList();

        try {
            tasks.addAll(storage.load());
        } catch (LokiExceptions exception) {
            ui.error(exception.getMessage());
            ui.close();
            return;
        }

        try {
            ui.showWelcome();
            boolean activeSession = true;
            while (activeSession && ui.hasNextLine()) {
                String input = ui.readLine();
                try {
                    String keyword = parser.parseKeyword(input);
                    switch (keyword) {
                    case "faretheewell":
                    case "exit":
                        storage.save(tasks);
                        activeSession = false;
                        ui.exit();
                        break;
                    case "list":
                        if (tasks.isEmpty()) {
                            ui.echo("You lack any tasks");
                        } else {
                            ui.listTasks(tasks);
                        }
                        break;
                    case "mark":
                        ui.obedient(tasks.mark(parser.parseTaskNumber(input)).toString());
                        break;
                    case "unmark":
                        ui.obedient(tasks.unmark(parser.parseTaskNumber(input)).toString());
                        break;
                    case "todo":
                    case "deadline":
                    case "event":
                        Task task = parser.parseTask(input);
                        tasks.add(task);
                        ui.taskAdded(task, tasks.size());
                        break;
                    case "delete":
                        Task deletedTask = tasks.delete(parser.parseTaskNumber(input));
                        ui.taskDeleted(deletedTask, tasks.size());
                        break;
                    default:
                        throw LokiExceptions.unknownCommand();
                    }
                } catch (LokiExceptions | IllegalArgumentException exception) {
                    ui.error(exception.getMessage());
                }
            }
        } finally {
            ui.close();
        }
    }
}
