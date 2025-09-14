package buddy;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;

/**
 * Manages a list of tasks with operations for adding, deleting, and modifying tasks.
 * Provides functionality to mark tasks as done/undone and retrieve tasks by index.
 */
public class TaskList {
    private ArrayList<Task> tasks;
    
    public TaskList() {
        this.tasks = new ArrayList<>();
    }
    
    public TaskList(ArrayList<Task> tasks) {
        this.tasks = tasks;
    }
    
    /**
     * Adds a task to the task list.
     * 
     * @param task the task to be added
     */
    public void addTask(Task task) {
        assert task != null : "Task should not be null when adding to task list";
        tasks.add(task);
    }
    
    public void deleteTask(int index) throws BuddyException {
        validateTaskIndex(index, "delete");
        assert !tasks.isEmpty() : "Task list should not be empty when deleting";
        tasks.remove(index);
    }

    public Task getTask(int index) throws BuddyException {
        validateTaskIndex(index, "access");
        return tasks.get(index);
    }

    private void validateTaskIndex(int index, String operation) throws BuddyException {
        if (tasks.isEmpty()) {
            throw new BuddyException("📋 Your task list is empty! Add some tasks first before trying to " +
                                   operation + " them! Use 'todo', 'deadline', or 'event' to add tasks! ✨");
        }

        if (index < 0) {
            throw new BuddyException("🔢 Task numbers start from 1, not " + (index + 1) + "! " +
                                   "Please use positive numbers! 📊");
        }

        if (index >= tasks.size()) {
            String suggestion = tasks.size() == 1
                ? "You only have 1 task, so use '1'! 🎯"
                : String.format("You have %d tasks, so use numbers 1 to %d! 📋", tasks.size(), tasks.size());

            throw new BuddyException("🎯 Task number " + (index + 1) + " doesn't exist! " + suggestion);
        }
    }
    
    public void markTask(int index) throws BuddyException {
        getTask(index).markAsDone();
    }
    
    public void unmarkTask(int index) throws BuddyException {
        getTask(index).markAsNotDone();
    }
    
    public int size() {
        return tasks.size();
    }
    
    public Task[] getTaskArray() {
        return tasks.toArray(new Task[tasks.size()]);
    }
    
    public ArrayList<Task> getTasks() {
        return tasks;
    }
    
    public Task[] findTasks(String keyword) {
        String lowerKeyword = keyword.toLowerCase();
        return tasks.stream()
                .filter(task -> task.getDescription().toLowerCase().contains(lowerKeyword))
                .toArray(Task[]::new);
    }
    
    public long getCompletedTaskCount() {
        return tasks.stream()
                .filter(Task::isDone)
                .count();
    }
    
    public Task[] getCompletedTasks() {
        return tasks.stream()
                .filter(Task::isDone)
                .toArray(Task[]::new);
    }
    
    public void sortByDescription() {
        Collections.sort(tasks, Comparator.comparing(Task::getDescription));
    }
    
    public void sortByStatus() {
        Collections.sort(tasks, (task1, task2) -> {
            // Incomplete tasks first, then completed tasks
            if (task1.isDone() == task2.isDone()) {
                return task1.getDescription().compareTo(task2.getDescription());
            }
            return task1.isDone() ? 1 : -1;
        });
    }
    
    public void sortByType() {
        Collections.sort(tasks, (task1, task2) -> {
            int typeComparison = task1.getClass().getSimpleName().compareTo(task2.getClass().getSimpleName());
            if (typeComparison == 0) {
                return task1.getDescription().compareTo(task2.getDescription());
            }
            return typeComparison;
        });
    }
}