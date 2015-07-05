package ru.kalcho.datebook;

import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import org.sql2o.Sql2o;
import org.sql2o.quirks.NoQuirks;
import ru.kalcho.datebook.dao.TaskDAOSql2o;
import ru.kalcho.datebook.helper.DatebookCalendar;
import ru.kalcho.datebook.model.Task;
import ru.kalcho.datebook.model.TaskStatus;
import ru.kalcho.datebook.service.TaskService;
import ru.kalcho.datebook.util.JsonUtils;
import ru.kalcho.sql2o.LocalDateTimeConverter;
import spark.ModelAndView;
import spark.template.freemarker.FreeMarkerEngine;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static spark.Spark.*;

/**
 *
 */
public class Main {

    public static void main(String[] args) {
        Configuration freeMarkerConfiguration = new Configuration();
        freeMarkerConfiguration.setTemplateLoader(new ClassTemplateLoader(Main.class, "/web"));
        FreeMarkerEngine htmlEngine = new FreeMarkerEngine(freeMarkerConfiguration);

        Map<String, String> opts = parseOptions(args);
        Sql2o sql2o = new Sql2o("jdbc:mysql://" + opts.get("dbHost") + "/" + opts.get("dbName"),
                opts.get("dbUsername"), opts.get("dbPassword"), new NoQuirks() {{
            converters.put(LocalDateTime.class, new LocalDateTimeConverter());
        }});

        TaskService taskService = new TaskService(new TaskDAOSql2o(sql2o));

        staticFileLocation("/web");

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

        get("/api/tasks", (request, response) -> {
            LocalDateTime day = request.queryParams("day") != null ?
                    LocalDateTime.parse(request.queryParams("day")) : LocalDateTime.now();

            List<Task> tasks = taskService.findByDay(day);
            response.type("application/json; charset=UTF-8");
            return JsonUtils.objectToJSON(tasks);
        });

        put("/api/tasks", (request, response) -> {
            Task inputTask = JsonUtils.jsonToObject(request.body(), Task.class);

            LocalDateTime scheduledTime = inputTask.getScheduledTime() != null ?
                    inputTask.getScheduledTime() : LocalDateTime.now();

            Task savedTask = taskService.addTask(inputTask.getTitle(), scheduledTime);
            response.type("application/json; charset=UTF-8");
            return JsonUtils.objectToJSON(savedTask);
        });

        post("/api/tasks", (request, response) -> {
            Task inputTask = JsonUtils.jsonToObject(request.body(), Task.class);
            Task foundTask = taskService.findById(inputTask.getId());
            if (inputTask.getStatus() != null) {
                foundTask.setStatus(inputTask.getStatus());
            }
            if (inputTask.getScheduledTime() != null) {
                foundTask.setScheduledTime(inputTask.getScheduledTime());
            }
            Task savedTask = taskService.update(foundTask);
            response.type("application/json; charset=UTF-8");
            return JsonUtils.objectToJSON(savedTask);
        });
    }

    private static Map<String, String> parseOptions(String[] args) {
        return Stream.of(args).collect(
                Collectors.toMap(arg -> arg.split("=")[0], arg -> arg.split("=")[1])
        );
    }

}
