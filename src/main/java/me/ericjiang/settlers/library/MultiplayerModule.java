package me.ericjiang.settlers.library;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

import lombok.extern.slf4j.Slf4j;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;
import me.ericjiang.settlers.library.player.Player;
import me.ericjiang.settlers.library.player.PlayerConnectionEvent;
import me.ericjiang.settlers.library.player.PlayerDisconnectionEvent;

@Slf4j
public abstract class MultiplayerModule {

    private final Map<String, Player> players;

    @SuppressWarnings("rawtypes")
    private final Multimap<Class<? extends Event>, Consumer> eventHandlers;

    public MultiplayerModule() {
        players = Maps.newConcurrentMap();
        eventHandlers = HashMultimap.create();
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

    @SuppressWarnings("unchecked")
    public <T extends Event> void handleEvent(T event) {
        getAllSuperclasses(event.getClass()).forEach(eventType -> {
            eventHandlers.get(eventType).forEach(handler -> {
                handler.accept(event);
            });
        });
    }

    public <T extends Event> void on(Class<T> eventType, Consumer<T> consumer) {
        eventHandlers.put(eventType, consumer);
    }

    protected String formatLog(String format, Object... args) {
        return formatLog(String.format(format, args));
    }

    protected String formatLog(String message) {
        return String.format("[%s] %s", getIdentifier(), message);
    }

    @SuppressWarnings("unchecked")
    private Collection<Class<? extends Event>> getAllSuperclasses(Class<? extends Event> eventType) {
        Set<Class<? extends Event>> classes = Sets.newHashSet(eventType);
        Class<?> clazz = eventType;
        while (clazz.equals(Event.class)) {
            clazz = clazz.getSuperclass();
            classes.add((Class<? extends Event>) clazz);
        }
        return classes;
    }
}
