package ru.kalcho.datebook.dao;

import ru.kalcho.datebook.model.Task;

import java.util.Collection;

/**
 *
 */
public interface TaskDAO {

    Task save(Task task);

    Task update(Task task);

    Task find(Long id);

    Collection<Task> findAll();

    void clearAll();
}
