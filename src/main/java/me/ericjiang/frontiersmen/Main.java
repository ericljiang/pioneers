package me.ericjiang.frontiersmen;

import static spark.Spark.*;

import java.security.GeneralSecurityException;

import com.google.gson.Gson;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.ericjiang.frontiersmen.config.DaggerFrontiersmen;
import me.ericjiang.frontiersmen.config.Frontiersmen;
import me.ericjiang.frontiersmen.library.auth.Authenticator;
import me.ericjiang.frontiersmen.library.auth.Ticket;
import me.ericjiang.frontiersmen.websockets.WebSocketTranslator;

@Slf4j
@RequiredArgsConstructor
public class Main {

    private final int port;

    private final WebSocketTranslator lobbyWebSocketHandler;

    private final WebSocketTranslator pregameWebSocketHandler;

    private final WebSocketTranslator gameWebSocketHandler;

    private final Authenticator authenticator;

    public static void main(String[] args) {
        final Frontiersmen frontiersmen = DaggerFrontiersmen.create();
        final Main main = new Main(frontiersmen.port(),
                frontiersmen.lobbyWebSocketHandler(),
                frontiersmen.pregameWebSocketHandler(),
                frontiersmen.gameWebSocketHandler(),
                frontiersmen.authenticator());
        main.initialize();
    }

    public void initialize() {
        port(port);
        staticFiles.location("/public");

        webSocket("/ws/lobby", lobbyWebSocketHandler);
        webSocket("/ws/pregame", pregameWebSocketHandler);
        webSocket("/ws/game", gameWebSocketHandler);

        get("/ping", (req, res) -> "pong");
        get("/api/auth-ticket", "application/json", (req, res) -> {
            return getAuthTicket(req.queryParams("playerId"),
                    req.queryParams("idToken"),
                    authenticator);
        }, new Gson()::toJson);

        after("/api/auth-ticket", (req, res) -> {
            res.header("Access-Control-Allow-Origin", "*");
            res.header("Access-Control-Allow-Methods", "GET");
        });
    }

    private Ticket getAuthTicket(String playerId, String idToken, Authenticator authenticator) {
        try {
            final Ticket ticket = authenticator.getTicket(playerId, idToken);
            log.info("Sending auth ticket to player {}", playerId);
            return ticket;
        } catch (GeneralSecurityException e) {
            log.error("Unauthorized request for auth ticket! 🚨", e);
            halt(403);
            return null;
        }
    };
}
