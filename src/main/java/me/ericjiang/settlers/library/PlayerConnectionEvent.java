package me.ericjiang.settlers.library;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class PlayerConnectionEvent extends Event {

    private final String playerId;

}
