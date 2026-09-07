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

    /**
     * Creates logic backed by an isolated temporary file.
     *
     * @return isolated command-processing logic.
     */
    private Logic createLogic() {
        return new Logic(temporaryDirectory.resolve("tasks.txt").toString());
    }
}
