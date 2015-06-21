package ru.kalcho.datebook.service;

import ru.kalcho.datebook.dao.TaskDAO;
import ru.kalcho.datebook.model.Task;
import ru.kalcho.datebook.model.TaskStatus;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

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

    public List<Task> findByDay(LocalDateTime day) {
        return taskDAO.findAll().stream()
                .filter(task -> task.getScheduledTime().getDayOfYear() == day.getDayOfYear()
                        && task.getScheduledTime().getYear() == day.getYear())
                .collect(Collectors.toList());
    }

    // TODO: update by individual fields, not whole object ?
    public Task update(Task task) {
        return taskDAO.update(task);
    }

    public Task findById(Long id) {
        return taskDAO.find(id);
    }

    public Task changeStatus(Task task, TaskStatus newStatus) {
        task.setStatus(newStatus);
        return taskDAO.update(task);
    }

}
