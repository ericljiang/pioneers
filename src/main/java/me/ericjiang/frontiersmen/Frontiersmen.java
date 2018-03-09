package me.ericjiang.frontiersmen;

import static spark.Spark.*;

import com.google.gson.Gson;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import me.ericjiang.frontiersmen.library.auth.Authenticator;
// import me.ericjiang.frontiersmen.library.auth.GoogleAuthenticator;
import me.ericjiang.frontiersmen.library.auth.MockAuthenticator;
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
        // Authenticator authenticator = new GoogleAuthenticator();
        Authenticator authenticator = new MockAuthenticator();
        LobbyWebSocketHandler lobbyWebSocketHandler = new LobbyWebSocketHandler(lobby, authenticator, playerRepository);
        GameWebSocketRouter gameWebSocketRouter = new GameWebSocketRouter(lobby, authenticator, playerRepository);

        log.info("+-----------------+");
        log.info("| Starting server |");
        log.info("+-----------------+");

        new Frontiersmen(port, gson, lobbyWebSocketHandler, gameWebSocketRouter);
    }

    private static Connection getDatabaseConnection() throws SQLException {
        String dbUrl = System.getenv("JDBC_DATABASE_URL");
        dbUrl = (dbUrl != null) ? dbUrl : "jdbc:postgresql:frontiersmen";
        Connection connection = DriverManager.getConnection(dbUrl);
        log.info("Connected to database at " + dbUrl);
        return connection;
    }

    private static int getPort() {
        String port = System.getenv("PORT");
        // return default port if heroku-port isn't set (i.e. on localhost)
        return port != null ? Integer.parseInt(port) : 4567;
    }
}
