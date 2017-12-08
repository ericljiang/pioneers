package me.ericjiang.settlers.library.player;

import me.ericjiang.settlers.library.Event;

public interface PlayerConnection {
    void transmit(Event event);
    void close(String reason);
}
