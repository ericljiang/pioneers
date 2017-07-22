package me.ericjiang.settlers.spark;

import static spark.Spark.*;

import lombok.AllArgsConstructor;
import me.ericjiang.settlers.core.Lobby;
import me.ericjiang.settlers.spark.auth.Authenticator;
import me.ericjiang.settlers.spark.auth.GoogleAuthenticator;
import spark.Response;
import spark.Request;

@AllArgsConstructor
public class Settlers {

    private Authenticator authenticator;

    private Lobby lobby;

    private HtmlRenderer renderer;

    public static void main(String[] args) {
        Settlers app = new Settlers(
                new GoogleAuthenticator(),
                new Lobby(),
                new HtmlRenderer());
        app.start();
    }

    public void start() {
        port(getPort());
        staticFileLocation("/public");

        // filters
        before("/lobby",    this::redirectUnauthenticated);
        before("/game",     this::redirectUnauthenticated);

        // routes
        get("/login",       authenticator::renderLoginPage);
        post("/login",      authenticator::login);
        get("/lobby",       (req, res) -> renderer.renderLobby(req, lobby));
        get("/game",        (req, res) -> "Game: Not yet implemented.");

        redirect.get("/", "/lobby");
    }

    private int getPort() {
        ProcessBuilder processBuilder = new ProcessBuilder();
        if (processBuilder.environment().get("PORT") != null) {
            return Integer.parseInt(processBuilder.environment().get("PORT"));
        }
        return 4567; //return default port if heroku-port isn't set (i.e. on localhost)
    }

    /**
     * Check if user is signed in and redirect to login page if not.
     */
    private void redirectUnauthenticated(Request request, Response response) {
        if (!authenticator.sessionIsAuthenticated(request)) {
            response.redirect("/login");
        }
    }
}