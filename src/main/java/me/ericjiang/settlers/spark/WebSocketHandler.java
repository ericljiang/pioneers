package me.ericjiang.settlers.spark;

import static spark.Spark.halt;

import java.io.IOException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.ericjiang.settlers.core.actions.Action;
import me.ericjiang.settlers.core.actions.PlayerAction;
import me.ericjiang.settlers.core.game.Game;
import me.ericjiang.settlers.core.game.GameDao;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;

@AllArgsConstructor
@WebSocket
@Slf4j
public class WebSocketHandler {

    private GameDao lobby;

    @OnWebSocketConnect
    public void onConnect(Session session) {
        log.info("Session connected to websocket.");
        WebSocketPlayer player = new WebSocketPlayer(session);
        String gameId = getGameId(session);
        Game game = lobby.getGame(gameId);
        if (!game.connectPlayer(player)) {
            halt(403);
        }
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        if (message.equals("")) { // ping
            session.getRemote().sendString(""); // pong
            log.debug("Received heartbeat from client.");
            return;
        }
        processMessage(session, message);
        // TODO: if an exception occurs, swallow but notify client
    }

    @OnWebSocketClose
    public void onClose(Session session, int statusCode, String reason) {
        log.debug("Session disconnected from websocket: " + statusCode + ":" + reason);
        WebSocketPlayer player = new WebSocketPlayer(session);
        String gameId = getGameId(session);
        Game game = lobby.getGame(gameId);
        game.disconnectPlayer(player);
    }

    private void processMessage(Session session, String message) throws IOException {
        try {
            PlayerAction action = (PlayerAction) Action.valueOf(message);
            action.setPlayerId(getUserId(session));
            Game game = lobby.getGame(getGameId(session));
            action.accept(game);
        } catch (ClassCastException e) {
            throw new InternalError("Server received an Action that wasn't a PlayerAction.", e);
        }
    }

    private String getGameId(Session session) {
        return getParameter(session, "g");
    }

    private String getUserId(Session session) {
        return getParameter(session, "u");
    }

    private String getParameter(Session session, String key) {
        return session.getUpgradeRequest().getParameterMap().get(key).get(0);
    }
}