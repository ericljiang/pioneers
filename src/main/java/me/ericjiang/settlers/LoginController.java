package me.ericjiang.settlers;

import spark.Route;

public class LoginController {
    public static Route loginPage = (request, response) -> {
        return "Hello world.";
    };
}