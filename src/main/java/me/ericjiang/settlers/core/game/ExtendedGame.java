package me.ericjiang.settlers.core.game;

import java.time.LocalDateTime;

public class ExtendedGame extends Game {

    public ExtendedGame(String id, LocalDateTime creationTime, String name) {
        super(id, creationTime, name);
    }

    @Override
    public void initializeBoard() {

    }

    @Override
    public String getExpansion() {
        return GameFactory.EXTENDED;
    }

    @Override
    public int getMinPlayers() {
        return 5;
    }

    @Override
    public int getMaxPlayers() {
        return 6;
    }
}
