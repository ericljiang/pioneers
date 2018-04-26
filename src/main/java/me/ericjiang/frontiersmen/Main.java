package me.ericjiang.frontiersmen;

import static spark.Spark.*;

import com.google.gson.Gson;

import lombok.extern.slf4j.Slf4j;
import me.ericjiang.frontiersmen.config.DaggerFrontiersmen;
import me.ericjiang.frontiersmen.config.Frontiersmen;
import me.ericjiang.frontiersmen.library.auth.Authenticator;
import me.ericjiang.frontiersmen.library.auth.Ticket;

@Slf4j
public class Main {
    public static void main(String[] args) {
        Frontiersmen frontiersmen = DaggerFrontiersmen.create();

        port(frontiersmen.port());
        staticFiles.location("/public");

        webSocket("/ws/lobby", frontiersmen.lobbyWebSocketHandler());
        webSocket("/ws/pregame", frontiersmen.pregameWebSocketHandler());
        webSocket("/ws/game", frontiersmen.gameWebSocketHandler());

        get("/ping", (req, res) -> "pong");
        get("/api/auth-ticket", "application/json", (req, res) -> {
            String playerId = req.queryParams("playerId");
            String idToken = req.queryParams("idToken");
            Authenticator authenticator = frontiersmen.authenticator();
            Ticket ticket = authenticator.getTicket(playerId, idToken);
            log.info("Sending auth ticket to player {}", playerId);
            return ticket;
        }, new Gson()::toJson);

        after("/api/auth-ticket", (req, res) -> {
            res.header("Access-Control-Allow-Origin", "*");
            res.header("Access-Control-Allow-Methods", "GET");
        });
    }
}
