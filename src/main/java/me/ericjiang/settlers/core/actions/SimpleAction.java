package me.ericjiang.settlers.core.actions;

import lombok.Getter;
import me.ericjiang.settlers.core.game.Game;

@Getter
public class SimpleAction extends PlayerAction {

    private String data;

    @Override
    public void accept(Game game) {
        game.processAction(this);
    }
}