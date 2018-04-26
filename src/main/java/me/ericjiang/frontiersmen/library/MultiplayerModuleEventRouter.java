package me.ericjiang.frontiersmen.library;

import java.security.GeneralSecurityException;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import com.google.common.collect.Sets;

import lombok.extern.slf4j.Slf4j;
import me.ericjiang.frontiersmen.library.auth.Authenticator;
import me.ericjiang.frontiersmen.library.auth.Ticket;
import me.ericjiang.frontiersmen.library.player.PlayerAuthenticationEvent;
import me.ericjiang.frontiersmen.library.player.PlayerConnection;

@Slf4j
public abstract class MultiplayerModuleEventRouter {

    private final Authenticator authenticator;

    private final Set<PlayerConnection> connectionsPendingAuthentication;

    public MultiplayerModuleEventRouter(Authenticator authenticator) {
        this.authenticator = authenticator;
        this.connectionsPendingAuthentication = Sets.newConcurrentHashSet();
    }

    public void acceptConnection(PlayerConnection connection) throws GeneralSecurityException {
        connectionsPendingAuthentication.add(connection);
    }

    public void removeConnection(PlayerConnection connection, String reason) {
        String playerId = connection.getPlayerId();
        MultiplayerModule module = getModule(connection).orElseThrow(() -> {
            return new IllegalArgumentException("Requested module does not exist.");
        });
        module.removeConnection(playerId, connection, reason);
    }

    public void receiveEvent(PlayerConnection connection, PlayerEvent event) throws GeneralSecurityException {
        log.debug("{} from {}", event.getClass().getSimpleName(), event.getPlayerId());
        if (event instanceof PlayerAuthenticationEvent) {
            receiveAuthenticationEvent(connection, (PlayerAuthenticationEvent) event);
        } else if (!connectionsPendingAuthentication.contains(connection)) {
            MultiplayerModule module = getModule(connection).orElseThrow(() -> {
                return new IllegalArgumentException("Message sent to invalid module.");
            });
            module.handleEvent(event);
        } else {
            throw new GeneralSecurityException("Event sent before authenticated");
        }
    }

    private void receiveAuthenticationEvent(PlayerConnection connection, PlayerAuthenticationEvent event) throws GeneralSecurityException {
        String playerId = event.getPlayerId();
        if (!playerId.equals(connection.getPlayerId())) {
            throw new GeneralSecurityException();
        }
        Ticket authTicket = Ticket.fromString(event.getTicket());
        if (!playerId.equals(authTicket.getPlayerId())) {
            throw new GeneralSecurityException();
        }
        authenticator.checkTicket(authTicket);

        connectionsPendingAuthentication.remove(connection);
        MultiplayerModule module = getModule(connection).orElseThrow(() -> {
            return new IllegalArgumentException("Requested module does not exist.");
        });
        module.addConnection(playerId, connection);
    }

    /**
     * @return a list of Event type classes for this module can receive from clients
     */
    public abstract List<Class<? extends Event>> getEventTypes();

    protected abstract Optional<? extends MultiplayerModule> getModule(PlayerConnection connection);

}
