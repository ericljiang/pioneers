package me.ericjiang.frontiersmen.library.game;

import java.util.List;
import me.ericjiang.frontiersmen.library.Event;
import me.ericjiang.frontiersmen.library.MultiplayerModule;
import me.ericjiang.frontiersmen.library.MultiplayerModuleWebSocketRouter;
import me.ericjiang.frontiersmen.library.auth.Authenticator;
import me.ericjiang.frontiersmen.library.lobby.Lobby;
import me.ericjiang.frontiersmen.library.player.PlayerNameChangeEvent;
import me.ericjiang.frontiersmen.library.player.PlayerRepository;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import com.google.common.collect.Lists;

@WebSocket
public class GameWebSocketRouter extends MultiplayerModuleWebSocketRouter {

    private final Lobby lobby;

    public GameWebSocketRouter(Lobby lobby, Authenticator authenticator, PlayerRepository playerRepository) {
        super(authenticator, playerRepository);
        this.lobby = lobby;
    }

    @Override
    protected MultiplayerModule getModule(Session session) {
        String gameId = getQueryParameterString(session, "gameId");
        MultiplayerModule game = lobby.getGame(gameId)
                .orElseThrow(() -> new IllegalArgumentException("No Game found with id " + gameId));
        return game;
    }

    @Override
    protected List<Class<? extends Event>> getEventTypes() {
        return Lists.newArrayList(GameUpdateEvent.class, StartGameEvent.class, PlayerNameChangeEvent.class);
    }
}
