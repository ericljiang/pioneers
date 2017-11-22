package me.ericjiang.settlers.simple;

import me.ericjiang.settlers.library.game.Game;
import me.ericjiang.settlers.library.game.GameSummary;

public class SimpleGame extends Game {

    public SimpleGame(String name) {
        super(name);
    }

    @Override
    public GameSummary summarize() {
        return new SimpleGameSummary(this);
    }

}
