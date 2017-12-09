package me.ericjiang.settlers.library.player;

import lombok.Getter;
import lombok.Setter;

@Getter
public class Player {

    private final String id;

    @Setter
    private boolean online;

    public Player(String id) {
        this.id = id;
        this.online = false;
    }

}
