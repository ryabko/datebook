package ru.kalcho.datebook.service;

import org.junit.Before;
import org.junit.Test;
import ru.kalcho.datebook.dao.TaskDAOInMemory;
import ru.kalcho.datebook.model.Task;
import ru.kalcho.datebook.model.TaskStatus;

import java.time.LocalDateTime;
import java.util.List;

import static org.junit.Assert.*;

/**
 *
 */
public class TaskServiceTest {

    private TaskService taskService;

    @Before
    public void setUp() throws Exception {
        taskService = new TaskService(new TaskDAOInMemory());
    }

    @Test
    public void testAddTask() throws Exception {
        Task task = taskService.addTask("Test task", LocalDateTime.parse("2015-10-12T08:00"));
        assertEquals(1L, task.getId().longValue());
        assertEquals(task.getStatus(), TaskStatus.ACTIVE);
        assertNotNull(task.getCreationTime());

        assertEquals(1, taskService.findAll().size());
    }


    @Test
    public void testFindAll() throws Exception {
        taskService.addTask("Test task 1", LocalDateTime.parse("2015-10-12T09:00"));
        assertEquals(1, taskService.findAll().size());

        taskService.addTask("Test task 2", LocalDateTime.parse("2015-10-12T10:00"));
        assertEquals(2, taskService.findAll().size());
    }

    @Test
    public void testFindByDay() throws Exception {
        taskService.addTask("Test task 1", LocalDateTime.parse("2015-10-12T09:00"));
        taskService.addTask("Test task 2", LocalDateTime.parse("2015-10-13T10:00"));
        taskService.addTask("Test task 3", LocalDateTime.parse("2015-10-12T12:00"));

        List<Task> byDay = taskService.findByDay(LocalDateTime.parse("2015-10-12T00:00"));
        assertEquals(2, byDay.size());
    }

    @Test
    public void testFindById() throws Exception {
        Task task = taskService.addTask("Test task 1", LocalDateTime.parse("2015-10-12T09:00"));
        Task foundById = taskService.findById(task.getId());

        assertEquals(task.getTitle(), foundById.getTitle());
    }

    @Test
    public void testChangeStatus() throws Exception {
        Task task = taskService.addTask("Test task 1", LocalDateTime.parse("2015-10-12T09:00"));
        taskService.changeStatus(task, TaskStatus.DONE);

        assertEquals(TaskStatus.DONE, taskService.findById(task.getId()).getStatus());
    }

    @Test
    public void testUpdate() throws Exception {
        Task task = taskService.addTask("Test task 1", LocalDateTime.parse("2015-10-12T09:00"));

        Task updatedTask = taskService.findById(task.getId());
        updatedTask.setStatus(TaskStatus.DONE);
        taskService.update(updatedTask);

        assertEquals(TaskStatus.DONE, taskService.findById(task.getId()).getStatus());
    }
}