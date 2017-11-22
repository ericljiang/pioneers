package me.ericjiang.settlers.library.game;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@SuppressWarnings("unused")
public abstract class GameSummary {

    private final String name;

    public GameSummary(Game game) {
        this.name = game.getName();
    }

}
