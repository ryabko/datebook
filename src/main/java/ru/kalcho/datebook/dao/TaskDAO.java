package ru.kalcho.datebook.dao;

import ru.kalcho.datebook.model.Task;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class TaskDAO {

    private Long lastId = 0L;
    private Map<Long, Task> tasks = new HashMap<>();

    private Long nextId() {
        return ++lastId;
    }

    public Task save(Task task) {
        task.setId(nextId());
        tasks.put(task.getId(), task);
        return task;
    }

    public Task update(Task task) {
        if (!tasks.containsKey(task.getId())) {
            throw new IllegalStateException();
        }
        tasks.put(task.getId(), task);
        return task;
    }

    public Task find(Long id) {
        return tasks.get(id);
    }

    public Collection<Task> findAll() {
        return tasks.values();
    }

    public void clearAll() {
        tasks.clear();
    }

}
