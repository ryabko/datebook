package ru.kalcho.datebook.service;

import ru.kalcho.datebook.dao.TaskDAO;
import ru.kalcho.datebook.model.Task;
import ru.kalcho.datebook.model.TaskStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

/**
 *
 */
public class TaskService {

    private TaskDAO taskDAO;

    public TaskService(TaskDAO taskDAO) {
        this.taskDAO = taskDAO;
    }

    public Task addTask(String title, LocalDateTime scheduledTime) {
        Task task = new Task();
        task.setTitle(title);
        task.setScheduledTime(scheduledTime);
        task.setCreationTime(LocalDateTime.now());
        task.setStatus(TaskStatus.ACTIVE);

        return taskDAO.save(task);
    }

    public List<Task> findAll() {
        return new ArrayList<>(taskDAO.findAll());
    }

}
