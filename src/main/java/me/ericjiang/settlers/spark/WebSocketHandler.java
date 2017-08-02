package me.ericjiang.settlers.spark;

import java.io.IOException;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.ericjiang.settlers.core.Game;
import me.ericjiang.settlers.core.Lobby;
import me.ericjiang.settlers.core.actions.Action;
import me.ericjiang.settlers.core.actions.PlayerAction;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;

@AllArgsConstructor
@WebSocket
@Slf4j
public class WebSocketHandler {

    private Lobby lobby;

    @OnWebSocketConnect
    public void onConnect(Session session) {
        log.info("Session connected to websocket.");
        WebSocketPlayer player = new WebSocketPlayer(session);
        String gameId = getGameId(session);
        Game game = lobby.getGame(gameId);
        game.connectPlayer(player);
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        if (message.equals("")) { // ping
            session.getRemote().sendString(""); // pong
            log.debug("Received heartbeat from client.");
            return;
        }
        processMessage(session, message);
    }

    @OnWebSocketClose
    public void onClose(Session session, int statusCode, String reason) {
        log.info("Session disconnected from websocket.");
    }

    private void processMessage(Session session, String message) throws IOException {
        try {
            PlayerAction action = (PlayerAction) Action.valueOf(message);
            action.setId(UUID.randomUUID().toString());
            action.setPlayerId(getUserId(session));
            action.setGameId(getGameId(session));
            Game game = lobby.getGame(action.getGameId());
            game.processAction(action);
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