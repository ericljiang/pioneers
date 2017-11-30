package me.ericjiang.settlers.library;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import lombok.extern.slf4j.Slf4j;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import me.ericjiang.settlers.library.Event;
import me.ericjiang.settlers.library.MultiplayerModule;
import me.ericjiang.settlers.library.player.WebSocketPlayer;
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

    private final Gson gson;

    public MultiplayerModuleWebSocketRouter() {
        RuntimeTypeAdapterFactory<Event> eventAdapterFactory = RuntimeTypeAdapterFactory.of(Event.class);
        getEventTypes().forEach(t -> eventAdapterFactory.registerSubtype(t));
        this.gson = new GsonBuilder()
                .registerTypeAdapterFactory(eventAdapterFactory)
                .create();
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
            MultiplayerModule module = getModule(session);
            module.connect(playerId, new WebSocketPlayer(session));
        } catch (RuntimeException e) {
            session.close(StatusCode.POLICY_VIOLATION, e.getMessage());
            log.error("Rejected WebSocket connection request", e);
        }
    }

    @OnWebSocketClose
    public void onClose(Session session, int statusCode, String reason) {
        String playerId = getQueryParameterString(session, "playerId");
        MultiplayerModule module = getModule(session);
        module.disconnect(playerId);
    }

    @OnWebSocketMessage
    public void onMessage(Session session, String message) throws IOException {
        MultiplayerModule module = getModule(session);
        Event event = gson.fromJson(message, Event.class);
        module.handleEvent(event);
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
