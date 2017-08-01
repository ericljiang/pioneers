package me.ericjiang.settlers.spark;

import lombok.AllArgsConstructor;

import me.ericjiang.settlers.core.Player;
import me.ericjiang.settlers.core.actions.Action;

import java.io.IOException;

import org.eclipse.jetty.websocket.api.Session;

@AllArgsConstructor
public class WebSocketPlayer implements Player {
    private Session session;

    public String id() {
        return session.getUpgradeRequest().getParameterMap().get("u").get(0);
    }

    public void update(Action action) {
        if (!session.isOpen()) {
            return;
        }
        try {
            session.getRemote().sendString("{\"id\":1,\"gameId\":1}");
        } catch (IOException e) {
            throw new InternalError(e);
        }
    }
}