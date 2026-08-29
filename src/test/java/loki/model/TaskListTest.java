package loki.model;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import loki.exception.LokiExceptions;

/** Tests task storage, lookup, mutation, and read-only access in {@link TaskList}. */
class TaskListTest {
    private TaskList taskList;
    private Task firstTask;
    private Task secondTask;
    private Task thirdTask;

    @BeforeEach
    void setUp() {
        taskList = new TaskList();
        firstTask = new ToDo("first task");
        secondTask = new ToDo("second task");
        thirdTask = new ToDo("third task");
        taskList.add(firstTask);
        taskList.add(secondTask);
        taskList.add(thirdTask);
    }

    @Test
    void add_task_appendsTaskAndUpdatesListState() throws LokiExceptions {
        TaskList emptyTaskList = new TaskList();
        Task task = new ToDo("new task");

        emptyTaskList.add(task);

        assertEquals(1, emptyTaskList.size());
        assertFalse(emptyTaskList.isEmpty());
        assertSame(task, emptyTaskList.get(1));
    }

    @Test
    void add_nullTask_throwsIllegalArgumentExceptionAndLeavesListUnchanged() {
        int originalSize = taskList.size();

        assertThrows(IllegalArgumentException.class, () -> taskList.add(null));

        assertEquals(originalSize, taskList.size());
    }

    @Test
    void addAll_iterableOfTasks_appendsTasksInOrder() throws LokiExceptions {
        TaskList emptyTaskList = new TaskList();
        List<Task> addedTasks = List.of(new ToDo("fourth task"), new ToDo("fifth task"));

        emptyTaskList.addAll(addedTasks);

        assertEquals(2, emptyTaskList.size());
        assertSame(addedTasks.get(0), emptyTaskList.get(1));
        assertSame(addedTasks.get(1), emptyTaskList.get(2));
    }

    @Test
    void addAll_existingTasks_appendsWithoutReplacingThem() throws LokiExceptions {
        List<Task> addedTasks = List.of(new ToDo("fourth task"), new ToDo("fifth task"));

        taskList.addAll(addedTasks);

        assertEquals(5, taskList.size());
        assertSame(firstTask, taskList.get(1));
        assertSame(secondTask, taskList.get(2));
        assertSame(thirdTask, taskList.get(3));
        assertSame(addedTasks.get(0), taskList.get(4));
        assertSame(addedTasks.get(1), taskList.get(5));
    }

    @Test
    void addAll_nullIterable_throwsIllegalArgumentExceptionAndLeavesListUnchanged() {
        int originalSize = taskList.size();

        assertThrows(IllegalArgumentException.class, () -> taskList.addAll(null));

        assertEquals(originalSize, taskList.size());
    }

    @Test
    void addAll_iterableContainingNull_throwsIllegalArgumentException() {
        List<Task> tasksWithNull = Arrays.asList(new ToDo("valid task"), null);

        assertThrows(IllegalArgumentException.class, () -> taskList.addAll(tasksWithNull));
    }

    @Test
    void get_validOneBasedIndex_returnsCorrespondingTask() throws LokiExceptions {
        assertSame(firstTask, taskList.get(1));
        assertSame(secondTask, taskList.get(2));
        assertSame(thirdTask, taskList.get(3));
    }

    @Test
    void get_zeroIndex_throwsLokiExceptions() {
        assertThrows(LokiExceptions.class, () -> taskList.get(0));
    }

    @Test
    void get_negativeIndex_throwsLokiExceptions() {
        assertThrows(LokiExceptions.class, () -> taskList.get(-1));
    }

    @Test
    void get_indexAfterLastTask_throwsLokiExceptions() {
        assertThrows(LokiExceptions.class, () -> taskList.get(taskList.size() + 1));
    }

    @Test
    void get_emptyList_throwsLokiExceptions() {
        TaskList emptyTaskList = new TaskList();

        assertThrows(LokiExceptions.class, () -> emptyTaskList.get(1));
    }

    @Test
    void delete_validIndex_removesAndReturnsTask() throws LokiExceptions {
        Task deletedTask = taskList.delete(1);

        assertSame(firstTask, deletedTask);
        assertEquals(2, taskList.size());
        assertSame(secondTask, taskList.get(1));
        assertSame(thirdTask, taskList.get(2));
    }

    @Test
    void delete_middleIndex_removesSelectedTaskAndReindexesFollowingTasks() throws LokiExceptions {
        Task deletedTask = taskList.delete(2);

        assertSame(secondTask, deletedTask);
        assertSame(firstTask, taskList.get(1));
        assertSame(thirdTask, taskList.get(2));
    }

