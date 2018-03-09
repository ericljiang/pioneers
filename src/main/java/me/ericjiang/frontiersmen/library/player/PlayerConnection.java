package me.ericjiang.frontiersmen.library.player;

import me.ericjiang.frontiersmen.library.Event;

public interface PlayerConnection {
    void transmit(Event event);
    void close(String reason);
}
