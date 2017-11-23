package me.ericjiang.settlers;

import static spark.Spark.*;

import com.google.gson.Gson;
import java.io.IOException;
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
        get("/lobby", (req, res) -> client);
        get("/game", (req, res) -> client);
        get("/message", (req, res) -> client);
    }

    public static void main(String[] args) throws IOException {
        Gson gson = new Gson();
        String client = IOUtils.toString(Spark.class.getResourceAsStream("/public/index.html"));
        GameFactory<SimpleGame> gameFactory = new SimpleGameFactory();
        GameDao<SimpleGame> gameDao = new SimpleGameDao();
        Lobby<SimpleGame> lobby = new Lobby<SimpleGame>(gameFactory, gameDao);
        LobbyWebSocketHandler lobbyWebSocketHandler = new LobbyWebSocketHandler(lobby);
        GameWebSocketRouter gameWebSocketRouter = new GameWebSocketRouter(lobby);

        new Settlers(gson, client, lobbyWebSocketHandler, gameWebSocketRouter);
    }
}
