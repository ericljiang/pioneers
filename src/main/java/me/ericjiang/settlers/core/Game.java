package me.ericjiang.settlers.core;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

import lombok.Getter;

import me.ericjiang.settlers.core.actions.PlayerAction;

@Getter
public class Game {

    @Getter
    public static enum Expansion {
        BASE(3, 4),
        EXTENDED(5, 6);

        private int minPlayers;
        private int maxPlayers;

        Expansion(int minPlayers, int maxPlayers) {
            this.minPlayers = minPlayers;
            this.maxPlayers = maxPlayers;
        }

        @Override
        public String toString() {
            return name().substring(0, 1).toUpperCase() + name().substring(1).toLowerCase();
        }
    }

    private final String id;
    private String name;
    private List<Player> players;
    private Expansion expansion;

    public Game(String name, String expansion) {
        id = UUID.randomUUID().toString();
        this.name = name;
        this.expansion = Expansion.valueOf(expansion);
        players = new ArrayList<Player>(this.expansion.getMaxPlayers());
    }

    public void processAction(PlayerAction action) {
        // TODO
        // validate
        // broadcaste changes to players e.g. player.update(action)
    }

    public int currentPlayerCount() {
        return players.size();
    }

    public boolean isOpen() {
        return currentPlayerCount() < expansion.getMaxPlayers();
    }

    public int getMaxPlayers() {
        return expansion.getMaxPlayers();
    }

    public boolean hasPlayer(String playerId) {
        for (Player player : players) {
            if (player.id() == playerId) {
                return true;
            }
        }
        return false;
    }

    public static Expansion[] values() {
        return Expansion.values();
    }
}
