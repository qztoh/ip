package loki.logic;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

/** Tests GUI-facing command processing in {@link Logic}. */
class LogicTest {
    @TempDir
    Path temporaryDirectory;

    @Test
    void processCommand_todoAndList_returnsAddedTask() {
        Logic logic = createLogic();

        String addResponse = logic.processCommand("todo Buy groceries");
        String listResponse = logic.processCommand("list");

        assertTrue(addResponse.contains("[T][ ] Buy groceries"));
        assertTrue(listResponse.contains("1. [T][ ] Buy groceries"));
    }

    @Test
    void processCommand_markAndUnmark_updatesTaskStatus() {
        Logic logic = createLogic();
        logic.processCommand("todo Write report");

        String markResponse = logic.processCommand("mark 1");
        String unmarkResponse = logic.processCommand("unmark 1");

        assertTrue(markResponse.contains("[T][X] Write report"));
        assertTrue(unmarkResponse.contains("[T][ ] Write report"));
    }

    @Test
    void processCommand_invalidCommand_returnsErrorResponse() {
        Logic logic = createLogic();

        String response = logic.processCommand("dance wildly");

        assertTrue(response.contains("Loki error:"));
    }

    @Test
    void processCommand_invalidTodo_returnsUsageTip() {
        Logic logic = createLogic();

        String response = logic.processCommand("todo");

        assertTrue(response.contains("Usage: todo <task description>"));
        assertTrue(response.contains("Example: todo Buy groceries"));
    }

    @Test
    void processCommand_invalidDeadline_returnsUsageTip() {
        Logic logic = createLogic();

        String response = logic.processCommand("deadline Return book");

        assertTrue(response.contains("Usage: deadline <task description> /by <date/time>"));
        assertTrue(response.contains("Example: deadline Return book /by 2019-06-06"));
    }

    @Test
    void processCommand_invalidEvent_returnsUsageTip() {
        Logic logic = createLogic();

        String response = logic.processCommand("event Project meeting /from Aug 6th 2pm");

        assertTrue(response.contains("Usage: event <title> /from <date/time> /to <date/time>"));
        assertTrue(response.contains("Example: event Project meeting /from 2019-08-06 1400"));
    }

    @Test
    void processCommand_exit_savesTasks() {
        Logic logic = createLogic();
        logic.processCommand("todo Remember to rest");

        String response = logic.processCommand("exit");
        Logic reloadedLogic = createLogic();

        assertTrue(response.contains("Farewell, mortal."));
        assertTrue(reloadedLogic.processCommand("list").contains("Remember to rest"));
    }

    /**
     * Creates logic backed by an isolated temporary file.
     *
     * @return isolated command-processing logic.
     */
    private Logic createLogic() {
        return new Logic(temporaryDirectory.resolve("tasks.txt").toString());
    }
}
