package me.ericjiang.settlers.library.lobby;

import java.util.List;
import me.ericjiang.settlers.library.Event;
import me.ericjiang.settlers.library.lobby.Lobby;
import me.ericjiang.settlers.library.websockets.MultiplayerModuleWebSocketRouter;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import com.google.common.collect.Lists;

import lombok.AllArgsConstructor;

@WebSocket
@AllArgsConstructor
public class LobbyWebSocketHandler extends MultiplayerModuleWebSocketRouter {

    private final Lobby lobby;

    @Override
    protected Lobby getModule(Session session) {
        return lobby;
    }

    @Override
    protected List<Class<? extends Event>> getEventTypes() {
        return Lists.newArrayList(GameCreationEvent.class);
    }
}
