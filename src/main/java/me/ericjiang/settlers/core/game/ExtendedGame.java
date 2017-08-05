package me.ericjiang.settlers.core.game;

public class ExtendedGame extends Game {

    public ExtendedGame(String name) {
        super(name);
    }

    public String getExpansion() {
        return GameFactory.EXTENDED;
    }

    public int getMinPlayers() {
        return 5;
    }

    public int getMaxPlayers() {
        return 6;
    }

}