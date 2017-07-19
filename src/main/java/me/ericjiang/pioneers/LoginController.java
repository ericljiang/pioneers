package me.ericjiang.pioneers;

import spark.Route;

public class LoginController {
    public static Route loginPage = (request, response) -> {
        return "Hello world.";
    };
}