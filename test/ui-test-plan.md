# Loki UI Test Plan

Run this plan from the repository root with Java 25.

- Program: `java -cp _temp/ui-classes Loki`
- Working directory: `.`
- Setup: `javac -d _temp/ui-classes src/main/java/Task.java src/main/java/ToDo.java src/main/java/Deadline.java src/main/java/Event.java src/main/java/LokiDialogue.java src/main/java/Loki.java`

Expected-output lines are checked in order. The test runner ignores surrounding whitespace and treats each expected line as a required substring, so randomized flavour text does not make the tests brittle.

## Test Case UI-001: Add a task and list it

### Aim

Verify that ordinary input creates an unfinished task and that `list` displays it.

### Inputs

```text
Buy groceries
list
exit
```

### Expected output

```text
added: Buy groceries
1. [T][ ] Buy groceries
```

## Test Case UI-002: Mark a task as complete

### Aim

Verify that `mark 1` marks the first task as complete.

### Inputs

```text
Write report
mark 1
list
exit
```

### Expected output

```text
added: Write report
[T][X] Write report
```

## Test Case UI-003: Unmark a completed task

### Aim

Verify that `unmark 1` changes a completed task back to unfinished.

### Inputs

```text
Write report
mark 1
unmark 1
list
exit
```

### Expected output

```text
added: Write report
[T][ ] Write report
```

## Test Case UI-004: Commands are case-insensitive

### Aim

Verify that command keywords can be entered in uppercase while task titles retain their original capitalization.

### Inputs

```text
Read Documentation
MARK 1
LIST
EXIT
```

### Expected output

```text
added: Read Documentation
[T][X] Read Documentation
```

## Test Case UI-005: Add a deadline task

### Aim

Verify that a `/by` marker creates a deadline task and preserves its deadline text.

### Inputs

```text
deadline Return book /by June 6th
list
exit
```

### Expected output

```text
1. [D][ ] Return book (by: June 6th)
```

## Test Case UI-006: Add an event task

### Aim

Verify that `/from` and `/to` markers create an event task with both times.

### Inputs

```text
event Project meeting /from Aug 6th 2pm /to 4pm
list
exit
```

### Expected output

```text
1. [E][ ] Project meeting (from: Aug 6th 2pm to: 4pm)
```
