package me.ericjiang.settlers.core.game;

public class BaseGame extends Game {

    public BaseGame(String id, String name) {
        super(id, name);
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
