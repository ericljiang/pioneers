package me.ericjiang.settlers.library;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import java.util.Map;
import java.util.function.Consumer;

public abstract class MultiplayerModule {

    private final Map<String, Player> players;

    @SuppressWarnings("rawtypes")
    private final Multimap<Class<? extends Event>, Consumer> eventHandlers;

    public MultiplayerModule() {
        players = Maps.newConcurrentMap();
        eventHandlers = HashMultimap.create();
    }

    public void connect(String playerId, Player player) {
        players.put(playerId, player);
        handleEvent(new PlayerConnectionEvent(playerId));
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
        eventHandlers.get(event.getClass())
                .forEach(handler -> handler.accept(event));
    }

    protected <T extends Event> void on(Class<T> eventType, Consumer<T> consumer) {
        eventHandlers.put(eventType, consumer);
    }
}
