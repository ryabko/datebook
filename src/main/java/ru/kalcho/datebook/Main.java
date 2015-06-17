package ru.kalcho.datebook;

import static spark.Spark.get;
import static spark.SparkBase.staticFileLocation;

/**
 *
 */
public class Main {

    public static void main(String[] args) {
        staticFileLocation("/web");
        get("/hello", (request, response) -> "Hello, world!");
    }

}
