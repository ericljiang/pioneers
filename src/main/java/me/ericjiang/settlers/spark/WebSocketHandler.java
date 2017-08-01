package me.ericjiang.settlers.spark;

import java.io.IOException;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import me.ericjiang.settlers.core.Game;
import me.ericjiang.settlers.core.Lobby;
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
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        if (message.equals("")) { // ping
            session.getRemote().sendString(""); // pong
            log.info("Received heartbeat from client.");
            return;
        }
        processMessage(session, message);
    }

    @OnWebSocketClose
    public void onClose(Session session, int statusCode, String reason) {
        log.info("Session disconnected from websocket.");
    }

    private void processMessage(Session session, String message) throws IOException {
        //session.getRemote().sendString(message);
        PlayerAction action = parseMessage(message);
        Game game = lobby.getGame(action.getGameId());
        game.processAction(action);
    }

    private PlayerAction parseMessage(String message) {
        // TODO gson
        return null;
    }
}