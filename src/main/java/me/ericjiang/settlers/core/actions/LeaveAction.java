package me.ericjiang.settlers.core.actions;

import lombok.Getter;
import me.ericjiang.settlers.core.game.Game;

public class LeaveAction extends PlayerAction {

    @Getter
    private String color;

    public LeaveAction(String playerId, String playerName, String color) {
        super(playerId, playerName);
        this.color = color;
    }

    @Override
    public void accept(Game game) {
        game.handleLeaveAction(this);
    }
}
