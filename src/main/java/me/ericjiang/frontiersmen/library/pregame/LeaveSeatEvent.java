package me.ericjiang.frontiersmen.library.pregame;

import lombok.Getter;
import me.ericjiang.frontiersmen.library.PlayerEvent;

public class LeaveSeatEvent extends PlayerEvent {

    @Getter
    private int seat;

}
