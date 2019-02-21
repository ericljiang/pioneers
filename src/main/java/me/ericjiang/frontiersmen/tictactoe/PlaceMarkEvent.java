package me.ericjiang.frontiersmen.tictactoe;

import lombok.Getter;
import lombok.Setter;
import me.ericjiang.frontiersmen.library.PlayerEvent;

@Getter
@Setter
public class PlaceMarkEvent extends PlayerEvent {
    private int row;
    private int col;
}
