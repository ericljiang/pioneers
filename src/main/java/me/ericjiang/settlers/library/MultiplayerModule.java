package me.ericjiang.settlers.library;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;

import lombok.extern.slf4j.Slf4j;

import me.ericjiang.settlers.library.player.PlayerConnection;
import me.ericjiang.settlers.library.player.PlayerConnectionEvent;
import me.ericjiang.settlers.library.player.PlayerDisconnectionEvent;

@Slf4j
public abstract class MultiplayerModule extends EventListener {

    private final transient Multimap<String, PlayerConnection> playerConnections;

    public MultiplayerModule() {
        this.playerConnections = HashMultimap.create();
        on(PlayerConnectionEvent.class, e -> {
            log.info(formatLog("Player %s connected", e.getPlayerId()));
        });
        on(PlayerDisconnectionEvent.class, e -> {
            log.info(formatLog("Player %s disconnected (Reason: %s)", e.getPlayerId(), e.getReason()));
        });
    }

    protected abstract String getIdentifier();

    protected abstract StateEvent toStateEvent();

    protected abstract boolean allowConnection(String playerId);

    public void addConnection(String playerId, PlayerConnection connection) {
        if (!allowConnection(playerId)) {
            connection.close("Unauthorized");
        }
        playerConnections.put(playerId, connection);
        connection.transmit(toStateEvent());
        if (playerConnections.get(playerId).size() == 1) {
            handleEvent(new PlayerConnectionEvent(playerId));
        }
    }

    public void removeConnection(String playerId, PlayerConnection connection, String reason) {
        playerConnections.remove(playerId, connection);
        if (!playerConnections.containsKey(playerId)) {
            handleEvent(new PlayerDisconnectionEvent(playerId, reason));
        }
    }

    public void transmit(String playerId, Event event) {
        playerConnections.get(playerId).forEach(c -> c.transmit(event));
    }

    public void broadcast(Event event) {
        playerConnections.values().forEach(c -> c.transmit(event));
    }

    protected String formatLog(String format, Object... args) {
        return formatLog(String.format(format, args));
    }

    protected String formatLog(String message) {
        return String.format("[%s] %s", getIdentifier(), message);
    }
}
