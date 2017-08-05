package me.ericjiang.settlers.core.game;

public class BaseGame extends Game {

    public BaseGame(String name) {
        super(name);
    }

    public String getExpansion() {
        return GameFactory.BASE;
    }

    public int getMinPlayers() {
        return 3;
    }

    public int getMaxPlayers() {
        return 4;
    }
}