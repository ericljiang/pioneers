package me.ericjiang.settlers.library;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;

import java.util.Collection;
import java.util.Map;
import java.util.Set;
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
        handleEvent(new PlayerDisconnectionEvent(playerId));
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
