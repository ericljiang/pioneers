package me.ericjiang.settlers.library;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@SuppressWarnings("unused")
public class GameSummary {

    private final String name;

    public GameSummary(Game game) {
        this.name = game.getName();
    }

}
