package ru.kalcho.datebook.service;

import org.junit.Before;
import org.junit.Test;
import ru.kalcho.datebook.dao.TaskDAO;
import ru.kalcho.datebook.model.Task;
import ru.kalcho.datebook.model.TaskStatus;

import java.time.LocalDateTime;

import static org.junit.Assert.*;

/**
 *
 */
public class TaskServiceTest {

    private TaskService taskService;

    @Before
    public void setUp() throws Exception {
        taskService = new TaskService(new TaskDAO());
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
}