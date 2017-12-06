package me.ericjiang.settlers.library;

import com.google.common.collect.HashMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Sets;
import java.util.Collection;
import java.util.Set;
import java.util.function.Consumer;

public class EventListener {

    @SuppressWarnings("rawtypes")
    private final transient Multimap<Class<? extends Event>, Consumer> eventHandlers;

    public EventListener() {
        eventHandlers = HashMultimap.create();
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
