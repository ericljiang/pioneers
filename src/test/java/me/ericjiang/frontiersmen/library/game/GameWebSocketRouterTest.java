package me.ericjiang.frontiersmen.library.game;

import static org.easymock.EasyMock.*;

import java.util.Optional;

import org.easymock.EasyMockSupport;
import org.eclipse.jetty.websocket.api.Session;
import org.junit.Before;
import org.junit.Test;

import me.ericjiang.frontiersmen.library.auth.Authenticator;
import me.ericjiang.frontiersmen.library.lobby.Lobby;
import me.ericjiang.frontiersmen.library.player.PlayerRepository;

public class GameWebSocketRouterTest extends EasyMockSupport {

    private Lobby lobby;

    private Authenticator authenticator;

    private PlayerRepository playerRepository;

    private GameWebSocketRouter gameWebSocketRouter;

    @Before
    public void before() {
        this.lobby = createNiceMock(Lobby.class);
        this.authenticator = createNiceMock(Authenticator.class);
        this.playerRepository = createNiceMock(PlayerRepository.class);
        this.gameWebSocketRouter = new GameWebSocketRouter(lobby, authenticator, playerRepository);
    }

    @Test(expected = IllegalArgumentException.class)
    public void shouldThrowExceptionForNonexistantGameId() {
        Session session = createNiceMock(Session.class);

        expect(lobby.getGame(anyString())).andReturn(Optional.empty());
        replayAll();

        gameWebSocketRouter.getModule(session);
        verifyAll();
    }
}
