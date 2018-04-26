package me.ericjiang.frontiersmen.library.auth;

import java.security.GeneralSecurityException;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;
import me.ericjiang.frontiersmen.library.player.PlayerRepository;

@Slf4j
@AllArgsConstructor
public class Authenticator {

    @Getter
    private final IdentityProvider identityProvider;

    private final TicketDao ticketDao;

    private final PlayerRepository playerRepository;

    /**
     * Return the player's auth ticket, or create a new one and store it.
     *
     * Stores the player's name in the PlayerRepository if the player has no name.
     *
     * @param playerId
     * @param idToken Auth token from a third-party identity provider
     * @return a {@link Ticket} representing the player's identity
     * @throws GeneralSecurityException when the idToken is invalid
     */
    public Ticket getTicket(String playerId, String idToken) throws GeneralSecurityException {
        identityProvider.verify(playerId, idToken);
        if (!playerRepository.contains(playerId)) {
            String name = identityProvider.getName(idToken);
            playerRepository.setDisplayName(playerId, name);
        }
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
