package me.ericjiang.settlers.core.game;

public class ExtendedGame extends Game {

    public ExtendedGame(String id, String name) {
        super(id, name);
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
