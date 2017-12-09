package me.ericjiang.settlers.library.game;

import java.util.List;
import me.ericjiang.settlers.library.Event;
import me.ericjiang.settlers.library.MultiplayerModule;
import me.ericjiang.settlers.library.MultiplayerModuleWebSocketRouter;
import me.ericjiang.settlers.library.auth.Authenticator;
import me.ericjiang.settlers.library.lobby.Lobby;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import com.google.common.collect.Lists;

@WebSocket
public class GameWebSocketRouter extends MultiplayerModuleWebSocketRouter {

    private final Lobby<?> lobby;

    public GameWebSocketRouter(Lobby<?> lobby, Authenticator authenticator) {
        super(authenticator);
        this.lobby = lobby;
    }

    @Override
    protected MultiplayerModule getModule(Session session) {
        String gameId = getQueryParameterString(session, "gameId");
        MultiplayerModule game = lobby.getGame(gameId);
        if (game == null) {
            throw new IllegalArgumentException("No Game found with id " + gameId);
        }
        return game;
    }

    @Override
    protected List<Class<? extends Event>> getEventTypes() {
        return Lists.newArrayList(GameUpdateEvent.class, StartGameEvent.class);
    }
}
