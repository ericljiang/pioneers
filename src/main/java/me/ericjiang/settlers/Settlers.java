package me.ericjiang.settlers;

import static spark.Spark.*;

import com.google.gson.Gson;

import me.ericjiang.settlers.library.game.GameDao;
import me.ericjiang.settlers.library.game.GameFactory;
import me.ericjiang.settlers.library.game.GameWebSocketRouter;
import me.ericjiang.settlers.library.lobby.Lobby;
import me.ericjiang.settlers.library.lobby.LobbyWebSocketHandler;
import me.ericjiang.settlers.simple.SimpleGame;
import me.ericjiang.settlers.simple.SimpleGameDao;
import me.ericjiang.settlers.simple.SimpleGameFactory;

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

    public static void main(String[] args) {
        Gson gson = new Gson();
        GameFactory<SimpleGame> gameFactory = new SimpleGameFactory();
        GameDao<SimpleGame> gameDao = new SimpleGameDao();
        Lobby<SimpleGame> lobby = new Lobby<SimpleGame>(gameFactory, gameDao);
        LobbyWebSocketHandler lobbyWebSocketHandler = new LobbyWebSocketHandler(lobby);
        GameWebSocketRouter gameWebSocketRouter = new GameWebSocketRouter(lobby);

        new Settlers(gson, lobbyWebSocketHandler, gameWebSocketRouter);
    }
}
