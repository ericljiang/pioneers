package me.ericjiang.settlers.library.game;

import lombok.AllArgsConstructor;
import me.ericjiang.settlers.library.StateEvent;

@AllArgsConstructor
public class GameUpdateEvent extends StateEvent {

    @SuppressWarnings("unused")
    private final Game game;

}
