package me.ericjiang.settlers.library.player;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.ericjiang.settlers.library.Event;

@Getter
@AllArgsConstructor
public class PlayerDisconnectionEvent extends Event {

    private final String playerId;

    private final String reason;

}
