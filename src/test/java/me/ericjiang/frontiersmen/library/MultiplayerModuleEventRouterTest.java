package me.ericjiang.frontiersmen.library;

import static org.easymock.EasyMock.*;

import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Optional;

import com.google.common.collect.Lists;
import com.google.gson.Gson;

import org.easymock.EasyMockSupport;
import org.junit.Before;
import org.junit.Test;

import me.ericjiang.frontiersmen.library.auth.Authenticator;
import me.ericjiang.frontiersmen.library.auth.Ticket;
import me.ericjiang.frontiersmen.library.player.PlayerAuthenticationEvent;
import me.ericjiang.frontiersmen.library.player.PlayerConnection;

public class MultiplayerModuleEventRouterTest extends EasyMockSupport {

    private static final String PLAYER_ID = "1";

    private static final Ticket TICKET = new Ticket(PLAYER_ID);

    private Authenticator authenticator;

    private MultiplayerModule module;

    private MultiplayerModuleEventRouter eventRouter;

    private PlayerConnection connection;

    @Before
    public void setup() {
        this.authenticator = createNiceMock(Authenticator.class);
        this.module = createNiceMock(MultiplayerModule.class);
        this.eventRouter = new MultiplayerModuleEventRouter(authenticator){
            @Override
            protected Optional<? extends MultiplayerModule> getModule(PlayerConnection connection) {
                return Optional.of(module);
            }

            @Override
            public List<Class<? extends Event>> getEventTypes() {
                return Lists.newArrayList();
            }
        };
        this.connection = createNiceMock(PlayerConnection.class);
        expect(connection.getPlayerId()).andStubReturn(PLAYER_ID);
    }

    @Test(expected = GeneralSecurityException.class)
    public void shouldNotAcceptEventsBeforeAuthentication() throws GeneralSecurityException {
        eventRouter.acceptConnection(connection);
        PlayerEvent event = new PlayerEvent();
        event.setPlayerId(PLAYER_ID);
        eventRouter.receiveEvent(connection, event);
    }

    @Test
    public void shouldAcceptEventsAfterAuthentication() throws GeneralSecurityException {
        replayAll();

        eventRouter.acceptConnection(connection);
        PlayerAuthenticationEvent authEvent = new PlayerAuthenticationEvent(PLAYER_ID, new Gson().toJson(TICKET));
        eventRouter.receiveEvent(connection, authEvent);
        PlayerEvent event = new PlayerEvent();
        event.setPlayerId(PLAYER_ID);
        eventRouter.receiveEvent(connection, event);
        verifyAll();
    }
}
