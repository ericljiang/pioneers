package me.ericjiang.settlers.library.websockets;

import java.io.IOException;
import me.ericjiang.settlers.library.Event;
import me.ericjiang.settlers.library.Player;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.StatusCode;

import com.google.gson.Gson;

public class WebSocketPlayer implements Player {

    private Session session;

    public WebSocketPlayer(Session session) {
        this.session = session;
    }

    @Override
    public void transmit(Event event) {
        try {
            session.getRemote().sendString(new Gson().toJson(event));
        } catch (IOException e) {
            session.close(StatusCode.SERVER_ERROR, "Failed to send message");
        }
    }
}
