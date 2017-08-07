package me.ericjiang.settlers.spark;

import java.io.IOException;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import me.ericjiang.settlers.core.actions.Action;
import me.ericjiang.settlers.core.player.Player;
import org.eclipse.jetty.websocket.api.Session;

@AllArgsConstructor
@Slf4j
public class WebSocketPlayer implements Player {
    private Session session;

    public String id() {
        return session.getUpgradeRequest().getParameterMap().get("u").get(0);
    }

    public void update(Action action) {
        if (session.isOpen()) {
            try {
                log.debug("Sending action to player: " + action.toString());
                session.getRemote().sendString(action.toString());
            } catch (IOException e) {
                throw new InternalError("Exception occured while updating a client with an action.", e);
            }
        }
    }
}