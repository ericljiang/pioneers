package me.ericjiang.frontiersmen.library.auth;

import static org.junit.Assert.assertTrue;

import java.security.GeneralSecurityException;

import org.junit.Before;
import org.junit.Test;

import me.ericjiang.frontiersmen.library.player.InMemoryPlayerRepository;
import me.ericjiang.frontiersmen.library.player.PlayerRepository;

public class AuthenticatorTest {

    private static final String PLAYER_ID = "1";

    private static final String AUTH_TOKEN = "a";

    private IdentityProvider identityProvider;

    private TicketDao ticketDao;

    private PlayerRepository playerRepository;

    private Authenticator authenticator;

    @Before
    public void setup() {
        this.identityProvider = new MockIdentityProvider();
        this.ticketDao = new TicketDaoInMemory();
        this.playerRepository = new InMemoryPlayerRepository();
        this.authenticator = new Authenticator(identityProvider, ticketDao, playerRepository);
    }

    @Test
    public void shouldAcceptProvidedTicket() throws GeneralSecurityException {
        Ticket ticket = authenticator.getTicket(PLAYER_ID, AUTH_TOKEN);
        authenticator.checkTicket(ticket);
    }

    @Test(expected = GeneralSecurityException.class)
    public void shouldFailUnknownTicket() throws GeneralSecurityException {
        authenticator.getTicket(PLAYER_ID, AUTH_TOKEN);
        authenticator.checkTicket(new Ticket(PLAYER_ID));
    }

    @Test
    public void shouldRecordName() throws GeneralSecurityException {
        authenticator.getTicket(PLAYER_ID, AUTH_TOKEN);
        assertTrue(playerRepository.contains(PLAYER_ID));
    }

}
