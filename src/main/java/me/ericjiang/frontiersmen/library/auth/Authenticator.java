package me.ericjiang.frontiersmen.library.auth;

import java.security.GeneralSecurityException;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@AllArgsConstructor
public class Authenticator {

    @Getter
    private final IdentityProvider identityProvider;

    private final TicketDao ticketDao;

    /**
     * Return the player's auth ticket, or create a new one and store it.
     *
     * @param playerId
     * @param authToken Auth token from a third-party identity provider
     * @return a {@link Ticket} representing the player's identity
     * @throws GeneralSecurityException when the authToken is invalid
     */
    public Ticket getTicket(String playerId, String authToken) throws GeneralSecurityException {
        identityProvider.verify(playerId, authToken);
        return ticketDao.getTicket(playerId).orElseGet(() -> {
            log.info("Creating new auth ticket for player {}", playerId);
            Ticket ticket = new Ticket(playerId);
            ticketDao.putTicket(playerId, ticket);
            return ticketDao.getTicket(playerId).get();
        });
    }

    /**
     * Compare the given ticket to the stored ticket.
     */
    public void checkTicket(Ticket ticket) throws GeneralSecurityException {
        String playerId = ticket.getPlayerId();
        Ticket storedTicket = ticketDao.getTicket(playerId).orElseThrow(() -> {
            return new GeneralSecurityException(String.format("No ticket stored for player %s", playerId));
        });
        if (!storedTicket.equals(ticket)) {
            throw new GeneralSecurityException("Provided ticket does not match stored ticket!");
        }
    }

}
