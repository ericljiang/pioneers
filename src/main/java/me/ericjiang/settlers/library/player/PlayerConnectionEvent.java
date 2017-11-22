package me.ericjiang.settlers.library.player;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.ericjiang.settlers.library.Event;

@Getter
@AllArgsConstructor
public class PlayerConnectionEvent extends Event {

    private final String playerId;

}
