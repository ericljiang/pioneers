package me.ericjiang.settlers.library;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import me.ericjiang.settlers.library.MultiplayerModule;
import me.ericjiang.settlers.library.auth.Authenticator;
import me.ericjiang.settlers.library.player.PlayerConnection;
import me.ericjiang.settlers.library.player.PlayerRepository;
import me.ericjiang.settlers.library.player.PlayerTypeAdapterFactory;
import me.ericjiang.settlers.library.player.WebSocketPlayerConnection;
import me.ericjiang.settlers.library.utility.RuntimeTypeAdapterFactory;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.StatusCode;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

@Slf4j
@WebSocket
public abstract class MultiplayerModuleWebSocketRouter {

    private final Authenticator authenticator;

    private final PlayerRepository playerRepository;

    private final Gson gson;

    private final Map<Session, PlayerConnection> connections;

    public MultiplayerModuleWebSocketRouter(Authenticator authenticator, PlayerRepository playerRepository) {
        this.authenticator = authenticator;
        this.playerRepository = playerRepository;

        RuntimeTypeAdapterFactory<Event> eventTypeAdapterFactory = RuntimeTypeAdapterFactory.of(Event.class, "eventType");
        getEventTypes().forEach(t -> eventTypeAdapterFactory.registerSubtype(t));
        PlayerTypeAdapterFactory playerTypeAdapterFactory = new PlayerTypeAdapterFactory(playerRepository);

        this.gson = new GsonBuilder()
                .registerTypeAdapterFactory(eventTypeAdapterFactory)
                .registerTypeAdapterFactory(playerTypeAdapterFactory)
                .create();

        this.connections = Maps.newConcurrentMap();
    }

    protected abstract MultiplayerModule getModule(Session session);

    /**
     * @return a list of Event type classes for this module can receive from clients
     */
    protected abstract List<Class<? extends Event>> getEventTypes();

    @OnWebSocketConnect
    public void onConnect(Session session) throws IOException {
        try {
            String playerId = getQueryParameterString(session, "playerId");
            String authToken = getQueryParameterString(session, "authToken");
            String name = authenticator.verify(playerId, authToken);
            if (!playerRepository.contains(playerId)) {
                playerRepository.setDisplayName(playerId, name);
            }
            MultiplayerModule module = getModule(session);
            PlayerConnection connection = new WebSocketPlayerConnection(session, gson);
            connections.put(session, connection);
            module.addConnection(playerId, connection);
        } catch (GeneralSecurityException | RuntimeException e) {
            session.close(StatusCode.POLICY_VIOLATION, e.getMessage());
            log.error("Rejected WebSocket connection request", e);
        }
    }

    @OnWebSocketClose
    public void onClose(Session session, int statusCode, String reason) {
        String playerId = getQueryParameterString(session, "playerId");
        MultiplayerModule module = getModule(session);
        PlayerConnection connection = connections.get(session);
        module.removeConnection(playerId, connection, reason);
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        log.debug(String.format("Received message from %s: %s", session.getUpgradeRequest().getRequestURI(), message));
        MultiplayerModule module = getModule(session);
        try {
            PlayerEvent event = (PlayerEvent) gson.fromJson(message, Event.class);
            String playerId = getQueryParameterString(session, "playerId");
            event.setPlayerId(playerId);
            module.handleEvent(event);
        } catch (ClassCastException e) {
            log.error("Client sent an Event that isn't a PlayerEvent", e);
        }
    }

    protected String getQueryParameterString(Session session, String parameter) {
        String playerId = getQueryParameterStringOptional(session, parameter).orElseThrow(() -> {
            String message = String.format("WebSocket request is missing the query parameter '%s'", parameter);
            return new IllegalArgumentException(message);
        });
        return playerId;
    }

    private Optional<String> getQueryParameterStringOptional(Session session, String parameter) {
        try {
            String value = session.getUpgradeRequest().getParameterMap().get(parameter).get(0);
            return Optional.ofNullable(value);
        } catch (NullPointerException e) {
            return Optional.empty();
        }
    }
}
