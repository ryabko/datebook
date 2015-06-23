package ru.kalcho.datebook.dao;

import org.sql2o.Connection;
import org.sql2o.Sql2o;
import ru.kalcho.datebook.model.Task;

import java.util.Collection;
import java.util.List;

/**
 *
 */
public class TaskDAOSql2o implements TaskDAO {

    private Sql2o sql2o;

    public TaskDAOSql2o(Sql2o sql2o) {
        this.sql2o = sql2o;
    }

    @Override
    public Task save(Task task) {
        try (Connection connection = sql2o.open()) {
            Long id = (Long) connection.createQuery(
                    "insert into tasks (title, creation_time, scheduled_time, status) " +
                    "values (:title, :creationTime, :scheduledTime, :status)", true)
                    .bind(task).executeUpdate().getKey();
            task.setId(id);
        }
        return task;
    }

    @Override
    public Task update(Task task) {
        try (Connection connection = sql2o.open()) {
            connection.createQuery("update tasks set title = :title, creation_time = :creationTime, " +
                            "scheduled_time = :scheduledTime, status = :status where id = :id")
                    .bind(task).executeUpdate();
        }
        return task;
    }

    @Override
    public Task find(Long id) {
        try (Connection connection = sql2o.open()) {
            return connection.createQuery(
                    "select id, title, creation_time creationTime, scheduled_time scheduledTime, status " +
                            "from tasks where id = :id")
                    .addParameter("id", id)
                    .executeAndFetch(Task.class)
                    .stream().findFirst().orElse(null);
        }
    }

    @Override
    public Collection<Task> findAll() {
        try (Connection connection = sql2o.open()) {
            return connection.createQuery(
                    "select id, title, creation_time creationTime, scheduled_time scheduledTime, status from tasks")
                    .executeAndFetch(Task.class);
        }
    }

    @Override
    public void clearAll() {
        try (Connection connection = sql2o.open()) {
            connection.createQuery("delete from tasks").executeUpdate();
        }
    }
}
