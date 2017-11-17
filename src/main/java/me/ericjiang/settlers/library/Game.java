package me.ericjiang.settlers.library;

public abstract class Game extends MultiplayerModule {

    private final String name;

    public Game(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }
}
