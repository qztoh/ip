package loki;

import java.io.IOException;

import loki.exception.LokiExceptions;
import loki.model.Task;
import loki.model.TaskList;
import loki.parser.Parser;
import loki.storage.Storage;
import loki.ui.Ui;

/**
 * Coordinates user interaction, command parsing, task operations, and storage.
 */
public class Loki {
    /** Creates an application coordinator. */
    public Loki() {
    }

    /**
     * Starts the Loki task manager and processes commands until the session ends.
     *
     * @param args command-line arguments, which are not used
     * @throws IOException if the welcome assets cannot be read
     */
    public static void main(String[] args) throws IOException {
        Ui ui = new Ui();
        Storage storage = new Storage();
        Parser parser = new Parser();
        TaskList tasks = new TaskList();

        try {
            tasks.addAll(storage.load());
        } catch (LokiExceptions exception) {
            ui.showError(exception.getMessage());
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
                            // Fallthrough
                        case "exit":
                            storage.save(tasks);
                            activeSession = false;
                            ui.showFarewell();
                            break;
                        case "list":
                            if (tasks.isEmpty()) {
                                ui.echo("You lack any tasks");
                            } else {
                                ui.showTaskList(tasks);
                            }
                            break;
                        case "mark":
                            ui.showSuccess(tasks.mark(parser.parseTaskNumber(input)).toString());
                            break;
                        case "unmark":
                            ui.showSuccess(tasks.unmark(parser.parseTaskNumber(input)).toString());
                            break;
                        case "todo":
                            // Fallthrough
                        case "deadline":
                            // Fallthrough
                        case "event":
                            Task task = parser.parseTask(input);
                            tasks.add(task);
                            ui.showTaskAdded(task, tasks.size());
                            break;
                        case "delete":
                            Task deletedTask = tasks.delete(parser.parseTaskNumber(input));
                            ui.showTaskDeleted(deletedTask, tasks.size());
                            break;
                        default:
                            throw LokiExceptions.unknownCommand();
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
                    case "find":
                        ui.showSearchResults(tasks,
                                tasks.find(parser.parseSearchKeyword(input)));
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
                    ui.showError(exception.getMessage());
                }
            }
        } finally {
            ui.close();
        }
    }
}
