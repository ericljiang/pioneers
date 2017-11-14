package me.ericjiang.settlers;

import static spark.Spark.*;

import com.google.gson.Gson;
import me.ericjiang.settlers.library.Lobby;
import me.ericjiang.settlers.library.websockets.GameWebSocketRouter;
import me.ericjiang.settlers.library.websockets.LobbyWebSocketHandler;

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
        Lobby lobby = new Lobby();
        LobbyWebSocketHandler lobbyWebSocketHandler = new LobbyWebSocketHandler(lobby);
        GameWebSocketRouter gameWebSocketRouter = new GameWebSocketRouter(lobby);

        new Settlers(gson, lobbyWebSocketHandler, gameWebSocketRouter);
    }
}
