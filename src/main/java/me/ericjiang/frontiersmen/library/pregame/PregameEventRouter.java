package me.ericjiang.frontiersmen.library.pregame;

import java.util.List;
import java.util.Optional;

import me.ericjiang.frontiersmen.library.Event;
import me.ericjiang.frontiersmen.library.MultiplayerModule;
import me.ericjiang.frontiersmen.library.MultiplayerModuleEventRouter;
import me.ericjiang.frontiersmen.library.auth.Authenticator;
import me.ericjiang.frontiersmen.library.lobby.Lobby;
import me.ericjiang.frontiersmen.library.player.PlayerConnection;
import me.ericjiang.frontiersmen.library.player.PlayerRepository;

import com.google.common.collect.Lists;

public class PregameEventRouter extends MultiplayerModuleEventRouter {

    private final String GAME_ID_PARAMETER = "gameId";

    private final Lobby lobby;

    public PregameEventRouter(Lobby lobby, Authenticator authenticator, PlayerRepository playerRepository) {
        super(authenticator, playerRepository);
        this.lobby = lobby;
    }

    @Override
    public List<Class<? extends Event>> getEventTypes() {
        return Lists.newArrayList(PregameUpdateEvent.class, StartGameEvent.class, TakeSeatEvent.class, LeaveSeatEvent.class, TransitionToGameEvent.class);
    }

    @Override
    protected Optional<? extends MultiplayerModule> getModule(PlayerConnection connection) {
        return lobby.getPregame(connection.getParameter(GAME_ID_PARAMETER));
    }
}
