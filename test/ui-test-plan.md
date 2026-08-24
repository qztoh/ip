# Loki UI Test Plan

Run this plan from the repository root with Java 25.

- Program: `java -cp _temp/ui-classes Loki`
- Working directory: `.`
- Setup: `javac -d _temp/ui-classes src/main/java/Task.java src/main/java/ToDo.java src/main/java/Deadline.java src/main/java/Event.java src/main/java/LokiExceptions.java src/main/java/LokiDialogue.java src/main/java/Loki.java`

Expected-output lines are checked in order. The test runner ignores surrounding whitespace and treats each expected line as a required substring, so randomized flavour text does not make the tests brittle.

## Test Case UI-001: Add a task and list it

### Aim

Verify that the `todo` command creates an unfinished task and that `list` displays it.

### Inputs

```text
todo Buy groceries
list
exit
```

### Expected output

```text
You have 1 tasks left to conquer.
1. [T][ ] Buy groceries
```

## Test Case UI-002: Mark a task as complete

### Aim

Verify that `mark 1` marks the first task as complete.

### Inputs

```text
todo Write report
mark 1
list
exit
```

### Expected output

```text
You have 1 tasks left to conquer.
[T][X] Write report
```

## Test Case UI-003: Unmark a completed task

### Aim

Verify that `unmark 1` changes a completed task back to unfinished.

### Inputs

```text
todo Write report
mark 1
unmark 1
list
exit
```

### Expected output

```text
You have 1 tasks left to conquer.
[T][X] Write report
[T][ ] Write report
```

## Test Case UI-004: Commands are case-insensitive

### Aim

Verify that command keywords can be entered in uppercase while task titles retain their original capitalization.

### Inputs

```text
todo Read Documentation
MARK 1
LIST
EXIT
```

### Expected output

```text
You have 1 tasks left to conquer.
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
[D][ ] Return book (by: June 6th)
You have 1 tasks left to conquer.
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
[E][ ] Project meeting (from: Aug 6th 2pm to: 4pm)
You have 1 tasks left to conquer.
```

## Test Case UI-007: Reject an unrecognised command

### Aim

Verify that an unrecognised keyword does not create a task and produces a randomly selected Loki exception response.

### Inputs

```text
dance wildly
exit
```

### Expected output

```text
Loki error:
```

## Test Case UI-011: Add multiple tasks

### Aim

Verify that two valid tasks are stored independently and listed in insertion order.

### Inputs

```text
todo First task
todo Second task
list
exit
```

### Expected output

```text
You have 2 tasks left to conquer.
1. [T][ ] First task
2. [T][ ] Second task
```

## Test Case UI-012: Invalid command does not change task state

### Aim

Verify that an invalid command between two valid task commands does not create an extra task.

### Inputs

```text
todo First task
unknown command
todo Second task
list
exit
```

### Expected output

```text
Loki error:
You have 2 tasks left to conquer.
1. [T][ ] First task
2. [T][ ] Second task
```

## Test Case UI-013: Mark and unmark a valid task

### Aim

Verify that valid status changes work in sequence and leave the task unfinished after `unmark`.

### Inputs

```text
todo Complete me
mark 1
unmark 1
list
exit
```

### Expected output

```text
You have 1 tasks left to conquer.
[T][X] Complete me
[T][ ] Complete me
1. [T][ ] Complete me
```

## Test Case UI-014: Invalid task indices do not change task state

### Aim

Verify that zero, non-numeric, and out-of-range indices all produce errors without changing the task.

### Inputs

```text
todo Keep pending
mark 0
mark nope
mark 2
list
exit
```

### Expected output

```text
You have 1 tasks left to conquer.
Loki error:
1. [T][ ] Keep pending
```

## Test Case UI-015: Add deadline and event tasks together

### Aim

Verify that different task subclasses can coexist and retain their individual metadata.

### Inputs

```text
deadline Submit report /by June 6th
event Project meeting /from Aug 6th 2pm /to 4pm
list
exit
```

### Expected output

```text
[D][ ] Submit report (by: June 6th)
[E][ ] Project meeting (from: Aug 6th 2pm to: 4pm)
1. [D][ ] Submit report (by: June 6th)
2. [E][ ] Project meeting (from: Aug 6th 2pm to: 4pm)
```

## Test Case UI-016: Malformed task commands do not create tasks

### Aim

Verify that malformed deadline and event commands are rejected before a valid task is added.

### Inputs

```text
deadline Missing deadline
event Missing end /from 2pm
todo Valid after errors
list
exit
```

### Expected output

```text
Loki error:
Loki error:
You have 1 tasks left to conquer.
1. [T][ ] Valid after errors
```

## Test Case UI-008: Reject an invalid task number

### Aim

Verify that a non-numeric task number is handled without terminating the session.

### Inputs

```text
mark nope
exit
```

### Expected output

```text
Loki error:
```

## Test Case UI-009: Reject a malformed deadline

### Aim

Verify that a deadline without a `/by` time reports the correct usage message.

### Inputs

```text
deadline Return book
exit
```

### Expected output

```text
Loki error:
```

## Test Case UI-010: Reject a malformed event

### Aim

Verify that an event without both `/from` and `/to` times reports the correct usage message.

### Inputs

```text
event Project meeting /from Aug 6th 2pm
exit
```

### Expected output

```text
Loki error:
```