    @Test
    void delete_zeroOrOutOfRangeIndex_throwsLokiExceptions() {
        assertThrows(LokiExceptions.class, () -> taskList.delete(0));
        assertThrows(LokiExceptions.class, () -> taskList.delete(-1));
        assertThrows(LokiExceptions.class, () -> taskList.delete(taskList.size() + 1));
        assertEquals(3, taskList.size());
    }

    @Test
    void delete_fromEmptyList_throwsLokiExceptions() {
        TaskList emptyTaskList = new TaskList();

        assertThrows(LokiExceptions.class, () -> emptyTaskList.delete(1));
    }

    @Test
    void mark_validIndex_marksAndReturnsTask() throws LokiExceptions {
        Task markedTask = taskList.mark(2);

        assertSame(secondTask, markedTask);
        assertTrue(secondTask.isDone());
        assertFalse(firstTask.isDone());
        assertFalse(thirdTask.isDone());
    }

    @Test
    void mark_alreadyCompletedTask_remainsCompletedAndReturnsTask() throws LokiExceptions {
        secondTask.markDone();

        Task markedTask = taskList.mark(2);

        assertSame(secondTask, markedTask);
        assertTrue(secondTask.isDone());
    }

    @Test
    void unmark_validIndex_unmarksAndReturnsTask() throws LokiExceptions {
        secondTask.markDone();

        Task unmarkedTask = taskList.unmark(2);

        assertSame(secondTask, unmarkedTask);
        assertFalse(secondTask.isDone());
    }

    @Test
    void unmark_alreadyIncompleteTask_remainsIncompleteAndReturnsTask() throws LokiExceptions {
        Task unmarkedTask = taskList.unmark(2);

        assertSame(secondTask, unmarkedTask);
        assertFalse(secondTask.isDone());
    }

    @Test
    void unmark_onlyChangesSelectedTask() throws LokiExceptions {
        firstTask.markDone();
        secondTask.markDone();
        thirdTask.markDone();

        taskList.unmark(2);

        assertTrue(firstTask.isDone());
        assertFalse(secondTask.isDone());
        assertTrue(thirdTask.isDone());
    }

    @Test
    void markOrUnmark_invalidIndex_throwsLokiExceptions() {
        assertThrows(LokiExceptions.class, () -> taskList.mark(0));
        assertThrows(LokiExceptions.class, () -> taskList.unmark(taskList.size() + 1));
    }

    @Test
    void sizeAndIsEmpty_reflectAddAndDeleteOperations() throws LokiExceptions {
        TaskList emptyTaskList = new TaskList();
        assertTrue(emptyTaskList.isEmpty());
        assertEquals(0, emptyTaskList.size());

        emptyTaskList.add(new ToDo("temporary task"));
        assertFalse(emptyTaskList.isEmpty());
        assertEquals(1, emptyTaskList.size());

        emptyTaskList.delete(1);
        assertTrue(emptyTaskList.isEmpty());
        assertEquals(0, emptyTaskList.size());
    }

    @Test
    void find_keywordMatchesDescriptionCaseInsensitivelyInOrder() {
        TaskList searchableTaskList = new TaskList();
        Task matchingTask = new ToDo("Read the project guide");
        Task nonMatchingTask = new ToDo("Buy groceries");
        Task secondMatchingTask = new ToDo("Write guide summary");
        searchableTaskList.add(matchingTask);
        searchableTaskList.add(nonMatchingTask);
        searchableTaskList.add(secondMatchingTask);

        List<Task> matchingTasks = searchableTaskList.find("GUIDE");

        assertIterableEquals(List.of(matchingTask, secondMatchingTask), matchingTasks);
    }

    @Test
    void find_noMatch_returnsEmptyList() {
        TaskList searchableTaskList = new TaskList();
        searchableTaskList.add(new ToDo("Buy groceries"));

        assertTrue(searchableTaskList.find("dentist").isEmpty());
    }

    @Test
    void find_blankKeyword_throwsIllegalArgumentException() {
        TaskList searchableTaskList = new TaskList();

        assertThrows(IllegalArgumentException.class, () -> searchableTaskList.find("  "));
    }

    @Test
    void asList_returnsReadOnlyViewThatReflectsListContents() {
        List<Task> view = taskList.asList();

        assertIterableEquals(List.of(firstTask, secondTask, thirdTask), view);
        assertThrows(UnsupportedOperationException.class,
                () -> view.add(new ToDo("not allowed")));

        taskList.add(new ToDo("new task"));
        assertEquals(4, view.size());
    }

    @Test
    void iterator_traversesTasksInInsertionOrder() {
        assertIterableEquals(List.of(firstTask, secondTask, thirdTask), taskList);
    }
}
