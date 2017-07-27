package me.ericjiang.settlers.spark;

import static spark.Spark.*;

import java.util.HashMap;
import java.util.Map;

import lombok.AllArgsConstructor;

import me.ericjiang.settlers.core.Lobby;
import me.ericjiang.settlers.spark.auth.Authenticator;
import me.ericjiang.settlers.spark.auth.GoogleAuthenticator;

import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.TemplateEngine;
import spark.template.thymeleaf.ThymeleafTemplateEngine;

@AllArgsConstructor
public class Settlers {

    private Authenticator authenticator;

    private Lobby lobby;

    private TemplateEngine templateEngine;

    public static void main(String[] args) {
        Settlers app = new Settlers(
                new GoogleAuthenticator(),
                new Lobby(),
                new ThymeleafTemplateEngine());
        app.start();
    }

    public void start() {
        port(getPort());
        staticFileLocation("/public");

        // filters
        before((req, res) -> {
            String url = req.url();
            if (!url.contains("localhost") && url.startsWith("http://")) {
                res.redirect("https://" + url.split("http://")[1]);
            }
        });

        before("/lobby",        this::redirectUnauthenticated);
        before("/game",         this::redirectUnauthenticated);

        // routes
        get("/sign-in",         authenticator::renderSignInPage);
        post("/sign-in",        authenticator::signIn);
        post("/sign-out",       authenticator::signOut);
        get("/lobby",           (req, res) -> {
                                    Map<String, Object> model = new HashMap<String, Object>();
                                    model.put("userId", req.session().attribute(Authenticator.USER_ID));
                                    model.put("lobby", lobby);
                                    return templateEngine.render(new ModelAndView(model, "lobby"));
                                });
        post("/create-game",    (req, res) -> {
                                    String name = req.queryParams("name");
                                    int maxPlayers = Integer.parseInt(req.queryParams("maxPlayers"));
                                    lobby.createGame(name, maxPlayers);
                                    res.redirect("/lobby", 303);
                                    return "303 See Other";
                                });
        get("/game",            (req, res) -> halt(501, "Game view not implemented"));

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
     * Check if user is signed in and redirect to sign-in page if not.
     */
    private void redirectUnauthenticated(Request request, Response response) {
        if (!authenticator.sessionIsAuthenticated(request)) {
            response.redirect("/sign-in");
        }
    }
}
