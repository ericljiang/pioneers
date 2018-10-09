package me.ericjiang.frontiersmen.library.auth;

import static org.easymock.EasyMock.*;

import java.security.GeneralSecurityException;
import java.util.Optional;

import org.easymock.EasyMockSupport;
import org.junit.Before;
import org.junit.Test;

import me.ericjiang.frontiersmen.library.player.PlayerRepository;

public class AuthenticatorTest extends EasyMockSupport {

    private static final String PLAYER_ID = "1";

    private static final String ID_TOKEN = "a";

    private static final Ticket AUTH_TICKET = new Ticket(PLAYER_ID);

    private IdentityProvider identityProvider;

    private PlayerRepository playerRepository;

    private Authenticator authenticator;

    @Before
    public void setup() {
        this.identityProvider = new MockIdentityProvider();
        this.playerRepository = createMock(PlayerRepository.class);
        this.authenticator = new Authenticator(identityProvider, playerRepository);
    }

    @Test
    public void shouldAcceptProvidedTicket() throws GeneralSecurityException {
        expect(playerRepository.getTicket(PLAYER_ID)).andReturn(Optional.of(AUTH_TICKET));
        replayAll();

        authenticator.checkTicket(AUTH_TICKET);
        verifyAll();
    }

    @Test(expected = GeneralSecurityException.class)
    public void shouldFailUnknownTicket() throws GeneralSecurityException {
        expect(playerRepository.getTicket(PLAYER_ID)).andReturn(Optional.of(AUTH_TICKET));
        replayAll();

        authenticator.checkTicket(new Ticket(PLAYER_ID));
        verifyAll();
    }

    @Test
    public void shouldRecordNameOnFirstEncounter() throws GeneralSecurityException {
        expect(playerRepository.hasPlayer(PLAYER_ID)).andReturn(false);
        playerRepository.addPlayer(eq(PLAYER_ID), eq(identityProvider.getName("")), anyObject(Ticket.class));
        expectLastCall();
        expect(playerRepository.getTicket(PLAYER_ID)).andReturn(Optional.of(new Ticket(PLAYER_ID)));
        replayAll();

        authenticator.getTicket(PLAYER_ID, ID_TOKEN);
        verifyAll();
    }

}
