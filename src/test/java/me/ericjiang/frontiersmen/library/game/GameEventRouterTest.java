package me.ericjiang.frontiersmen.library.game;

import static org.easymock.EasyMock.*;
import static org.junit.Assert.*;

import java.util.Optional;

import org.easymock.EasyMockSupport;
import org.junit.Before;
import org.junit.Test;

import me.ericjiang.frontiersmen.library.auth.Authenticator;
import me.ericjiang.frontiersmen.library.lobby.Lobby;
import me.ericjiang.frontiersmen.library.player.PlayerConnection;
import me.ericjiang.frontiersmen.library.player.PlayerRepository;

public class GameEventRouterTest extends EasyMockSupport {

    private Lobby lobby;

    private Authenticator authenticator;

    private PlayerRepository playerRepository;

    private GameEventRouter gameEventRouter;

    @Before
    public void before() {
        this.lobby = createNiceMock(Lobby.class);
        this.authenticator = createNiceMock(Authenticator.class);
        this.playerRepository = createNiceMock(PlayerRepository.class);
        this.gameEventRouter = new GameEventRouter(lobby, authenticator, playerRepository);
    }

    @Test
    public void shouldNotReturnGameIfLobbyDoesNotReturnGame() {
        PlayerConnection connection = createNiceMock(PlayerConnection.class);
        String gameId = "1";

        expect(connection.getParameter("gameId")).andStubReturn(gameId);
        expect(lobby.getGame(gameId)).andReturn(Optional.empty());
        replayAll();

        assertFalse(gameEventRouter.getModule(connection).isPresent());
        verifyAll();
    }
}
