package me.ericjiang.frontiersmen;

import static spark.Spark.*;

import com.google.gson.Gson;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Optional;
import me.ericjiang.frontiersmen.library.auth.Authenticator;
import me.ericjiang.frontiersmen.library.auth.GoogleAuthenticator;
import me.ericjiang.frontiersmen.library.game.GameDao;
import me.ericjiang.frontiersmen.library.game.GameDaoPostgres;
import me.ericjiang.frontiersmen.library.game.GameFactory;
import me.ericjiang.frontiersmen.library.game.GameWebSocketRouter;
import me.ericjiang.frontiersmen.library.lobby.Lobby;
import me.ericjiang.frontiersmen.library.lobby.LobbyWebSocketHandler;
import me.ericjiang.frontiersmen.library.player.InMemoryPlayerRepository;
import me.ericjiang.frontiersmen.library.player.PlayerRepository;
import me.ericjiang.frontiersmen.simple.SimpleGame;
import me.ericjiang.frontiersmen.simple.SimpleGameFactory;

@Slf4j
public class Frontiersmen {

    private static final String HEROKU_PORT_VARIABLE = "PORT";

    private static final String HEROKU_DATABASE_URL_VARIABLE = "JDBC_DATABASE_URL";

    private static final int DEFAULT_PORT = 4567;

    private static final String DEFAULT_DATABASE_URL = "jdbc:postgresql:frontiersmen";

    public Frontiersmen(
            int port,
            Gson gson,
            LobbyWebSocketHandler lobbyWebSocketHandler,
            GameWebSocketRouter gameWebSocketRouter) {

        port(port);

        staticFiles.location("/public");

        webSocket("/ws/lobby", lobbyWebSocketHandler);
        webSocket("/ws/game", gameWebSocketRouter);

        get("/api/hello", (req, res) -> new Greeting(), gson::toJson);
        get("/ping", (req, res) -> "pong");
    }

    public static void main(String[] args) throws IOException, SQLException {
        int port = getPort();
        Gson gson = new Gson();
        Connection connection = getDatabaseConnection();
        GameDao gameDao = new GameDaoPostgres(SimpleGame.class, connection);
        GameFactory gameFactory = new SimpleGameFactory(gameDao);
        PlayerRepository playerRepository = new InMemoryPlayerRepository();
        Lobby lobby = new Lobby(gameFactory, playerRepository);
        Authenticator authenticator = new GoogleAuthenticator();
        LobbyWebSocketHandler lobbyWebSocketHandler = new LobbyWebSocketHandler(lobby, authenticator, playerRepository);
        GameWebSocketRouter gameWebSocketRouter = new GameWebSocketRouter(lobby, authenticator, playerRepository);

        log.info("+-----------------+");
        log.info("| Starting server |");
        log.info("+-----------------+");

        new Frontiersmen(port, gson, lobbyWebSocketHandler, gameWebSocketRouter);
    }

    /**
     * Return default port if Heroku port isn't set (i.e. on localhost)
     */
    private static int getPort() {
        return Optional.ofNullable(System.getenv(HEROKU_PORT_VARIABLE))
                .map(s -> Integer.parseInt(s))
                .orElse(DEFAULT_PORT);
    }

    private static Connection getDatabaseConnection() throws SQLException {
        String databaseUrl = Optional.ofNullable(System.getenv(HEROKU_DATABASE_URL_VARIABLE))
                .orElse(DEFAULT_DATABASE_URL);
        Connection connection = DriverManager.getConnection(databaseUrl);
        log.info("Connected to database at " + databaseUrl);
        return connection;
    }
}
