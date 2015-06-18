package ru.kalcho.datebook;

import freemarker.cache.ClassTemplateLoader;
import freemarker.template.Configuration;
import ru.kalcho.datebook.dao.TaskDAO;
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
            attributes.put("tasks", taskService.findAll());
            return new ModelAndView(attributes, "index.ftl");
        }, htmlEngine);

        post("/task", (request, response) -> {
            taskService.addTask(request.queryParams("title"), LocalDateTime.now());
            response.redirect("/");
            return null;
        });
    }

}
