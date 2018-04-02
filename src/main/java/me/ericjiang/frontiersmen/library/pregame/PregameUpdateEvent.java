package me.ericjiang.frontiersmen.library.pregame;

import lombok.AllArgsConstructor;
import me.ericjiang.frontiersmen.library.StateEvent;

@AllArgsConstructor
public class PregameUpdateEvent extends StateEvent {

    @SuppressWarnings("unused")
    private final Pregame pregame;

}
