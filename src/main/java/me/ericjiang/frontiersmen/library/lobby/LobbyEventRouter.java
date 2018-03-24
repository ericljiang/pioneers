package me.ericjiang.frontiersmen.library.lobby;

import java.util.List;
import java.util.Optional;

import com.google.common.collect.Lists;

import me.ericjiang.frontiersmen.library.Event;
import me.ericjiang.frontiersmen.library.MultiplayerModule;
import me.ericjiang.frontiersmen.library.MultiplayerModuleEventRouter;
import me.ericjiang.frontiersmen.library.auth.Authenticator;
import me.ericjiang.frontiersmen.library.player.PlayerConnection;
import me.ericjiang.frontiersmen.library.player.PlayerNameChangeEvent;
import me.ericjiang.frontiersmen.library.player.PlayerRepository;

public class LobbyEventRouter extends MultiplayerModuleEventRouter {

    private final Lobby lobby;

    public LobbyEventRouter(Lobby lobby, Authenticator authenticator, PlayerRepository playerRepository) {
        super(authenticator, playerRepository);
        this.lobby = lobby;
    }

    @Override
    public List<Class<? extends Event>> getEventTypes() {
        return Lists.newArrayList(
                LobbyUpdateEvent.class,
                GameCreationEvent.class,
                PlayerNameChangeEvent.class);
    }

    @Override
    protected Optional<? extends MultiplayerModule> getModule(PlayerConnection connection) {
        return Optional.of(lobby);
    }
}
