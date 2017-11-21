package me.ericjiang.settlers.simple;

import me.ericjiang.settlers.library.Game;
import me.ericjiang.settlers.library.GameSummary;
import me.ericjiang.settlers.library.data.GameDao;

public class SimpleGame extends Game {

    public SimpleGame(String name, GameDao gameDao) {
        super(name, gameDao);
    }

    @Override
    public GameSummary summarize() {
        return new SimpleGameSummary(this);
    }

}
