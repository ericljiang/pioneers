package me.ericjiang.frontiersmen.library.player;

import lombok.Getter;

@Getter
public class PlayerDisconnectionEvent extends ClientDisconnectionEvent {

    public PlayerDisconnectionEvent(String playerId, String reason) {
        super(playerId, reason);
    }

}
