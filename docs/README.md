# Loki User Guide

Loki is a command-line task manager for to-dos, deadlines, and events.

## Getting help

Type `help` at any time to see all supported commands, their usage, and examples. Loki also suggests this command in the opening greeting.

## Updating tasks

Use `update` with a one-based task number and one or more supported fields. Fields may appear in any order and are case-insensitive.

```text
update TASK_NUMBER FIELD VALUE [FIELD VALUE ...]
```

Supported fields are:

- to-do: `/title`
- deadline: `/title`, `/by`
- event: `/title`, `/from`, `/to`

For example:

```text
update 1 /title Buy groceries and toiletries
update 2 /by 20/9/2026 1800
update 3 /title Project consultation /from 20/9/2026 1400 /to 20/9/2026 1600
```

An update preserves the task type, completion status, list position, and every omitted field. Invalid updates are rejected without changing the task. Updated tasks are saved at the normal application save point.

## Adding deadlines

// Describe the action and its outcome.

// Give examples of usage

Example: `keyword (optional arguments)`

// A description of the expected outcome goes here

```
expected output
```

## Feature ABC

// Feature details


## Feature XYZ

// Feature details
