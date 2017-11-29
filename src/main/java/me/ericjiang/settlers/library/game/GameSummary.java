package me.ericjiang.settlers.library.game;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@SuppressWarnings("unused")
public abstract class GameSummary {

    private final String id;

    private final String name;

    public GameSummary(Game game) {
        this.id = game.getId();
        this.name = game.getName();
    }

}
