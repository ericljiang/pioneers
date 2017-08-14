package me.ericjiang.settlers.spark;

import static spark.Spark.*;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.ericjiang.settlers.core.game.Game;
import me.ericjiang.settlers.data.board.BoardDao;
import me.ericjiang.settlers.data.board.BoardDaoInMemory;
import me.ericjiang.settlers.data.game.GameDao;
import me.ericjiang.settlers.data.game.GameDaoPostgres;
import me.ericjiang.settlers.data.player.PlayerDao;
import me.ericjiang.settlers.data.player.PlayerDaoPostgres;
import me.ericjiang.settlers.spark.auth.Authenticator;
import me.ericjiang.settlers.spark.auth.GoogleAuthenticator;
import spark.ModelAndView;
import spark.Request;
import spark.Response;
import spark.TemplateEngine;
import spark.template.thymeleaf.ThymeleafTemplateEngine;

@Slf4j
@AllArgsConstructor
public class Settlers {

    private Authenticator authenticator;

    private PlayerDao playerDao;

    private GameDao gameDao;

    private WebSocketHandler webSocketHandler;

    private TemplateEngine templateEngine;

    public static void main(String[] args) throws SQLException {
        Authenticator authenticator = new GoogleAuthenticator();
        Connection connection = getDatabaseConnection();
        BoardDao boardDao = new BoardDaoInMemory();
        PlayerDao playerDao = new PlayerDaoPostgres(connection);
        GameDao gameDao = new GameDaoPostgres(connection, boardDao, playerDao);
        WebSocketHandler webSocketHandler = new WebSocketHandler(gameDao);
        TemplateEngine templateEngine = new ThymeleafTemplateEngine();

        new Settlers(authenticator, playerDao, gameDao, webSocketHandler, templateEngine).start();
    }

    public static Connection getDatabaseConnection() throws SQLException {
        String dbUrl = System.getenv("JDBC_DATABASE_URL");
        dbUrl = (dbUrl != null) ? dbUrl : "jdbc:postgresql:settlers";
        Connection connection = DriverManager.getConnection(dbUrl);
        log.info("Connected to database at " + dbUrl);
        return connection;
    }

    public void start() {
        port(getPort());
        staticFileLocation("/public");

        webSocket("/game", webSocketHandler);

        // filters
        before((req, res) -> {
            String url = req.url();
            if (!url.contains("localhost") && url.startsWith("http://")) {
                res.redirect("https://" + url.split("http://")[1]);
            }
        });

        before("/lobby", this::redirectUnauthenticated);
        before("/play", this::redirectUnauthenticated);

        // routes
        get("/sign-in", authenticator::renderSignInPage);
        post("/sign-in", authenticator::signIn);
        post("/sign-out", authenticator::signOut);
        get("/lobby", (req, res) -> {
            String userId = req.session().attribute(Authenticator.USER_ID);
            String nickname = playerDao.getName(userId);
            if (nickname == null) {
                String firstName = req.session().attribute(Authenticator.NAME);
                playerDao.setName(userId, firstName);
            }
            Map<String, Object> model = new HashMap<String, Object>();
            model.put("userId", userId);
            model.put("nickname", nickname);
            model.put("gameDao", gameDao);
            return templateEngine.render(new ModelAndView(model, "lobby"));
        });
        post("/create-game", (req, res) -> {
            String name = req.queryParams("name");
            String expansion = req.queryParams("expansion");
            gameDao.createGame(name, expansion);
            res.redirect("/lobby", 303);
            return "303 See Other";
        });
        get("/play", (req, res) -> {
            String gameId = req.queryParams("g");
            String playerId = req.queryParams("u");
            Game game = gameDao.getGame(gameId);
            Map<String, Object> model = new HashMap<String, Object>();
            model.put("gameName", game.getName());
            model.put("gameId", gameId);
            model.put("expansion", game.getExpansion());
            model.put("maxPlayers", game.getMaxPlayers());
            model.put("minPlayers", game.getMinPlayers());
            model.put("playerId", playerId);
            model.put("players", game.connectedPlayers().stream()
                    .map(p -> playerDao.getName(p))
                    .collect(Collectors.toList()));
            log.info("Rendering template play.html");
            return templateEngine.render(new ModelAndView(model, "play"));
        });

        redirect.get("/", "/lobby");
    }

    private int getPort() {
        String port = System.getenv("PORT");
        // return default port if heroku-port isn't set (i.e. on localhost)
        return port != null ? Integer.parseInt(port) : 4567;
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
