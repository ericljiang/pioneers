package me.ericjiang.frontiersmen.library.player;

import lombok.Getter;

@Getter
public class PlayerConnectionEvent extends ClientConnectionEvent {
    public PlayerConnectionEvent(String playerId) {
        super(playerId);
    }
}
