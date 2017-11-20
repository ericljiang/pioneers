package me.ericjiang.settlers.library.websockets;

import java.util.List;
import me.ericjiang.settlers.library.Event;
import me.ericjiang.settlers.library.Game;
import me.ericjiang.settlers.library.lobby.Lobby;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import com.google.common.collect.Lists;

import lombok.AllArgsConstructor;

@WebSocket
@AllArgsConstructor
public class GameWebSocketRouter extends MultiplayerModuleWebSocketRouter {

    private final Lobby lobby;

    @Override
    protected Game getModule(Session session) {
        String gameId = getQueryParameterString(session, "gameId");
        return lobby.getGame(gameId);
    }

    @Override
    protected List<Class<? extends Event>> getEventTypes() {
        return Lists.newArrayList();
    }
}
