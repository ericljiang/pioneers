package me.ericjiang.frontiersmen.library.pregame;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.ericjiang.frontiersmen.library.PlayerEvent;

@AllArgsConstructor
public class LeaveSeatEvent extends PlayerEvent {

    @Getter
    private int seat;

}
