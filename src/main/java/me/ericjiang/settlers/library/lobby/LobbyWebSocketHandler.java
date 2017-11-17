package me.ericjiang.settlers.library.lobby;

import java.util.List;
import me.ericjiang.settlers.library.Event;
import me.ericjiang.settlers.library.lobby.Lobby;
import me.ericjiang.settlers.library.websockets.MultiplayerModuleWebSocketRouter;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import com.google.common.collect.Lists;

@WebSocket
public class LobbyWebSocketHandler extends MultiplayerModuleWebSocketRouter {

    private Lobby lobby;

    public LobbyWebSocketHandler(Lobby lobby) {
        this.lobby = lobby;
    }

    @Override
    protected Lobby getModule(Session session) {
        return lobby;
    }

    @Override
    protected List<Class<Event>> getEventTypes() {
        return Lists.newArrayList();
    }
}
