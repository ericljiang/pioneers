package me.ericjiang.settlers.library.player;

import java.io.IOException;
import me.ericjiang.settlers.library.Event;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.StatusCode;

import com.google.gson.Gson;

import lombok.AllArgsConstructor;

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
}
