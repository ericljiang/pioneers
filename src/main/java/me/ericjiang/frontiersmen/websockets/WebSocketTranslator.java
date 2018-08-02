package me.ericjiang.frontiersmen.websockets;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.Map;

import me.ericjiang.frontiersmen.library.Event;
import me.ericjiang.frontiersmen.library.MultiplayerModuleEventRouter;
import me.ericjiang.frontiersmen.library.PlayerEvent;
import me.ericjiang.frontiersmen.library.player.PingEvent;
import me.ericjiang.frontiersmen.library.player.PlayerAuthenticationEvent;
import me.ericjiang.frontiersmen.library.player.PlayerConnection;
import me.ericjiang.frontiersmen.library.player.PlayerRepository;
import me.ericjiang.frontiersmen.library.player.PongEvent;
import me.ericjiang.frontiersmen.serialization.PlayerTypeAdapterFactory;
import me.ericjiang.frontiersmen.serialization.RuntimeTypeAdapterFactory;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.StatusCode;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

@Slf4j
@WebSocket
public class WebSocketTranslator {

    private static final long TIMEOUT_IN_MS = 12 * 1000;

    private final MultiplayerModuleEventRouter eventRouter;

    private final Gson gson;

    private final Map<Session, PlayerConnection> connections;

    public WebSocketTranslator(MultiplayerModuleEventRouter eventRouter, PlayerRepository playerRepository) {
        this.eventRouter = eventRouter;

        RuntimeTypeAdapterFactory<Event> eventTypeAdapterFactory = RuntimeTypeAdapterFactory.of(Event.class, "eventType");
        eventRouter.getEventTypes().forEach(t -> eventTypeAdapterFactory.registerSubtype(t));
        eventTypeAdapterFactory.registerSubtype(PingEvent.class);
        eventTypeAdapterFactory.registerSubtype(PongEvent.class);
        eventTypeAdapterFactory.registerSubtype(PlayerAuthenticationEvent.class);
        PlayerTypeAdapterFactory playerTypeAdapterFactory = new PlayerTypeAdapterFactory(playerRepository);

        this.gson = new GsonBuilder()
                .registerTypeAdapterFactory(eventTypeAdapterFactory)
                .registerTypeAdapterFactory(playerTypeAdapterFactory)
                .create();

        this.connections = Maps.newConcurrentMap();
    }

    @OnWebSocketConnect
    public void onConnect(Session session) throws IOException {
        session.setIdleTimeout(TIMEOUT_IN_MS);
        try {
            PlayerConnection connection = new WebSocketPlayerConnection(session, gson);
            connections.put(session, connection);
            eventRouter.acceptConnection(connection);
        } catch (GeneralSecurityException e) {
            log.error("Rejected WebSocket connection request", e);
            session.close(ErrorCode.AUTHENTICATION_ERROR, e.getMessage());
        } catch (IllegalArgumentException e) {
            log.error("Rejected WebSocket connection request", e);
            session.close(StatusCode.POLICY_VIOLATION, e.getMessage());
        } catch (RuntimeException e) {
            log.error("Error receiving client connection", e);
            session.close(StatusCode.SERVER_ERROR, e.toString());
        }
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        log.debug(String.format("Received message from %s", session.getUpgradeRequest().getRequestURI()));
        try {
            PlayerConnection connection = connections.get(session);
            PlayerEvent event = (PlayerEvent) gson.fromJson(message, Event.class);
            String playerId = connection.getPlayerId();
            event.setPlayerId(playerId);
            eventRouter.receiveEvent(connection, event);
        } catch (ClassCastException e) {
            log.error("Client sent an Event that isn't a PlayerEvent", e);
        } catch (IllegalArgumentException e) {
            log.error("Invalid WebSocket request", e);
        } catch (RuntimeException e) {
            log.error("Error processing client message", e);
        } catch (GeneralSecurityException e) {
            log.error("Unauthorized", e);
            PlayerConnection connection = connections.remove(session);
            eventRouter.removeConnection(connection, "Unauthorized");
            session.close(ErrorCode.AUTHENTICATION_ERROR, "Unauthorized");
        }
    }

    @OnWebSocketClose
    public void onClose(Session session, int statusCode, String reason) {
        try {
            PlayerConnection connection = connections.remove(session);
            eventRouter.removeConnection(connection, reason);
        } catch (IllegalArgumentException e) {
            log.error("Invalid WebSocket request", e);
            session.close(StatusCode.POLICY_VIOLATION, e.getMessage());
        } catch (RuntimeException e) {
            log.error("Error closing client connection", e);
            session.close();
        }
    }

    private static class ErrorCode {
        public final static int AUTHENTICATION_ERROR = 4000;
    }
}
