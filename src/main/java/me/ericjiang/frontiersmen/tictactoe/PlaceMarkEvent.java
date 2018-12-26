package me.ericjiang.frontiersmen.tictactoe;

import lombok.Getter;
import me.ericjiang.frontiersmen.library.PlayerEvent;

@Getter
public class PlaceMarkEvent extends PlayerEvent {
    private int row;
    private int col;
}
