package me.ericjiang.frontiersmen.websockets;

import java.io.IOException;

import me.ericjiang.frontiersmen.library.Event;
import me.ericjiang.frontiersmen.library.player.PlayerConnection;

import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.StatusCode;

import lombok.AllArgsConstructor;

import com.google.gson.Gson;

@AllArgsConstructor
public class WebSocketPlayerConnection implements PlayerConnection {

    private final Session session;

    private final Gson gson;

    @Override
    public void transmit(Event event) {
        try {
            session.getRemote().sendString(gson.toJson(event, Event.class));
        } catch (IOException e) {
            session.close(StatusCode.SERVER_ERROR, "Failed to send message");
        }
    }

    @Override
    public String getParameter(String parameter) {
        try {
            return session.getUpgradeRequest().getParameterMap().get(parameter).get(0);
        } catch (NullPointerException e) {
            throw new IllegalArgumentException(
                String.format("WebSocket request is missing the '%s' parameter. (%s)",
                        parameter, session.getUpgradeRequest().getRequestURI().toString()));
        }
    }
}
