package me.ericjiang.frontiersmen.library.auth;

import java.security.GeneralSecurityException;

import org.junit.Before;
import org.junit.Test;

public class AuthenticatorTest {

    private static final String PLAYER_ID = "1";

    private static final String AUTH_TOKEN = "a";

    private IdentityProvider identityProvider;

    private TicketDao ticketDao;

    private Authenticator authenticator;

    @Before
    public void setup() {
        this.identityProvider = new MockIdentityProvider();
        this.ticketDao = new TicketDaoInMemory();
        this.authenticator = new Authenticator(identityProvider, ticketDao);
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

}
