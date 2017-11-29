package me.ericjiang.settlers;

import static spark.Spark.*;

import com.google.gson.Gson;
import java.io.IOException;
import java.util.Arrays;
import me.ericjiang.settlers.library.game.GameDao;
import me.ericjiang.settlers.library.game.GameFactory;
import me.ericjiang.settlers.library.game.GameWebSocketRouter;
import me.ericjiang.settlers.library.lobby.Lobby;
import me.ericjiang.settlers.library.lobby.LobbyWebSocketHandler;
import me.ericjiang.settlers.simple.SimpleGame;
import me.ericjiang.settlers.simple.SimpleGameDao;
import me.ericjiang.settlers.simple.SimpleGameFactory;
import spark.Spark;
import spark.utils.IOUtils;

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

    public static void main(String[] args) throws IOException {
        Gson gson = new Gson();
        String client = IOUtils.toString(Spark.class.getResourceAsStream("/public/index.html"));
        GameDao<SimpleGame> gameDao = new SimpleGameDao();
        GameFactory<SimpleGame> gameFactory = new SimpleGameFactory(gameDao);
        Lobby<SimpleGame> lobby = new Lobby<SimpleGame>(gameFactory);
        LobbyWebSocketHandler lobbyWebSocketHandler = new LobbyWebSocketHandler(lobby);
        GameWebSocketRouter gameWebSocketRouter = new GameWebSocketRouter(lobby);

        new Settlers(gson, client, lobbyWebSocketHandler, gameWebSocketRouter);
    }
}
