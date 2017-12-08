package me.ericjiang.settlers.library.player;

import lombok.Getter;
import lombok.Setter;
import me.ericjiang.settlers.library.Event;

@Getter
public class Player {

    @Setter
    private transient PlayerConnection connection;

    @Setter
    private boolean online;

    public Player() {
        this.online = false;
    }

    public void transmit(Event event) {
        connection.transmit(event);
    }

}
