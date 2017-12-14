package me.ericjiang.settlers;

import static spark.Spark.*;

import com.google.gson.Gson;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Arrays;

import me.ericjiang.settlers.library.auth.Authenticator;
import me.ericjiang.settlers.library.auth.GoogleAuthenticator;
import me.ericjiang.settlers.library.game.GameDao;
import me.ericjiang.settlers.library.game.GameDaoPostgres;
import me.ericjiang.settlers.library.game.GameFactory;
import me.ericjiang.settlers.library.game.GameWebSocketRouter;
import me.ericjiang.settlers.library.lobby.Lobby;
import me.ericjiang.settlers.library.lobby.LobbyWebSocketHandler;
import me.ericjiang.settlers.library.player.InMemoryPlayerRepository;
import me.ericjiang.settlers.library.player.PlayerRepository;
import me.ericjiang.settlers.simple.SimpleGame;
import me.ericjiang.settlers.simple.SimpleGameFactory;
import spark.Spark;
import spark.utils.IOUtils;

@Slf4j
public class Settlers {

    public Settlers(
            Gson gson,
            String client,
            LobbyWebSocketHandler lobbyWebSocketHandler,
            GameWebSocketRouter gameWebSocketRouter) {

        staticFiles.location("/public");

        webSocket("/ws/lobby", lobbyWebSocketHandler);
        webSocket("/ws/game", gameWebSocketRouter);

        get("/api/hello", (req, res) -> new Greeting(), gson::toJson);

        // Routes in react-router
        String[] reactRoutes = { "/lobby", "/game/*", "/message" };
        Arrays.stream(reactRoutes).forEach(path -> {
            get(path, (req, res) -> client);
        });
    }

    public static void main(String[] args) throws IOException, SQLException {
        Gson gson = new Gson();
        String client = IOUtils.toString(Spark.class.getResourceAsStream("/public/index.html"));
        Connection connection = DriverManager.getConnection("jdbc:postgresql:settlers");
        GameDao<SimpleGame> gameDao = new GameDaoPostgres<SimpleGame>(SimpleGame.class, connection);
        GameFactory<SimpleGame> gameFactory = new SimpleGameFactory(gameDao);
        Lobby<SimpleGame> lobby = new Lobby<SimpleGame>(gameFactory);
        Authenticator authenticator = new GoogleAuthenticator();
        PlayerRepository playerRepository = new InMemoryPlayerRepository();
        LobbyWebSocketHandler lobbyWebSocketHandler = new LobbyWebSocketHandler(lobby, authenticator, playerRepository);
        GameWebSocketRouter gameWebSocketRouter = new GameWebSocketRouter(lobby, authenticator, playerRepository);

        log.info("+-----------------+");
        log.info("| Starting server |");
        log.info("+-----------------+");

        new Settlers(gson, client, lobbyWebSocketHandler, gameWebSocketRouter);
    }
}
