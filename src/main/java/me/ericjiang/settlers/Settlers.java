package me.ericjiang.settlers;

import static spark.Spark.*;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class Settlers {
    
    public static void main(String[] args) {
        log.info("Hello world.");
        port(getHerokuAssignedPort());
        get(Routes.LOGIN, LoginController.loginPage);
    }

    static int getHerokuAssignedPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 4567; //return default port if heroku-port isn't set (i.e. on localhost)
    }
}