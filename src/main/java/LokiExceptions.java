/**
 * Represents recoverable errors caused by invalid user input.
 *
 * <p>Each instance receives a randomly selected flavour message from
 * {@link LokiUi#randomExceptionMessage()}.</p>
 */
public class LokiExceptions extends Exception {
    /**
     * Creates an input error with a randomly selected Loki message.
     */
    public LokiExceptions() {
        super(LokiUi.randomExceptionMessage());
    }

    public LokiExceptions(String message) {
        super(LokiUi.randomExceptionMessage() + "\n" + message);
    }

    /**
     * Creates an error for an empty command.
     *
     * @return the empty-input error
     */
    public static LokiExceptions emptyInput() {
        return new LokiExceptions();
    }

    /**
     * Creates an error for an unrecognised command.
     *
     * @return the unknown-command error
     */
    public static LokiExceptions unknownCommand() {
        return new LokiExceptions();
    }

    /**
     * Creates an error for a missing or invalid task number.
     *
     * @return the invalid-task-number error
     */
    public static LokiExceptions invalidTaskNumber() {
        return new LokiExceptions();
    }

    /**
     * Creates an error for an incorrectly formatted to-do command.
     *
     * @return the invalid-to-do error
     */
    public static LokiExceptions invalidToDo() {
        return new LokiExceptions();
    }

    /**
     * Creates an error for an incorrectly formatted deadline command.
     *
     * @return the invalid-deadline error
     */
    public static LokiExceptions invalidDeadline() {
        return new LokiExceptions();
    }

    /**
     * Creates an error for an incorrectly formatted event command.
     *
     * @return the invalid-event error
     */
    public static LokiExceptions invalidEvent() {
        return new LokiExceptions();
    }

}
