package me.ericjiang.settlers.library.game;

import java.util.Set;

import lombok.AllArgsConstructor;

@AllArgsConstructor
@SuppressWarnings("unused")
public abstract class GameSummary {

    private final String id;

    private final String owner;

    private final String name;

    private final Set<String> players;

    public GameSummary(Game game) {
        this.id = game.getId();
        this.owner = game.getOwner();
        this.name = game.getName();
        this.players = game.getPlayers();
    }

}
