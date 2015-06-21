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

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

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

}
