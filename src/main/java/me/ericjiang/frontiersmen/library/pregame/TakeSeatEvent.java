package me.ericjiang.frontiersmen.library.pregame;

import lombok.Getter;
import me.ericjiang.frontiersmen.library.PlayerEvent;

public class TakeSeatEvent extends PlayerEvent {

    @Getter
    private int seat;

}
