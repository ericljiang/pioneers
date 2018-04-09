package me.ericjiang.frontiersmen.library.pregame;

import lombok.AllArgsConstructor;
import lombok.Getter;
import me.ericjiang.frontiersmen.library.PlayerEvent;

@AllArgsConstructor
public class TakeSeatEvent extends PlayerEvent {

    @Getter
    private int seat;

}
