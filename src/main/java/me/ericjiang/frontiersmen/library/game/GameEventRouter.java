package me.ericjiang.frontiersmen.library.game;

import java.util.List;
import java.util.Optional;

import me.ericjiang.frontiersmen.library.Event;
import me.ericjiang.frontiersmen.library.MultiplayerModule;
import me.ericjiang.frontiersmen.library.MultiplayerModuleEventRouter;
import me.ericjiang.frontiersmen.library.auth.Authenticator;
import me.ericjiang.frontiersmen.library.lobby.Lobby;
import me.ericjiang.frontiersmen.library.player.PlayerConnection;
import me.ericjiang.frontiersmen.library.player.PlayerNameChangeEvent;
import me.ericjiang.frontiersmen.tictactoe.PlaceMarkEvent;

import com.google.common.collect.Lists;

public class GameEventRouter extends MultiplayerModuleEventRouter {

    private final String GAME_ID_PARAMETER = "gameId";

    private final Lobby lobby;

    public GameEventRouter(Lobby lobby, Authenticator authenticator) {
        super(authenticator);
        this.lobby = lobby;
    }

    @Override
    public List<Class<? extends Event>> getEventTypes() {
        return Lists.newArrayList(
                GameUpdateEvent.class,
                PlayerNameChangeEvent.class,
                PlaceMarkEvent.class);
    }

    @Override
    protected Optional<? extends MultiplayerModule> getModule(PlayerConnection connection) {
        return lobby.getGame(connection.getParameter(GAME_ID_PARAMETER));
    }
}
