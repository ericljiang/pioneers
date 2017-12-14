package me.ericjiang.settlers.library;

import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Sets;

import java.util.Set;
import java.util.function.Consumer;

public class EventListener {

    @SuppressWarnings("rawtypes")
    private final transient LinkedListMultimap<Class<? extends Event>, Consumer> eventHandlers;

    public EventListener() {
        eventHandlers = LinkedListMultimap.create();
    }

    /**
     * Process event with each registered handler in registration order
     */
    @SuppressWarnings("unchecked")
    public <T extends Event> void handleEvent(T event) {
        Set<Class<? extends Event>> eventTypes = getAllSuperclasses(event.getClass());
        eventHandlers.entries().stream()
                .filter(entry -> eventTypes.contains(entry.getKey()))
                .map(entry -> entry.getValue())
                .forEach(handler -> handler.accept(event));
    }

    /**
     * Register an event handler
     */
    public <T extends Event> void on(Class<T> eventType, Consumer<T> consumer) {
        eventHandlers.put(eventType, consumer);
    }

    @SuppressWarnings("unchecked")
    private Set<Class<? extends Event>> getAllSuperclasses(Class<? extends Event> eventType) {
        Set<Class<? extends Event>> classes = Sets.newHashSet(eventType);
        Class<?> clazz = eventType;
        while (!clazz.equals(Event.class)) {
            clazz = clazz.getSuperclass();
            classes.add((Class<? extends Event>) clazz);
        }
        return classes;
    }

}
