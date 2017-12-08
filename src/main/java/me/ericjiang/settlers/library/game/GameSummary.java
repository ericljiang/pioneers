package me.ericjiang.settlers.library.game;

import java.util.Map;
import java.util.Set;

import lombok.AllArgsConstructor;
import me.ericjiang.settlers.library.player.Player;

@AllArgsConstructor
@SuppressWarnings("unused")
public abstract class GameSummary {

    private final String id;

    private final String owner;

    private final String name;

    private final Map<String, Player> players;

    private final boolean pregame;

    public GameSummary(Game game) {
        this.id = game.getId();
        this.owner = game.getOwner();
        this.name = game.getName();
        this.players = game.getPlayers();
        this.pregame = game.isPregame();
    }

}
