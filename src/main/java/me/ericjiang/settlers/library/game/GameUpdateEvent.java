package me.ericjiang.settlers.library.game;

import lombok.AllArgsConstructor;
import me.ericjiang.settlers.library.Event;

@AllArgsConstructor
public class GameUpdateEvent extends Event {

    @SuppressWarnings("unused")
    private final Game game;

}
