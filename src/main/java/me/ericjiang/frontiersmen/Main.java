package me.ericjiang.frontiersmen;

import static spark.Spark.*;

import me.ericjiang.frontiersmen.config.DaggerFrontiersmen;
import me.ericjiang.frontiersmen.config.Frontiersmen;

public class Main {
    public static void main(String[] args) {
        Frontiersmen frontiersmen = DaggerFrontiersmen.create();

        port(frontiersmen.port());
        staticFiles.location("/public");

        webSocket("/ws/lobby", frontiersmen.lobbyWebSocketHandler());
        webSocket("/ws/game", frontiersmen.gameWebSocketRouter());
        get("/api/hello", (req, res) -> new Greeting(), frontiersmen.gson()::toJson);
        get("/ping", (req, res) -> "pong");
    }
}
