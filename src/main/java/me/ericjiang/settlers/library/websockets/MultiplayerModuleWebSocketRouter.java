package me.ericjiang.settlers.library.websockets;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.io.IOException;
import java.util.List;
import me.ericjiang.settlers.library.Event;
import me.ericjiang.settlers.library.MultiplayerModule;
import me.ericjiang.settlers.utility.RuntimeTypeAdapterFactory;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.StatusCode;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketClose;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketConnect;
import org.eclipse.jetty.websocket.api.annotations.OnWebSocketMessage;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

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
     * @return a list of valid Event type classes for this module
     */
    protected abstract List<Class<Event>> getEventTypes();

    @OnWebSocketConnect
    public void onConnect(Session session) throws IOException {
        try {
            String playerId = getQueryParameterString(session, "playerId");
            MultiplayerModule module = getModule(session);
            module.connect(playerId, new WebSocketPlayer(session));
        } catch (IllegalArgumentException e) {
            session.close(StatusCode.POLICY_VIOLATION, e.getMessage());
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
        String value = session.getUpgradeRequest().getParameterMap().get(parameter).get(0);
        if (value == null) {
            IllegalArgumentException e = new IllegalArgumentException("Missing " + parameter + " query parameter");
            e.printStackTrace();
            throw e;
        }
        return value;
    }
}
