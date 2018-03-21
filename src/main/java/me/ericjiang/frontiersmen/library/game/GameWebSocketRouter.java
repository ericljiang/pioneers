package me.ericjiang.frontiersmen.library.game;

import java.util.List;
import java.util.Optional;
import me.ericjiang.frontiersmen.library.Event;
import me.ericjiang.frontiersmen.library.MultiplayerModule;
import me.ericjiang.frontiersmen.library.MultiplayerModuleWebSocketRouter;
import me.ericjiang.frontiersmen.library.auth.Authenticator;
import me.ericjiang.frontiersmen.library.lobby.Lobby;
import me.ericjiang.frontiersmen.library.player.PlayerNameChangeEvent;
import me.ericjiang.frontiersmen.library.player.PlayerRepository;
import org.eclipse.jetty.websocket.api.Session;
import org.eclipse.jetty.websocket.api.annotations.WebSocket;

import com.google.common.annotations.VisibleForTesting;
import com.google.common.collect.Lists;

@WebSocket
public class GameWebSocketRouter extends MultiplayerModuleWebSocketRouter {

    private final String GAME_ID_PARAMETER = "gameId";

    private final Lobby lobby;

    public GameWebSocketRouter(Lobby lobby, Authenticator authenticator, PlayerRepository playerRepository) {
        super(authenticator, playerRepository);
        this.lobby = lobby;
    }

    @Override
    protected Optional<? extends MultiplayerModule> getModule(Session session) {
        return lobby.getGame(getGameId(session));
    }

    @Override
    protected List<Class<? extends Event>> getEventTypes() {
        return Lists.newArrayList(GameUpdateEvent.class, StartGameEvent.class, PlayerNameChangeEvent.class);
    }

    @VisibleForTesting
    protected String getGameId(Session session) {
        return getQueryParameterString(session, GAME_ID_PARAMETER);
    }
}
