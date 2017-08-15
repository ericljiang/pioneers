package me.ericjiang.settlers.core.actions;

import lombok.Getter;
import me.ericjiang.settlers.core.game.Game;

public class JoinAction extends PlayerAction {

    @Getter
    private String color;

    public JoinAction(String playerId, String playerName, String color) {
        super(playerId, playerName);
        this.color = color;
    }

    @Override
    public void accept(Game game) {
        game.handleJoinAction(this);
    }
}
