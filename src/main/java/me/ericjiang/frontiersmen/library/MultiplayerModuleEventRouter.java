package me.ericjiang.frontiersmen.library;

import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Optional;

import lombok.AllArgsConstructor;
import me.ericjiang.frontiersmen.library.auth.Authenticator;
import me.ericjiang.frontiersmen.library.player.PlayerConnection;
import me.ericjiang.frontiersmen.library.player.PlayerRepository;

@AllArgsConstructor
public abstract class MultiplayerModuleEventRouter {

    private final Authenticator authenticator;

    private final PlayerRepository playerRepository;

    public void acceptConnection(PlayerConnection connection) throws GeneralSecurityException {
        String playerId = connection.getParameter("playerId");
        String authToken = connection.getParameter("authToken");
        if (!playerRepository.contains(playerId)) {
            String name = authenticator.getIdentityProvider().getName(authToken);
            playerRepository.setDisplayName(playerId, name);
        }
        MultiplayerModule module = getModule(connection).orElseThrow(() -> {
            return new IllegalArgumentException("Requested module does not exist.");
        });
        module.addConnection(playerId, connection);
    }

    public void removeConnection(PlayerConnection connection, String reason) {
        String playerId = connection.getParameter("playerId");
        MultiplayerModule module = getModule(connection).orElseThrow(() -> {
            return new IllegalArgumentException("Requested module does not exist.");
        });
        module.removeConnection(playerId, connection, reason);
    }

    public void receiveEvent(PlayerConnection connection, PlayerEvent event) {
        MultiplayerModule module = getModule(connection).orElseThrow(() -> {
            return new IllegalArgumentException("Message sent to invalid module.");
        });
        module.handleEvent(event);
    }

    /**
     * @return a list of Event type classes for this module can receive from clients
     */
    public abstract List<Class<? extends Event>> getEventTypes();

    protected abstract Optional<? extends MultiplayerModule> getModule(PlayerConnection connection);

}
