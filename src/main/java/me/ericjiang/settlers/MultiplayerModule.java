package me.ericjiang.settlers;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import java.util.function.Consumer;

public abstract class MultiplayerModule {

    private static final Multimap<Class<? extends Event>, Consumer> EVENT_HANDLERS = HashMultimap.create();

    public abstract void onConnect(String playerId, Player player);

    public abstract void onDisconnect(String playerId);

    protected static <T extends Event> void on(Class<T> eventType, Consumer<T> consumer) {
        EVENT_HANDLERS.put(eventType, consumer);
    }

    protected <T extends Event> void handleEvent(T event) {
        EVENT_HANDLERS.get(event.getClass())
                .forEach(handler -> handler.accept(event));
    }
}
