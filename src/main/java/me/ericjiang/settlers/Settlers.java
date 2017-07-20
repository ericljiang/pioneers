package me.ericjiang.settlers;

import static spark.Spark.*;

import me.ericjiang.settlers.login.LoginHandler;
import me.ericjiang.settlers.login.UnimplementedLoginHandler;

public class Settlers {
    
    public static void main(String[] args) {
        // assign port
        port(getPort());

        // dependencies
        LoginHandler loginHandler = new UnimplementedLoginHandler();

        // routes
        get("/login",       (req, res) -> loginHandler.renderLoginPage());
        post("/login",      (req, res) -> loginHandler.authenticate(req, res));
        get("/lobby",       (req, res) -> "Not yet implemented.");
        get("/game",        (req, res) -> "Not yet implemented.");
    }

    static int getPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 4567; //return default port if heroku-port isn't set (i.e. on localhost)
    }
}