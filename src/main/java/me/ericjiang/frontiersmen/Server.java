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
public class Server {

    protected static final String LOBBY_WEBSOCKET_PATH = "/ws/lobby";
    protected static final String PREGAME_WEBSOCKET_PATH = "/ws/pregame";
    protected static final String GAME_WEBSOCKET_PATH = "/ws/game";

    protected static final String PING_PATH = "/ping";

    protected static final String AUTH_TICKET_PATH = "/api/auth-ticket";

    private final int port;

    private final WebSocketTranslator lobbyWebSocketHandler;

    private final WebSocketTranslator pregameWebSocketHandler;

    private final WebSocketTranslator gameWebSocketHandler;

    private final Authenticator authenticator;

    public static void main(String[] args) {
        final Frontiersmen frontiersmen = DaggerFrontiersmen.create();
        final Server server = new Server(frontiersmen.port(),
                frontiersmen.lobbyWebSocketHandler(),
                frontiersmen.pregameWebSocketHandler(),
                frontiersmen.gameWebSocketHandler(),
                frontiersmen.authenticator());
        server.initialize();
    }

    public void initialize() {
        port(port);
        staticFiles.location("/public");

        webSocket(LOBBY_WEBSOCKET_PATH, lobbyWebSocketHandler);
        webSocket(PREGAME_WEBSOCKET_PATH, pregameWebSocketHandler);
        webSocket(GAME_WEBSOCKET_PATH, gameWebSocketHandler);

        get(PING_PATH, (req, res) -> "pong");
        get(AUTH_TICKET_PATH, "application/json", (req, res) -> {
            return getAuthTicket(req.queryParams("playerId"),
                    req.queryParams("idToken"));
        }, new Gson()::toJson);

        after(AUTH_TICKET_PATH, (req, res) -> {
            res.header("Access-Control-Allow-Origin", "*");
            res.header("Access-Control-Allow-Methods", "GET");
        });
    }

    private Ticket getAuthTicket(String playerId, String idToken) {
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
