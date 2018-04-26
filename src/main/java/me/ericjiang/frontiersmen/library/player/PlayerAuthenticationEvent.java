package me.ericjiang.frontiersmen.library.player;

import lombok.Getter;
import lombok.Setter;
import me.ericjiang.frontiersmen.library.PlayerEvent;

@Getter
@Setter
public class PlayerAuthenticationEvent extends PlayerEvent {

    private String ticket;

    public PlayerAuthenticationEvent(String playerId, String ticket) {
        super(playerId);
        this.ticket = ticket;
    }

}
