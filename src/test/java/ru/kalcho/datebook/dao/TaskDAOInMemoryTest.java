package ru.kalcho.datebook.dao;

import org.junit.Before;
import org.junit.Test;
import ru.kalcho.datebook.model.Task;
import ru.kalcho.datebook.model.TaskStatus;

import java.time.LocalDateTime;

import static org.junit.Assert.*;

/**
 *
 */
public class TaskDAOInMemoryTest {

    private TaskDAO taskDAO;

    @Before
    public void setUp() throws Exception {
        this.taskDAO = new TaskDAOInMemory();
    }

    private Task createTask() {
        Task task = new Task();
        task.setTitle("Test task");
        task.setCreationTime(LocalDateTime.now());
        task.setStatus(TaskStatus.ACTIVE);
        task.setScheduledTime(LocalDateTime.now());
        return task;
    }

    @Test
    public void testSave() throws Exception {
        taskDAO.save(createTask());
        assertEquals(1, taskDAO.findAll().size());

        taskDAO.save(createTask());
        assertEquals(2, taskDAO.findAll().size());
    }

    @Test
    public void testSave_generatedIdIsValid() throws Exception {
        Task savedTask1 = taskDAO.save(createTask());
        assertEquals(1L, savedTask1.getId().longValue());

        Task savedTask2 = taskDAO.save(createTask());
        assertEquals(2L, savedTask2.getId().longValue());
    }

    @Test
    public void testUpdate() throws Exception {
        Task savedTask = taskDAO.save(createTask());

        Task updatedTask = createTask();
        updatedTask.setId(savedTask.getId());
        updatedTask.setTitle("Test task 2");
        taskDAO.update(updatedTask);

        assertEquals("Test task 2", taskDAO.find(savedTask.getId()).getTitle());
    }

    @Test(expected = IllegalStateException.class)
    public void testUpdate_notSavedTask() throws Exception {
        Task task = createTask();
        taskDAO.update(task);
    }

    @Test
    public void testFindAll() throws Exception {
        taskDAO.save(createTask());
        taskDAO.save(createTask());

        assertEquals(2, taskDAO.findAll().size());
    }

    @Test
    public void testClearAll() throws Exception {
        taskDAO.save(createTask());
        taskDAO.save(createTask());

        taskDAO.clearAll();

        assertEquals(0, taskDAO.findAll().size());
    }

    @Test
    public void testFind() throws Exception {
        Task savedTask = taskDAO.save(createTask());

        assertEquals(savedTask.getTitle(), taskDAO.find(savedTask.getId()).getTitle());
    }
}