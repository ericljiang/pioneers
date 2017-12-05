package me.ericjiang.settlers.library;

import com.google.common.collect.Maps;

import lombok.extern.slf4j.Slf4j;

import java.util.Map;
import me.ericjiang.settlers.library.player.Player;
import me.ericjiang.settlers.library.player.PlayerConnectionEvent;
import me.ericjiang.settlers.library.player.PlayerDisconnectionEvent;

@Slf4j
public abstract class MultiplayerModule extends EventListener {

    private final transient Map<String, Player> players;

    public MultiplayerModule() {
        players = Maps.newConcurrentMap();
        on(PlayerConnectionEvent.class, e -> {
            log.info(formatLog("Player %s connected", e.getPlayerId()));
        });
        on(PlayerDisconnectionEvent.class, e -> {
            log.info(formatLog("Player %s disconnected (Reason: %s)", e.getPlayerId(), e.getReason()));
        });
    }

    protected abstract String getIdentifier();

    public void connect(String playerId, Player player) {
        players.put(playerId, player);
    }

    public void disconnect(String playerId) {
        players.remove(playerId);
    }

    public void transmit(String playerId, Event event) {
        players.get(playerId).transmit(event);
    }

    public void broadcast(Event event) {
        players.values().forEach(p -> p.transmit(event));
    }

    protected String formatLog(String format, Object... args) {
        return formatLog(String.format(format, args));
    }

    protected String formatLog(String message) {
        return String.format("[%s] %s", getIdentifier(), message);
    }
}
