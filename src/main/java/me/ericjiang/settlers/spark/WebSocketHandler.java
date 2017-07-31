package me.ericjiang.settlers.spark;

import java.io.IOException;

import me.ericjiang.settlers.core.Lobby;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.*;

import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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
        // parse message
        // identify game e.g. game = lobby.getGame(uuid);
        // send message to game
        session.getRemote().sendString(message);
    }
}