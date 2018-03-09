package me.ericjiang.frontiersmen.library.game;

import lombok.AllArgsConstructor;
import me.ericjiang.frontiersmen.library.StateEvent;

@AllArgsConstructor
public class GameUpdateEvent extends StateEvent {

    @SuppressWarnings("unused")
    private final Game game;

}
