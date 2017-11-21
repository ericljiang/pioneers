package me.ericjiang.settlers;

import static spark.Spark.*;

import com.google.gson.Gson;
import me.ericjiang.settlers.library.GameFactory;
import me.ericjiang.settlers.library.data.GameDao;
import me.ericjiang.settlers.library.data.GameDaoPostgres;
import me.ericjiang.settlers.library.lobby.Lobby;
import me.ericjiang.settlers.library.lobby.LobbyWebSocketHandler;
import me.ericjiang.settlers.library.websockets.GameWebSocketRouter;
import me.ericjiang.settlers.simple.SimpleGame;
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
        GameDao gameDao = new GameDaoPostgres(SimpleGame.class);
        GameFactory gameFactory = new SimpleGameFactory(gameDao);
        Lobby lobby = new Lobby(gameFactory, gameDao);
        LobbyWebSocketHandler lobbyWebSocketHandler = new LobbyWebSocketHandler(lobby);
        GameWebSocketRouter gameWebSocketRouter = new GameWebSocketRouter(lobby);

        new Settlers(gson, lobbyWebSocketHandler, gameWebSocketRouter);
    }
}
