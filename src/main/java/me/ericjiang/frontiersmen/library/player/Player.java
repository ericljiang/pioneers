package me.ericjiang.frontiersmen.library.player;

import lombok.Getter;
import lombok.Setter;

@Getter
public class Player {

    private final String id;

    private int seat;

    @Setter
    private boolean online;

    public Player(String id) {
        this.id = id;
        this.seat = -1;
        this.online = false;
    }

    public Player(String id, int seat) {
        this.id = id;
        this.seat = seat;
        this.online = false;
    }

}
