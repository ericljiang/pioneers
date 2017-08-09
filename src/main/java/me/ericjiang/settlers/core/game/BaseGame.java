package me.ericjiang.settlers.core.game;

import java.time.LocalDateTime;

public class BaseGame extends Game {

    public BaseGame(String id, LocalDateTime creationTime, String name) {
        super(id, creationTime, name);
    }

    @Override
    public void initializeBoard() {

    }

    @Override
    public String getExpansion() {
        return GameFactory.BASE;
    }

    @Override
    public int getMinPlayers() {
        return 3;
    }

    @Override
    public int getMaxPlayers() {
        return 4;
    }
}
