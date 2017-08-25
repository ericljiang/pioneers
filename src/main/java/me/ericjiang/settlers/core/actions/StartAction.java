package me.ericjiang.settlers.core.actions;

import me.ericjiang.settlers.core.game.Game;

public class StartAction extends PlayerAction {

    @Override
    public void accept(Game game) {
        game.handleStartAction(this);
    }
}
