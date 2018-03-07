package me.ericjiang.settlers;

import static spark.Spark.*;

import com.google.gson.Gson;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

import me.ericjiang.settlers.library.auth.Authenticator;
// import me.ericjiang.settlers.library.auth.GoogleAuthenticator;
import me.ericjiang.settlers.library.auth.MockAuthenticator;
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

@Slf4j
public class Settlers {

    public Settlers(
            Gson gson,
            LobbyWebSocketHandler lobbyWebSocketHandler,
            GameWebSocketRouter gameWebSocketRouter) {

        staticFiles.location("/public");

        webSocket("/ws/lobby", lobbyWebSocketHandler);
        webSocket("/ws/game", gameWebSocketRouter);

        get("/api/hello", (req, res) -> new Greeting(), gson::toJson);
    }

    public static void main(String[] args) throws IOException, SQLException {
        Gson gson = new Gson();
        Connection connection = DriverManager.getConnection("jdbc:postgresql:settlers");
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

        new Settlers(gson, lobbyWebSocketHandler, gameWebSocketRouter);
    }
}
