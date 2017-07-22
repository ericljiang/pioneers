package me.ericjiang.settlers;

import static spark.Spark.*;

import me.ericjiang.settlers.auth.Authenticator;
import me.ericjiang.settlers.auth.GoogleAuthenticator;

public class Settlers {
    
    public static void main(String[] args) {
        port(getPort());
        staticFileLocation("/public");

        // dependencies
        Authenticator authenticator = new GoogleAuthenticator();

        // filters
        before("/lobby", (req, res) -> {
            if (!authenticator.sessionIsAuthenticated(req, res)) {
                res.redirect("/login");
            }
        });

        before("/game", (req, res) -> {
            if (!authenticator.sessionIsAuthenticated(req, res)) {
                res.redirect("/login");
            }
        });

        // routes
        redirect.get("/", "/login");
        get("/login",   authenticator::renderLoginPage);
        post("/login",  authenticator::login);
        get("/lobby",   (req, res) -> "Lobby: Not yet implemented.");
        get("/game",    (req, res) -> "Game: Not yet implemented.");
    }

    private static int getPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 4567; //return default port if heroku-port isn't set (i.e. on localhost)
    }
}