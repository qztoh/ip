package loki.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

import loki.exception.LokiExceptions;

/** Tests task command boundaries and case-insensitive delimiters. */
class ParserTest {
    private final Parser parser = new Parser();

    @Test
    void parseTask_mixedCaseDeadline_preservesTitleAndDueDate() throws LokiExceptions {
        assertEquals("D | 0 | Return Book | 2019-06-06T00:00:00",
                parser.parseTask("  DeAdLiNe Return Book /BY 2019-06-06  ").saveString());
    }

    @Test
    void parseTask_mixedCaseEvent_preservesBothTimes() throws LokiExceptions {
        assertEquals("E | 0 | Team Meeting | 2019-08-06T14:00:00 -> 2019-08-06T16:00:00",
                parser.parseTask("EVENT Team Meeting /FROM 2019-08-06 1400 /TO 2019-08-06 1600").saveString());
    }

    @Test
    void parseTask_missingDeadlineFields_throwsLokiExceptions() {
        String[] commands = {"deadline Return book", "deadline /by 2019-06-06", "deadline Return book /by "};
        for (String command : commands) {
            assertThrows(LokiExceptions.class, () -> parser.parseTask(command), command);
        }
    }

    @Test
    void parseTask_missingOrReversedEventMarkers_throwsLokiExceptions() {
        String[] commands = {
            "event Meeting /from 2019-08-06 1400",
            "event Meeting /to 2019-08-06 1600",
            "event Meeting /to 2019-08-06 1600 /from 2019-08-06 1400",
            "event Meeting /from /to 2019-08-06 1600",
            "event Meeting /from 2019-08-06 1400 /to "
        };
        for (String command : commands) {
            assertThrows(LokiExceptions.class, () -> parser.parseTask(command), command);
        }
    }

    @Test
    void parseTask_invalidDatesOrReversedInterval_throwsLokiExceptions() {
        assertThrows(LokiExceptions.class, () -> parser.parseTask("deadline Book /by 2019-02-30"));
        String reversedEvent = "event Meeting /from 2019-08-06 1600 /to 2019-08-06 1400";
        assertThrows(LokiExceptions.class, () -> parser.parseTask(reversedEvent));
    }
}
