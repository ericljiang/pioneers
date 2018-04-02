package me.ericjiang.frontiersmen.library;

import static org.easymock.EasyMock.*;

import java.util.List;
import java.util.function.Consumer;

import com.google.common.collect.Lists;

import org.easymock.EasyMockSupport;
import org.easymock.IMocksControl;
import org.junit.Before;
import org.junit.Test;

@SuppressWarnings("unchecked")
public class EventListenerTest extends EasyMockSupport {

    private EventListener eventListener;

    @Before
    public void before() {
        eventListener = new EventListener();
    }

    @Test
    public void shouldTriggerEventHandlers() {
        Consumer<Event> eventHandler = createMock(Consumer.class);
        eventHandler.accept(anyObject());
        expectLastCall();
        replayAll();

        eventListener.on(Event.class, eventHandler);
        eventListener.handleEvent(new Event());
        verifyAll();
    }

    @Test
    public void shouldTriggerEventHandlersInOrder() {
        List<Consumer<Event>> eventHandlers = Lists.newArrayList();
        IMocksControl control = createStrictControl();
        for (int i = 0; i < 10; i++) {
            Consumer<Event> eventHandler = control.createMock(Consumer.class);
            eventHandlers.add(eventHandler);
            eventHandler.accept(anyObject());
            expectLastCall();
        }
        replayAll();

        eventHandlers.forEach(h -> {
            eventListener.on(Event.class, h);
        });
        eventListener.handleEvent(new Event());
        verifyAll();
    }

    @Test
    public void shouldTriggerHandlersForEventSupertypes() {
        Consumer<SuperEvent> superEventHandler = createMock(Consumer.class);
        superEventHandler.accept(anyObject());
        expectLastCall();
        replayAll();

        eventListener.on(SuperEvent.class, superEventHandler);
        eventListener.handleEvent(new SubEvent());
        verifyAll();
    }

    @Test
    public void shouldTriggerEventHandlersInOrderAcrossEventTypes() {
        List<Consumer<? extends Event>> eventHandlers = Lists.newArrayList();
        IMocksControl control = createStrictControl();

        Consumer<SuperEvent> superEventHandler1 = control.createMock(Consumer.class);
        eventHandlers.add(superEventHandler1);
        Consumer<SubEvent> subEventHandler1 = control.createMock(Consumer.class);
        eventHandlers.add(subEventHandler1);
        Consumer<SuperEvent> superEventHandler2 = control.createMock(Consumer.class);
        eventHandlers.add(superEventHandler2);
        Consumer<SubEvent> subEventHandler2 = control.createMock(Consumer.class);
        eventHandlers.add(subEventHandler2);

        eventHandlers.forEach(h -> {
            h.accept(anyObject());
            expectLastCall();
        });
        replayAll();

        eventListener.on(SuperEvent.class, superEventHandler1);
        eventListener.on(SubEvent.class, subEventHandler1);
        eventListener.on(SuperEvent.class, superEventHandler2);
        eventListener.on(SubEvent.class, subEventHandler2);
        eventListener.handleEvent(new SubEvent());
        verifyAll();
    }

    private class SuperEvent extends Event {}

    private class SubEvent extends SuperEvent {}
}
