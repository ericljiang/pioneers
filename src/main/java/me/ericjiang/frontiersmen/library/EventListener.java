package me.ericjiang.frontiersmen.library;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.LinkedListMultimap;
import com.google.common.collect.Multimap;
import com.google.common.collect.Multimaps;
import com.google.common.collect.Sets;
import java.util.Set;
import java.util.function.BooleanSupplier;
import java.util.function.Consumer;

/**
 * An object that registers Event handlers and can handle Events.
 */
public class EventListener {

    @SuppressWarnings("rawtypes")
    private final transient Multimap<Class<? extends Event>, Consumer> eventHandlers;

    public EventListener() {
        // Using a LinkedListMultimap keeps entries in insertion order
        eventHandlers = Multimaps.synchronizedListMultimap(LinkedListMultimap.create());
    }

    /**
     * Process event with each applicable registered handler. This method
     * ensures that event handlers are triggered in the same order they are
     * registered.
     */
    @SuppressWarnings("unchecked")
    public <T extends Event> void handleEvent(T event) {
        Set<Class<? extends Event>> eventTypes = getAllSuperclasses(event.getClass());
        synchronized (eventHandlers) {
            ImmutableList.copyOf(eventHandlers.entries()).stream()
                    .filter(entry -> eventTypes.contains(entry.getKey()))
                    .map(entry -> entry.getValue())
                    .forEach(handler -> handler.accept(event));
        }
    }

    /**
     * Register an event handler
     */
    public <T extends Event> void on(Class<T> eventType, Consumer<T> eventHandler) {
        eventHandlers.put(eventType, eventHandler);
    }

    /**
     * Creates a set of Classes for each supertype of the provided event type.
     */
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
