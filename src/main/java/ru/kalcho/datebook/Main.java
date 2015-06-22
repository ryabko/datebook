package ru.kalcho.datebook;

import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import ru.kalcho.datebook.dao.TaskDAO;
import ru.kalcho.datebook.helper.DatebookCalendar;
import ru.kalcho.datebook.model.Task;
import ru.kalcho.datebook.model.TaskStatus;
import ru.kalcho.datebook.service.TaskService;
import spark.ModelAndView;
import spark.template.freemarker.FreeMarkerEngine;

import java.sql.*;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static spark.Spark.get;
import static spark.Spark.post;

/**
 *
 */
public class Main {

    public static void main(String[] args) {
        Configuration freeMarkerConfiguration = new Configuration();
        freeMarkerConfiguration.setTemplateLoader(new ClassTemplateLoader(Main.class, "/web"));
        FreeMarkerEngine htmlEngine = new FreeMarkerEngine(freeMarkerConfiguration);

        System.out.println("args: " + Arrays.toString(args));

        testDatabase(args);

        TaskService taskService = new TaskService(new TaskDAO());

        get("/", (request, response) -> {
            Map<String, Object> attributes = new HashMap<>();

            LocalDateTime currentDate = request.queryParams("day") != null ?
                    LocalDateTime.parse(request.queryParams("day")) : LocalDateTime.now();
            attributes.put("calendar", new DatebookCalendar(currentDate));

            attributes.put("tasks", taskService.findByDay(currentDate));

            return new ModelAndView(attributes, "index.ftl");
        }, htmlEngine);

        post("/task", (request, response) -> {
            LocalDateTime scheduledDate = request.queryParams("day") != null ?
                    LocalDateTime.parse(request.queryParams("day")) : LocalDateTime.now();
            taskService.addTask(request.queryParams("title"), scheduledDate);

            response.redirect("/?day=" + request.queryParams("day"));
            return null;
        });

        post("/task-update", (request, response) -> {
            Long taskId = Long.valueOf(request.queryParams("id"));
            TaskStatus status = TaskStatus.valueOf(request.queryParams("status"));

            Task task = taskService.findById(taskId);
            task.setStatus(status);
            taskService.update(task);

            response.redirect("/?day=" + request.queryParams("returnDay"));
            return null;
        });
    }

    private static void testDatabase(String[] args) {
        Map<String, String> argsMap = Stream.of(args).collect(
                Collectors.toMap(arg -> arg.split("=")[0], arg -> arg.split("=")[1])
        );
        String dbHost = argsMap.get("dbHost");
        String dbName = argsMap.get("dbName");
        String dbUsername = argsMap.get("dbUsername");
        String dbPassword = argsMap.get("dbPassword");
        try {
            Connection connection = DriverManager.getConnection(
                    "jdbc:mysql://" + dbHost + "/" + dbName, dbUsername, dbPassword);
            Statement statement = connection.createStatement();
            ResultSet result = statement.executeQuery("select * from tasks");
            while (result.next()) {
                System.out.println("next row");
            }
            result.close();
            statement.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
