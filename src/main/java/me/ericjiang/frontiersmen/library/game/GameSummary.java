package me.ericjiang.frontiersmen.library.game;

import java.util.Map;
import java.util.Set;

import lombok.AllArgsConstructor;
import me.ericjiang.frontiersmen.library.player.Player;

@AllArgsConstructor
@SuppressWarnings("unused")
public abstract class GameSummary {

    private final String id;

    private final String name;

    private final Player creator;

    private final Map<String, Player> players;

    public GameSummary(Game game) {
        this.id = game.getId();
        this.name = game.getName();
        this.creator = game.getCreator();
        this.players = game.getPlayers();
    }

}
