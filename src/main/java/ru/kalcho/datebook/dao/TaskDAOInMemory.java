package ru.kalcho.datebook.dao;

import ru.kalcho.datebook.model.Task;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

/**
 *
 */
public class TaskDAOInMemory implements TaskDAO {

    private Long lastId = 0L;
    private Map<Long, Task> tasks = new HashMap<>();

    private Long nextId() {
        return ++lastId;
    }

    @Override
    public Task save(Task task) {
        task.setId(nextId());
        tasks.put(task.getId(), task);
        return task;
    }

    @Override
    public Task update(Task task) {
        if (!tasks.containsKey(task.getId())) {
            throw new IllegalStateException();
        }
        tasks.put(task.getId(), task);
        return task;
    }

    @Override
    public Task find(Long id) {
        return tasks.get(id);
    }

    @Override
    public Collection<Task> findAll() {
        return tasks.values();
    }

    @Override
    public void clearAll() {
        tasks.clear();
    }

}
