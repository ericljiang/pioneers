package me.ericjiang.frontiersmen.library;

import com.google.common.collect.Maps;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import me.ericjiang.frontiersmen.library.MultiplayerModule;
import me.ericjiang.frontiersmen.library.auth.Authenticator;
import me.ericjiang.frontiersmen.library.player.PlayerConnection;
import me.ericjiang.frontiersmen.library.player.PlayerRepository;
import me.ericjiang.frontiersmen.library.player.PlayerTypeAdapterFactory;
import me.ericjiang.frontiersmen.library.player.WebSocketPlayerConnection;
import me.ericjiang.frontiersmen.library.utility.RuntimeTypeAdapterFactory;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.StatusCode;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

@Slf4j
@WebSocket
public abstract class MultiplayerModuleWebSocketRouter {

    private final String PLAYER_ID_PARAMETER = "playerId";

    private final String AUTH_TOKEN_PARAMETER = "authToken";

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

    protected abstract Optional<? extends MultiplayerModule> getModule(Session session);

    /**
     * @return a list of Event type classes for this module can receive from clients
     */
    protected abstract List<Class<? extends Event>> getEventTypes();

    @OnWebSocketConnect
    public void onConnect(Session session) throws IOException {
        try {
            String playerId = getPlayerId(session);
            String authToken = getAuthToken(session);
            String name = authenticator.verify(playerId, authToken);
            if (!playerRepository.contains(playerId)) {
                playerRepository.setDisplayName(playerId, name);
            }
            MultiplayerModule module = getModule(session).orElseThrow(() -> {
                return new IllegalArgumentException("Requested module does not exist.");
            });
            PlayerConnection connection = new WebSocketPlayerConnection(session, gson);
            connections.put(session, connection);
            module.addConnection(playerId, connection);
        } catch (GeneralSecurityException | IllegalArgumentException e) {
            session.close(StatusCode.POLICY_VIOLATION, e.getMessage());
            log.error("Rejected WebSocket connection request", e);
        } catch (RuntimeException e) {
            log.error("Error receiving client connection", e);
            session.close(StatusCode.SERVER_ERROR, e.toString());
        }
    }

    @OnWebSocketClose
    public void onClose(Session session, int statusCode, String reason) {
        try {
            String playerId = getPlayerId(session);
            MultiplayerModule module = getModule(session).orElseThrow(() -> {
                return new IllegalArgumentException("Requested module does not exist.");
            });
            PlayerConnection connection = connections.get(session);
            module.removeConnection(playerId, connection, reason);
        } catch (IllegalArgumentException e) {
            log.error("Invalid WebSocket request", e);
            session.close(StatusCode.POLICY_VIOLATION, e.getMessage());
        }
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        log.debug(String.format("Received message from %s: %s", session.getUpgradeRequest().getRequestURI(), message));
        try {
            String playerId = getPlayerId(session);
            MultiplayerModule module = getModule(session).orElseThrow(() -> {
                return new IllegalArgumentException("Message sent to invalid module.");
            });
            PlayerEvent event = (PlayerEvent) gson.fromJson(message, Event.class);
            event.setPlayerId(playerId);
            module.handleEvent(event);
        } catch (ClassCastException e) {
            log.error("Client sent an Event that isn't a PlayerEvent", e);
        } catch (IllegalArgumentException e) {
            log.error("Invalid WebSocket request", e);
            session.close(StatusCode.POLICY_VIOLATION, e.getMessage());
        } catch (RuntimeException e) {
            log.error("Error processing client message", e);
        }
    }

    protected String getPlayerId(Session session) {
        return getQueryParameterString(session, PLAYER_ID_PARAMETER);
    }

    protected String getAuthToken(Session session) {
        return getQueryParameterString(session, AUTH_TOKEN_PARAMETER);
    }

    protected String getQueryParameterString(Session session, String parameter) {
        try {
            return session.getUpgradeRequest().getParameterMap().get(parameter).get(0);
        } catch (NullPointerException e) {
            throw new IllegalArgumentException(
                String.format("WebSocket request is missing the '%s' parameter. (%s)",
                        parameter, session.getUpgradeRequest().getRequestURI().toString()));
        }
    }
}
