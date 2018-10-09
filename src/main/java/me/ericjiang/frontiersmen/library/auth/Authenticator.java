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

        if (!playerRepository.hasPlayer(playerId)) {
            final String name = identityProvider.getName(idToken);
            final Ticket ticket = new Ticket(playerId);
            playerRepository.addPlayer(playerId, name, ticket);
        }
        return playerRepository.getTicket(playerId).orElseGet(() -> {
            log.info("Creating new auth ticket for player {}", playerId);
            playerRepository.setTicket(new Ticket(playerId));
            return playerRepository.getTicket(playerId).orElseThrow(() -> {
                return new RuntimeException();
            });
        });
    }

    /**
     * Compare the given ticket to the stored ticket.
     */
    public void checkTicket(Ticket ticket) throws GeneralSecurityException {
        final String playerId = ticket.getPlayerId();
        log.info("Checking auth ticket for player {}...", playerId);
        final Ticket storedTicket = playerRepository.getTicket(playerId).orElseThrow(() -> {
            return new GeneralSecurityException(String.format("No ticket stored for player %s", playerId));
        });
        if (!storedTicket.equals(ticket)) {
            throw new GeneralSecurityException("Provided ticket does not match stored ticket!");
        }
        log.info("Ticket for player {} verified. ✅", playerId);
    }

}
